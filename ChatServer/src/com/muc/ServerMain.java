package com.muc;

public class ServerMain {
    public static void main(String[] args){
        final int port = 9999;
        Server server = new Server(port);
        server.start();

    }


}
