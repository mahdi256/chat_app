package com.muc;

public class ServerMain { // 2 Server an unterschiedlichen Ports werden erstellt
	public static void main(String[] args) {
		final int port1 = 9999;
		final int port2 = 9998;
		Server server1 = new Server(port1);
		Server server2 = new Server(port2);
		server1.start();
		server2.start();

	}

}
