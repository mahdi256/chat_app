package com.muc;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
	private final int serverPort;
	private ArrayList<ServerWorker> workerList;

	// constructor (Port und ServerWorkerListe wird von ServerMain übergeben)
	public Server(int serverPort, ArrayList<ServerWorker> workerList) {
		this.serverPort = serverPort;
		this.workerList = workerList;
	}

	public List<ServerWorker> getWorkerList() {
		return workerList;
	}

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(serverPort); //Socket wird erstellt
			while (true) { //Endlosschleife
				Socket clientSocket = serverSocket.accept(); //Socket wartet auf Eröffung eines Sockets von einem Client
				// create a new Thread for each user/Client
				ServerWorker worker = new ServerWorker(this, clientSocket);
				synchronized (workerList) {
					workerList.add(worker);
					worker.start();
				}

			} // end While
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeWorker(ServerWorker serverWorker) {
		workerList.remove(serverWorker);
	}

	public int getServerPort() {
		return this.serverPort;
	}
}
