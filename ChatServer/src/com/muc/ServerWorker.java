package com.muc;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

	public ServerWorker(Server server, Socket clientSocket) {
		this.server = server;
		this.clientSocket = clientSocket;
		this.workerList = server.getWorkerList();
	}

	@Override // muss noch bearbeitet werden
	public void run() {
		try {
			handleClientSocket();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public PrintWriter getPrintWriter(){
		return this.printWriter;
	}

	//gibt Username als String zurück
	public String getUsername() {
		return this.username;
	}

	//gibt geöffneten Chat als String zurück
	public String getOpenedChat(){
		return this.openedChat;
	}


	//Methode, um UI-Input in String[] participants zu packen
	private String[] createParticipantArray(String InputStream){
		String[] temp = null;
		InputStream = InputStream + username;

		temp = InputStream.split("#");

		String[] participants = new String[temp.length];

		int x = 0;
		for(int i = 0; i < temp.length; i++){
			participants[i] = temp[x];
			x++;
		}

		return participants;

	}


	// Methode,  die Input des Login-Screens überprüft in dem Abgleich mit userlist
	private void handleLogin(String[] tokens) throws IOException {
			String[] internTokens = tokens[1].split(" ");
			String username = internTokens[0];
			String password = internTokens[1];
			String msg = null;
			String entryMethod = internTokens[2];

			int cValue = FileQuery.checkLoginCredentials(username, password);

			if (entryMethod.equalsIgnoreCase("login")) {

				if(cValue == 2){
					msg = "login successful";// "ok login" wird an Client gesendet wenn Logindaten korrekt
                    printWriter.println(msg);
                    printWriter.flush();
					this.username = username; //username wird in Klassen-Variable gespeichert
					System.out.println("User logged in succesfully: " + username + " Server: " + this.server.getServerPort());
					//findAllChatsOfUser(username); LUCA
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
					FileQuery.addUserToUserlist(username, password);
					msg = "registration successful";
					this.username = username; //username wird in Klassen-Variable gespeichert
					System.out.println("User registered succesfully: " + username + " Server: " + this.server.getServerPort());
					//outputStream.write(msg.getBytes());
                    printWriter.println(msg);
                    printWriter.flush();
				}
			}
		}


	//lädt alle begonnen Konversationen des User, Rückgabe als String[]
	public void loadChatlist(){
		String[] chatlist = FileQuery.getAllChatsOfUser(this.username);
		String msg = "chatList ";
		for(int i = 0; i<chatlist.length; i++){
			msg= msg + chatlist[i]+ " ";
		}

		System.out.println("msg: "+msg);

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
		/*for(int i = 0;i<participants.length;i++){
			System.out.println(participants[i]);
		}*/
		String chathistory = FileQuery.readChat(participants);

		String msg ="chat " + chatName  + " " + chathistory;

		try{
			//outputStream.write(msg.getBytes());
			printWriter.println(msg);
			printWriter.flush();
		}catch (Exception e){
			e.printStackTrace();
		}


	}

	//und Methode, die gesendete Nachricht in Chatverlauf schreibt  Chats aller User, die diesen geöffnet haben, aktualisiert
	/*private void sendMessage(InputStream /* message ){
		String username = getUsername();
		String openedChat = getOpenedChat();
		String[] participants = createParticipantArray(openedChat);
		FileQuery.writeChatMessage(participants, username, /* message);
		loadChatAll(openedChat);
	}*/

	// Methode, die den Chat aller User aktualisiert, die in Chat drin sind & diesen gerade geöffnet haben
	/*private void loadChatAll (/*String Chattitel (participants)){
		//String input = String Chattitel (participants);
		String[] participants = createParticipantArray(/*input);
		for(ServerWorker worker : workerList){
			for (int i = 0; i < participants.length; i++){
				if (worker.getUsername().equals(participants[i]) && worker.getOpenedChat().equals(/*input)){
					worker.loadChat(/*input);
				}
			}}
	}*/

	// Methode, die User auslogged, ServerWorker + Socket schließt
	private void handleLogoff() throws IOException { //Methode ruft bei allen Server-Workern den Befehl send auf und gibt eine Nachricht bestehend aus offline und username mit
		server.removeWorker(this);
		this.workerList = server.getWorkerList();

		clientSocket.close(); //Socket wird geschlossen
	}

	private void handleClientSocket() throws IOException, InterruptedException { //Methode wird beim starten des ServerWorkers aufgerufen
	    this.inputStream = clientSocket.getInputStream();
		this.outputStream = clientSocket.getOutputStream();
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream));

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while (true) { //Endlosschleifen
			//System.out.println("handleClientSocket: warte auf eingehende Nachricht");
			line = reader.readLine(); //wartet auf eingehende Nachrichten
			if (line != null) {
				System.out.println("Eingehende Nachricht: " + line);
				String[] tokens = line.split(" ", 2); //Teilt Nachricht in Teile
				if (tokens != null && tokens.length > 0) {
					String msg = tokens[0];
					if ("logoff".equals(msg) || "quit".equalsIgnoreCase(msg)) { //wenn logoff-Commando empfangen wird
						//handleLogoff();
						break;
					} else if ("login".equalsIgnoreCase(msg)) { // wenn login-Comando empfangend wird
						handleLogin(tokens);
					} else if("chatList".equalsIgnoreCase(msg)) {
						loadChatlist();
					} else if ("message".equalsIgnoreCase(msg)) {
						handleMessage(tokens);
					} else if ("openChat".equalsIgnoreCase(msg)){
						loadChat(tokens[1]);
					} else if("createChat".equalsIgnoreCase(msg)){
						createChat(tokens[1]);
					} else if("logOff".equalsIgnoreCase(msg)){
						handleLogoff();
					}else{
						msg = "unknown " + msg; //wenn anderes Commando empfangen wird, wird eine Nachricht mit "unknown" und dem Commando an den Client geschickt
						//outputStream.write(msg.getBytes());
						printWriter.println(msg);
						printWriter.flush();
					}
				}
			}
		}

		clientSocket.close();
	}

	private void createChat(String participantsString){
		String[] participants = participantsString.split("#"); //old format: #user#user#
		boolean allUsersExist=true;
		for(int i=1; i<participants.length;i++){
			if(!FileQuery.checkUsername(participants[i])){
				System.out.println(participants[i] + "false");
				allUsersExist=false;
			}
		}
		if(!FileQuery.findChatFile(participants) && allUsersExist){
            FileQuery.createChatFile(participants);

			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String time = sdf.format(cal.getTime());

            String msg = "Chat erstellt um " + time;
            FileQuery.writeChatMessage(participants,username,msg,time);
            this.server.redirectMsg("chatlist "+participantsString);
            for(ServerWorker worker : workerList){
                for (int i = 1; i < participants.length; i++) {
					if (worker.getUsername().equals(participants[i])) {
						worker.loadChatlist();
					}
				}
            }
            System.out.println("tokens: "+participantsString);
            this.loadChat(participantsString);
        }else{
		    System.out.println("Chat bereits vorhanden oder ein User ist nicht registriert");
        }
	}

	private void handleMessage(String[] tokens){
		String[] internTokens = tokens[1].split(" ", 4);
		String message = internTokens[3];
		String[] chatname = internTokens[0].split("#");
		String sender = internTokens[1];
		String time = internTokens[2];

		FileQuery.writeChatMessage(chatname,sender,message,time);

		// refresh des Chats bei allen Clients(, die Chat offen haben)!
		this.server.redirectMsg("message "+tokens[1]);
		for (ServerWorker worker : workerList){
			for(int i = 0; i < chatname.length; i++) {
				if (chatname[i].equalsIgnoreCase(worker.username)) {//prüfen ob Client, der dem worker zugeordnet ist, Teil des Chats ist
					worker.loadChat(internTokens[0]); //loadChat() bei serverWorker aufrufen
				}
			}
		}

	}

}
