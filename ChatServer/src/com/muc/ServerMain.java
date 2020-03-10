package com.muc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args){
        final int port = 8818;
        try {
            ServerSocket serverSocket = new ServerSocket( port );
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // creat a new Thread for every user(Client)
                ServerWorker worker= new ServerWorker( clientSocket );
                worker.start();

            }// end While
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
