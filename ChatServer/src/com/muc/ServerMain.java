package com.muc;

import java.util.ArrayList;

public class ServerMain { // 2 Server an unterschiedlichen Ports werden erstellt
	public static void main(String[] args) {
		ArrayList<ServerWorker> workerList = new ArrayList<>();
		final int port1 = 9999;
		final int port2 = 9998;
		Server server1 = new Server(port1, workerList);
		Server server2 = new Server(port2, workerList);
		server1.start();
		server2.start();

	}

}
