package com.muc;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
	private PrintWriter output;
	private BufferedReader input;
	private final int serverPort;
	private ServerSocket serverSocket;
	private ArrayList<ServerWorker> workerList;
	private FileQuery fileQuery;

	// Konstruktor (Port und ServerWorkerListe wird von ServerMain übergeben)
	public Server(int serverPort) {
		this.serverPort = serverPort;
	}



	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket( this.getServerPort() );
			this.fileQuery = new FileQuery(this);
			this.fileQuery.createUserlist();
			if(this.getServerPort() == 9999){    // Anhand des Ports werden die Server erkannt und Input und Output Streams werden erstellt
				Socket socket = new Socket("localhost", 9998); //Der Server am Port 9999 erstellt einen Socket, der sich mit dem Server am Port 9998 verbindet
				this.output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			}else{
				Socket otherServer = serverSocket.accept(); //Der Server am Port 9998 akzeptiert die Verbindung
				this.input = new BufferedReader(new InputStreamReader(otherServer.getInputStream()));
				this.output = new PrintWriter(new OutputStreamWriter(otherServer.getOutputStream()));

			}
			this.workerList = new ArrayList<ServerWorker>();

			Thread t1 = new Thread() {
				@Override
				public void run() {
					acceptLoop();
				}
			};
			Thread t2 = new Thread() {
				@Override
				public void run() {
					interServerCommunicationLoop();
				}
			};
			t1.start();
			t2.start();


		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public void acceptLoop(){  //Methode, die Anfragen der Clients annimmt
		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				// Für jeden Client, der eine Verbindungsanfrage an diesen Server sendet, wird ein neuer Thread erstellt
				ServerWorker worker = new ServerWorker(this, clientSocket, fileQuery);
				workerList.add(worker);
				worker.start();
			}catch (Exception e){
				e.printStackTrace();
			}

		}// end While
	}

	public void interServerCommunicationLoop(){ //Methode, die Nachrichten des jeweiligen anderen Severs empfängt
		while (true) {
			try {
				String msg = input.readLine();
				String[] tokens = msg.split(" ", 2);
				if (tokens != null && tokens.length > 0) {
					if (tokens[0].equalsIgnoreCase("chatlist")) {
						String[] participants = tokens[1].split("#");
						for (ServerWorker sw : workerList) {
							for (int i = 1; i < participants.length; i++) {
								if (sw.getUsername().equalsIgnoreCase(participants[i])) {
									sw.loadChatlist();
								}
							}
						}
					} else if (tokens[0].equalsIgnoreCase("message")) {
						String[] internTokens = tokens[1].split(" ", 4);
						String[] participants = internTokens[0].split("#");
						for (ServerWorker worker : workerList) {
							for (int i = 0; i < participants.length; i++) {
								if (participants[i].equalsIgnoreCase(worker.getUsername())) {//prüfen ob Client, der dem worker zugeordnet ist, Teil des Chats ist
									worker.loadChat(internTokens[0]); //loadChat() bei serverWorker aufrufen
								}
							}
						}
					}else if(tokens[0].equalsIgnoreCase("addUser")){
						String[] data = tokens[1].split(" ", 2);
						fileQuery.addUserToUserlist(data[0], data[1]);
					}else if(tokens[0].equalsIgnoreCase("createChat")){
						String[] participants = tokens[1].split(" ");
						File file = fileQuery.getFilename(participants);
						try {
							file.createNewFile();
						}catch (IOException e){
							e.printStackTrace();
						}
					}else if(tokens[0].equalsIgnoreCase("writeMessage")){
						String[] internTokens = tokens[1].split(" ",4);
						String[] participants = internTokens[0].split("#");
						fileQuery.writeChatMessage(participants, internTokens[1], internTokens[3], internTokens[2]);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void redirectMsg(String msg){ //Methode, um Nachrichten an den jeweils anderen Server zu senden
		output.println(msg);
		output.flush();
	}

	public List<ServerWorker> getWorkerList() {
		return workerList;
	}

	public void removeWorker(ServerWorker serverWorker) {
		workerList.remove(serverWorker);
	}

	public int getServerPort() {
		return this.serverPort;
	}
}
