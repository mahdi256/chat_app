package com.muc;

import java.io.*;
import java.net.*;

public class ServerWorker extends Thread {
    private final Socket clientSocket;
    private String login = null;

    public ServerWorker(Socket clientSocket){
        this.clientSocket=clientSocket;
    }
    @Override
    public void run(){
        try {
            handleClientSocket();
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    private void handleClientSocket()throws IOException,InterruptedException{
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine()) != null){

        }

        clientSocket.close();
    }
}
