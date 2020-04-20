package com.muc;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;


public class ServerWorker extends Thread {

	private final Socket clientSocket;
	private final Server server;
	private String username = null;
	private String openedChat = null;
	private OutputStream outputStream;
	private InputStream inputStream;
	private List<ServerWorker> workerList;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private FileQuery fileQuery;
	private Boolean stophandleClientSocket = false;

	public ServerWorker(Server server, Socket clientSocket, FileQuery fileQuery) {
		this.server = server;
		this.clientSocket = clientSocket;
		this.workerList = server.getWorkerList();
		this.fileQuery = fileQuery;
	}

	@Override
	public void run() {
		try {

			handleClientSocket();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getUsername() {
		return this.username;
	}


	// Methode,  die Input des Login-Screens überprüft durch Abgleich mit userlist
	private void handleLogin(String[] tokens) throws IOException {
			String[] internTokens = tokens[1].split(" ");
			String username = internTokens[0];
			String password = internTokens[1];
			String msg = null;
			String entryMethod = internTokens[2];

			int cValue = fileQuery.checkLoginCredentials(username, password);

			if (entryMethod.equalsIgnoreCase("login")) {

				if(cValue == 2){
					msg = "login successful";// "login successful" wird an Client gesendet wenn Logindaten korrekt
                    printWriter.println(msg);
                    printWriter.flush();
					this.username = username; //username wird in Klassen-Variable gespeichert
					System.out.println("User logged in succesfully: " + username + " Server: " + this.server.getServerPort());
				}else{
					msg = "login failed";
                    printWriter.println(msg);
                    printWriter.flush();
				}
			}else if(entryMethod.equalsIgnoreCase("register")){
				if(cValue == 1 || cValue==2){
					msg = "registration failed";
                    printWriter.println(msg);
                    printWriter.flush();
				}else if(cValue == 0){
					fileQuery.addUserToUserlist(username, password);
					msg = "registration successful";
					this.username = username; //username wird in Klassen-Variable gespeichert
					System.out.println("User registered succesfully: " + username + " Server: " + this.server.getServerPort());
                    printWriter.println(msg);
                    printWriter.flush();
				}
			}
		}


	//lädt alle begonnen Konversationen des User, Rückgabe als String[]
	public void loadChatlist(){
		String[] chatlist = fileQuery.getAllChatsOfUser(this.username);
		String msg = "chatList ";
		for(int i = 0; i<chatlist.length; i++){
			msg= msg + chatlist[i]+ " ";
		}
		try {
            printWriter.println(msg);
            printWriter.flush();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	// Methode, die aktualisierten Chat an Client sendet
	public void loadChat(String chatName){ //chatName format: #username#username#
		String[] participants = chatName.split("#");
		String chathistory = fileQuery.readChat(participants);
		String msg ="chat " + chatName  + " " + chathistory;
		try{
			printWriter.println(msg);
			printWriter.flush();
		}catch (Exception e){
			e.printStackTrace();
		}


	}

	// Methode, die User auslogged, ServerWorker + Socket schließt
	private void handleLogoff() throws IOException {
		server.removeWorker(this);
		this.workerList = server.getWorkerList();
		printWriter.println("logoff");
		printWriter.flush();
	}

	//Methode wird beim starten des ServerWorkers aufgerufen und ließt Nachrichten, die vom Client gesendet wurden, und verarbeitet diese entsprechend
	private void handleClientSocket() throws IOException, InterruptedException {
	    this.inputStream = clientSocket.getInputStream();
		this.outputStream = clientSocket.getOutputStream();
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream));

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while (true) {
			line = reader.readLine(); //wartet auf eingehende Nachrichten
			if (line != null) {
				System.out.println("Eingehende Nachricht am Server:  " + line);
				String[] tokens = line.split(" ", 2); //Teilt Nachricht in Teile
				if (tokens != null && tokens.length > 0) {
					String msg = tokens[0];
					if ("logoff".equals(msg)) {
						handleLogoff();
						stophandleClientSocket = true;
					} else if ("login".equalsIgnoreCase(msg)) {
						handleLogin(tokens);
					} else if("chatList".equalsIgnoreCase(msg)) {
						loadChatlist();
					} else if ("message".equalsIgnoreCase(msg)) {
						handleMessage(tokens);
					} else if ("openChat".equalsIgnoreCase(msg)){
						loadChat(tokens[1]);
					} else if("createChat".equalsIgnoreCase(msg)){
						createChat(tokens[1]);
					}
				}
				if(stophandleClientSocket){
					clientSocket.close();
					System.out.println("Socket von ServerWorker des Users " + username + " wurde geschlossen");
					break;
				}
			}
		}

		clientSocket.close();
	}

	//Methode, die einen Chat erstellt und von der GUI aufgerufen wird
	private void createChat(String participantsString){
		String[] participants = participantsString.split("#");
		boolean allUsersExist=true;
		for(int i=1; i<participants.length;i++){ // prüft ob alle Nutzer, mit denen ein Chat begonnen werden soll, registriert sind
			if(!fileQuery.checkUsername(participants[i])){
				System.out.println(participants[i] + "false");
				allUsersExist=false;
			}
		}
		if(!fileQuery.findChatFile(participants) && allUsersExist){
            fileQuery.createChatFile(participants);

			Date date = new Date();
			String time = date+"";
			time = time.split(" ")[3];

            String msg = "Chat erstellt um " + time;
            fileQuery.writeChatMessage(participants,username,msg,time);
            this.server.redirectMsg("chatlist "+participantsString);
            for(ServerWorker worker : workerList){ // Benachrichtigt alle am Chat beteilgten Clients über den neuen Chat
                for (int i = 1; i < participants.length; i++) {
					System.out.println("Teilnehmer in neuem Chat: "+ participants[i]);
					if (worker.getUsername().equals(participants[i])) {
						worker.loadChatlist();
					}
				}
            }
        }else{
		    System.out.println("Chat bereits vorhanden oder mindestens ein User ist nicht registriert");
        }
	}

	//Diese Methode verarbeitet ausgehende Chatnachrichten und wird vom GUI aufgerufen
	private void handleMessage(String[] tokens){
		String[] internTokens = tokens[1].split(" ", 4);
		String message = internTokens[3];
		String[] chatname = internTokens[0].split("#");
		String sender = internTokens[1];
		String time = internTokens[2];

		fileQuery.writeChatMessage(chatname,sender,message,time);

		// neuladen des Chats bei allen Clients, die Chat zur Zeit geöffnet haben
		this.server.redirectMsg("message "+tokens[1]);
		for (ServerWorker worker : workerList){
			for(int i = 0; i < chatname.length; i++) {
				if (chatname[i].equalsIgnoreCase(worker.username)) { //prüfen ob Client, der dem worker zugeordnet ist, Teil des Chats ist
					worker.loadChat(internTokens[0]); //loadChat() bei serverWorker aufrufen
				}
			}
		}

	}

}
