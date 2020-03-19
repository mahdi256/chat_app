package com.muc;

import java.io.*;
import java.net.*;

/**
 * Created by Mahmoud on 03,2020
 */
public class ChatClient {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedredIn;

    // Constructor
    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("localhost", 9999);
        if(!client.connect()){
            System.err.println( "Connect failed" );
        }else{
            System.out.println( "Connect successful" );
            if (client.login("test1", "test1")){
                System.out.println( "login successful" );
            }else {
                System.err.println( "Login failed" );
            }
        }
    }

    private boolean login(String username, String password)throws IOException {
        String cmd = "login" + username+" "+password+ "\n";
        serverOut.write( cmd.getBytes() );

        String response = bufferedredIn.readLine();
        System.out.println("Server Response "+ response );
        if(response.equalsIgnoreCase( response ) ){
            return true;
        }else {
            return false;
        }
    }

    private boolean connect() {
        try {
                this.socket = new Socket( serverName, serverPort );
                this.serverIn = socket.getInputStream();
                this.serverOut = socket.getOutputStream();
                this.bufferedredIn = new BufferedReader( new InputStreamReader( serverIn));
                return true;
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
}
