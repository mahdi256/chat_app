package com.muc;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

public class ServerWorker extends Thread {

	private final Socket clientSocket;
	private final Server server;
	private String login = null;
	private OutputStream outputStream;
	private HashSet<String> topicSet = new HashSet<>();

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
		while (true) { //Endlosschleifen
			line = reader.readLine(); //wartet auf eingehende Nachrichten
			if (line != null) {
				System.out.println(line);
				String[] tokens = line.split(" "); //Teilt Nachricht in Teile
				if (tokens != null && tokens.length > 0) {
					String cmd = tokens[0];
					if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd)) { //wenn logoff-Commando empfangen wird
						handleLogoff();
						break;
					} else if ("login".equalsIgnoreCase(cmd)) { // wenn login-Comanndo empfangend wird
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
				}
			}
		}

		clientSocket.close();
	}

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

	// format: "msg" "login" body...
	// format: "msg" "#topic" body...
	private void handleMessage(String[] tokens) throws IOException {
		String sendTo = tokens[1];
		String body = tokens[2];

		boolean isTopic = sendTo.charAt(0) == '#'; // Prüfen ob ertes Zeichen von sendTo # entspeicht -> # bedeutet Gruppenchat

		List<ServerWorker> workerList = server.getWorkerList(); //erstellt lokale Kopie der WorkerList vom Server
		for (ServerWorker worker : workerList) {
			if (isTopic) { //wird ausgeführt wenn es sich um einen Gruppenchat handelt
				if (worker.isMemberOfTopic(sendTo)) {
					String outMsg = "msg " + sendTo + ":" + login + " " + body + "\n";
					worker.send(outMsg); // Commando msg mit Topic:username wird an CLient gesendet
	//Hier fehlt etwas: Es wird weder im ServerWorker noch beim Client geprüft ob der User teil der Topic(Gruppe) ist
				}
			} else { //wird ausgeführt, wenn es kein Gruppenchat ist
				if (sendTo.equalsIgnoreCase(worker.getLogin())) {
					String outMsg = "msg " + login + " " + body + "\n";
					worker.send(outMsg); //Commando mit msg wird an Client gesendet
				}
			}
		}
	}

	private void handleLogoff() throws IOException { //Methode ruft bei allen Server-Workern den Befehl send auf und gibt eine Nachricht bestehend aus offline und username mit
		server.removeWorker(this);
		List<ServerWorker> workerList = server.getWorkerList();

		// send other online users current user's status
		String onlineMsg = "offline " + login + "\n";
		for (ServerWorker worker : workerList) {
			if (!login.equals(worker.getLogin())) {
				worker.send(onlineMsg);
			}
		}
		clientSocket.close(); //Socket wird geschlossen
	}

	public String getLogin() {
		return login;
	}

	private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException { //Methode 
		if (tokens.length == 3) {
			String login = tokens[1]; //login = username
			String password = tokens[2];

			if ((login.equals("test1") && password.equals("test1")) // username und password werden verglichen
					|| (login.equals("test2") && password.equals("test2"))) {
				String msg = "ok login\n";
				outputStream.write(msg.getBytes()); // "ok login" wird an Client gesendet wenn Logindaten korrekt
				this.login = login; //username wird in Klassen-Variable gespeichert
				System.out.println("User logged in succesfully: " + login + " Server: " + this.server.getServerPort());

				List<ServerWorker> workerList = server.getWorkerList(); // lokale Kopie der workerList vom sever wird erstellt

				// send current user all other online logins (sendet Liste aller Clients die online sind an zu diesem ServerWorker gehörenden Client)
				for (ServerWorker worker : workerList) {
					if (worker.getLogin() != null) {
						if (!login.equals(worker.getLogin())) {
							String msg2 = "online " + worker.getLogin() + "\n";
							send(msg2);
						}
					}
				}

				// send other online users current user's status (sendet Nachricht an alle Clients, dass dieser Client online gegangen ist)
				String onlineMsg = "online " + login + "\n";
				for (ServerWorker worker : workerList) {
					if (!login.equals(worker.getLogin())) {
						worker.send(onlineMsg);
					}
				}
			} else {
				String msg = "error login\n";
				outputStream.write(msg.getBytes());
			}
		}
	}

	private void send(String msg) throws IOException { //Methode sendet Nachricht msg über outputStream an Client (flush wird nicht benötigt, wenn Nachricht \n enhält)
		if (login != null) {
			outputStream.write(msg.getBytes());
		}
	}
}
