package com.muc;

import javax.swing.text.AbstractDocument;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ServerWorker extends Thread {

	private final Socket clientSocket;
	private final Server server;
	private String username = null;
	private String openedChat = null;
	private OutputStream outputStream;
	private HashSet<String> topicSet = new HashSet<>();
	private ArrayList<ServerWorker> workerList;

	public ServerWorker(Server server, Socket clientSocket) {
		this.server = server;
		this.clientSocket = clientSocket;
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

	private void handleClientSocket() throws IOException, InterruptedException { //Methode wird beim starten des ServerWorkers aufgerufen
		InputStream inputStream = clientSocket.getInputStream();
		this.outputStream = clientSocket.getOutputStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		/*while (true) { //Endlosschleifen
			line = reader.readLine(); //wartet auf eingehende Nachrichten
			if (line != null) {
				System.out.println(line);
				String[] tokens = line.split(" "); //Teilt Nachricht in Teile
				if (tokens != null && tokens.length > 0) {
					String cmd = tokens[0];
					if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd)) { //wenn logoff-Commando empfangen wird
						handleLogoff();
						break;
					} else if ("login".equalsIgnoreCase(cmd)) { // wenn login-Comando empfangend wird
						handleLogin(outputStream, tokens);
					} else if ("msg".equalsIgnoreCase(cmd)) { // wenn msg-Commando empfangend wird
						String[] tokensMsg = line.split(" ", 3);
						handleMessage(tokensMsg);
					} else if ("join".equalsIgnoreCase(cmd)) { //wenn join-Commando empfangen wird
						handleJoin(tokens);
					} else if ("leave".equalsIgnoreCase(cmd)) { //wenn leave-Commando empfangen wird
						handleLeave(tokens);
					} else {
						String msg = "unknown " + cmd + "\n"; //wenn anderes Commando empfangen wird, wird eine Nachricht mit "unknown" und dem Commando an den Client geschickt
						outputStream.write(msg.getBytes());
					}
					//Kommando von Client wenn er Chat anfordert:
						//Methode zum auslesen und zurückgeben eines Chats aufrufen
					
					//Kommando von Client wenn er neuen Chat erstellt hat
						//Methode zum erstellen eines neuen Chats aufrufen
				}
			}
		}

		clientSocket.close();*/
	}
/*
	private void handleLeave(String[] tokens) {//Methode die Topic(Gruppe) aus tokens[1] der HashMap Topic entfernt
		if (tokens.length > 1) {
			String topic = tokens[1];
			topicSet.remove(topic);
		}
	}

	public boolean isMemberOfTopic(String topic) { //Prüft ob Topic existiert und erstellt sie, falls nicht
		return topicSet.contains(topic);
	}

	private void handleJoin(String[] tokens) { //Methode die Topic(Gruppe) aus tokens[1] der HashMap Topic hinzugefügt
		if (tokens.length > 1) {
			String topic = tokens[1];
			topicSet.add(topic);
		}
	}
*/
//zu ergänzen: Trennzeichen zwischen user und body oder user kennzeichen oder anders lösen
	// format: "msg" "login" body...
	// format: "msg" "login1" "login2" "login3" "login4" body..
//	private void handleMessage(String[] tokens) throws IOException {
//		String sendTo = tokens[1];
//ergänzen: sendTo Loop handeln
//		String body = tokens[2];

	private void sendMessage(InputStream /* message */){
		String username = getUsername();
		String openedChat = getOpenedChat();
		String[] participants = createParticipantArray(openedChat);
		FileQuery.writeChatMessage(participants, username, /* message*/);
		loadChatAll(openedChat);
	}


//Methode writeChatMessage(String[] participant,String sender, String message) aufrufen
//Methode sendChatToAllParticipants(String[] users) aufrufen

		
		
//		boolean isTopic = sendTo.charAt(0) == '#'; // Prüfen ob ertes Zeichen von sendTo # entspeicht -> # bedeutet Gruppenchat

//		List<ServerWorker> workerList = server.getWorkerList(); //erstellt lokale Kopie der WorkerList vom Server
//		for (ServerWorker worker : workerList) {
//			if (isTopic) { //wird ausgeführt wenn es sich um einen Gruppenchat handelt
//				if (worker.isMemberOfTopic(sendTo)) {
//					String outMsg = "msg " + sendTo + ":" + login + " " + body + "\n";
//					worker.send(outMsg); // Commando msg mit Topic:username wird an CLient gesendet
//	//Hier fehlt etwas: Es wird weder im ServerWorker noch beim Client geprüft ob der User teil der Topic(Gruppe) ist
//				}
//			} else { //wird ausgeführt, wenn es kein Gruppenchat ist
//				if (sendTo.equalsIgnoreCase(worker.getLogin())) {
//					String outMsg = "msg " + login + " " + body + "\n";
//					worker.send(outMsg); //Commando mit msg wird an Client gesendet
//				}
//			}
//		}


	private void handleLogoff() throws IOException { //Methode ruft bei allen Server-Workern den Befehl send auf und gibt eine Nachricht bestehend aus offline und username mit
		server.removeWorker(this);
		List<ServerWorker> workerList = server.getWorkerList();

		// send other online users current user's status, muss an UI angebunden werden
		String onlineMsg = "offline " + username + "\n";
		for (ServerWorker worker : workerList) {
			if (!username.equals(worker.getUsername())) {
				worker.send(onlineMsg);
			}
		}
		clientSocket.close(); //Socket wird geschlossen
	}

	public String getUsername() {
		return username;
	}

	public String getOpenedChat(){
		return openedChat;
	}

	// Methode,  die Input des Login-Screens überprüft in dem Abgleich mit userlist
	private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
		if (tokens.length == 3) {
			String username = tokens[1];
			String password = tokens[2];
			String msg = null;

			int CValue = FileQuery.checkLoginCredentials(username, password);

			switch(CValue) {
				case 0: // User nicht vorhanden, Neu-Registrierung
					msg = "This account does not exist, would you like to create a new one with this credentials?\n";
					outputStream.write(msg.getBytes());
					//Fragefeld Ja oder Nein?
					//if (true){
					//   if(username != null && password != null) {
					//FileQuery.addUserToUserlist(username, password);
					msg = "user added\n";
					outputStream.write(msg.getBytes());
					//               openHomeScreen();
					// 	 } else "Your username and password is empty!"
					//} else schließt sich Fragefeld
					// }
					break;

				case 1: // Passwort falsch
					msg = "wrong password\n";
					outputStream.write(msg.getBytes());
					break;
				case 2: // Login erfolgreich
					msg = "ok login\n";
					outputStream.write(msg.getBytes()); // "ok login" wird an Client gesendet wenn Logindaten korrekt
					this.username = username; //username wird in Klassen-Variable gespeichert
					System.out.println("User logged in succesfully: " + username + " Server: " + this.server.getServerPort());
					//findAllChatsOfUser(username); LUCA
					break;
			}




// wenn Rückgabe = 2, rufe findeAlleChatsDesUsers(user) [diese Methode gibt alle Chats zurück] auf und sende "ok login Chat1 Chat2 Chat3 ChatN" an Client
// wenn Rückgabe = 0, rufe addUserToUserlist() auf und rufe findeAlleChatsDesUsers(user) [diese Methode gibt alle Chats zurück] auf und sende "ok login Chat1 Chat2 Chat3 ChatN" an Client
// wenn Rückgabe = 1, sende Fehler

//				List<ServerWorker> workerList = server.getWorkerList(); // lokale Kopie der workerList vom sever wird erstellt
//
//				// send current user all other online logins (sendet Liste aller Clients die online sind an zu diesem ServerWorker gehörenden Client)
//				for (ServerWorker worker : workerList) {
//					if (worker.getLogin() != null) {
//						if (!login.equals(worker.getLogin())) {
//							String msg2 = "online " + worker.getLogin() + "\n";
//							send(msg2);
//						}
//					}
//				}
//
//				// send other online users current user's status (sendet Nachricht an alle Clients, dass dieser Client online gegangen ist)
//				String onlineMsg = "online " + login + "\n";
//				for (ServerWorker worker : workerList) {
//					if (!login.equals(worker.getLogin())) {
//						worker.send(onlineMsg);
//					}
//				}
//			} else {
//				String msg = "error login\n";
//				outputStream.write(msg.getBytes());
//			}
		}
	}

/*	private void send(String msg) throws IOException { //Methode sendet Nachricht msg über outputStream an Client (flush wird nicht benötigt, wenn Nachricht \n enhält)
		if (username != null) {
			outputStream.write(msg.getBytes());
		}
	}*/

	// Methode, die aktualisierten Chat bei Klick auf Chatbutton lädt
	private void loadChat(/*String Chattitel*/){
		String input = ;//String Chattitel
		// openedChat = String Chattitel
		String[] participants = createParticipantArray(input);
		String[][] chathistory = FileQuery.readChat(participants);
		//Übergabe chathistory an UI

	}
	// Methode, die den Chat aller User aktualisiert, die in Chat drin sind & diesen gerade geöffnet haben
	private void loadChatAll (/*String Chattitel (participants)*/){
		String input = /*String Chattitel (participants)*/;
		String[] participants = createParticipantArray(input);
		for(ServerWorker worker : workerList){
			for (i = 0; i < participants.length; i++){
			if (worker.getUsername().equals(participants[i]) && worker.getOpenedChat().equals(input)){
				worker.loadChat(/*input*/);
				}
		}}
	}

	//Methode, die neuen Chat mit gegebenem Input erstellt & created-Message reinschreibt
	private void createChat(/*String InputStream (participants)*/){
		//String input = String InputStream (participants);
		String[] participants = createParticipantArray(/*input*/);
		FileQuery.createChatFile(participants);
		String msg = "Chat created on " + System.currentTimeMillis();
		FileQuery.writeChatMessage(participants, username, msg);
	}

	// Methode zum erstellen eines neuen Chats
	//usernames werden mitgegeben
	// createChatFile(String[] participant) wird aufgerufen
	// Loop über user[] mit Index i
	//	for (ServerWorker worker : workerList) {
	//		if (worker.getLogin().equals(user[i])) {
	//			worker.findeAlleChatsDesUsers();
	//		}
	//	}
	
	// Methode findeAlleChatsDesUsers()
	// String username = this.getlogin();
	// rufe auf FileQuery findeAlleChatsDesUsers(String username) auf [muss Luca ganz bald mal machen]
	// zurückgegeben wird Chat[]

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
	
}
