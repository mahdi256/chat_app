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
    private InputStream inputStream;
    private HashSet<String> chatRooms = new HashSet<String>(  );

    //constructor
    public ServerWorker(Server server,Socket clientSocket){
        this.server = server;
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
    // Message format: sender: msg <receiverName> <body>
    //                  receiver: msg <senderName> <body>
    // Message format for chatRoom: msg <#chatroom> <body>
    private void handleMessage(String[] tokens)throws IOException{
    String sendTo = tokens[1];
    String messageBody = tokens[2];
    boolean isChatRoom = sendTo.charAt( 0 )== '#';
    List<ServerWorker> workerList = server.getWorkerList();
        for(ServerWorker worker : workerList){
            if (isChatRoom){
                if(worker.isMemberOf( sendTo )){        //sendTo = chatRoom name
                    String outMsg = "msg "+sendTo+" : " +login+ " "+messageBody+"\n";
                    worker.send( outMsg );
                }

            }else if(sendTo.equals( worker.getLogin()  )){
                String outMsg = "msg "+ login+" "+messageBody+" \n";
                worker.send( outMsg );
            }

        }
    }



    private void handleClientSocket()throws IOException,InterruptedException{
        this.inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine()) != null){
            String[] tokens = line.split(" ");
            if(tokens != null && tokens.length>0){
                String cmd = tokens[0];
                if("logoff".equals( cmd )){
                    handleLogoff();
                    break;
                }else if("login".equals( cmd )) {
                    handleLogin( outputStream, tokens );
                }else if ("msg".equals( cmd )) {
                    String[] tokensMsg = line.split( " ", 3 );
                    handleMessage( tokensMsg );
                }else if("join".equals( cmd )) {
                    handleJoin( tokens );
                }else if("leave".equals( cmd )){
                    handleLeave(tokens);

                }else {
                    String msg = cmd+ " ist unbekannter Befehl \n";
                    outputStream.write( msg.getBytes() );
                }
            }
            
        }


    }

    private void handleLeave(String[] tokens) {
        if (tokens.length>1){
            String chatRoom = tokens[1];
            chatRooms.remove( chatRoom );

        }
    }

    // join <chatRoom> join #team1
    private void handleJoin(String[] tokens) {
        if (tokens.length>1){
            String chatRoom = tokens[1];
            chatRooms.add( chatRoom );

        }
    }
    public boolean isMemberOf(String chatRoom){
        return chatRooms.contains( chatRoom );
    }

    private void handleLogoff() throws IOException {
        String offline = "offline "+ login+"\n";
        server.removeWorker(this);      //delete the user form the list
        List<ServerWorker> workerList = server.getWorkerList();
        for(ServerWorker worker: workerList){ // send other online users current user's status
            if(!login.equals( worker.getLogin() )) { // don't send me my status
                worker.send( offline );
            }
        }
        clientSocket.close();
    }

    // to check if the user are login or logout
    public String getLogin(){
        return login;
    }
    private void handleLogin(OutputStream outputStream, String[] tokens)throws IOException{
        if (tokens.length==3){      //check of a login command
            String login = tokens[1];      // login <username> <password>
            String password = tokens[2];
            // create 2 users (test1)(test2)
            if(login.equals( "test1" )&& password.equals( "test1" ) ||
            login.equals( "test2" )&& password.equals( "test2" )){
                String msg ="ok login \n";
                outputStream.write( msg.getBytes() );
                this.login = login;
                System.out.println( "User logged in succesfully: "+login );

                String onlineMsg = "online "+ login+"\n";
                // in this list are all the online user
                List<ServerWorker> workerList = server.getWorkerList();

                for(ServerWorker worker: workerList){ // send current user other online users
                    if(!login.equals( worker.getLogin() )) { // don't send me my status
                        if(worker.getLogin()!= null) {         //connect without login
                            String msg2 = "online " + worker.getLogin() + " \n";
                            worker.send( msg2 );
                        }
                    }
                }


                for(ServerWorker worker: workerList){ // send other online users current user's status
                    if(!login.equals( worker.getLogin() )) { // don't send me my status
                        worker.send( onlineMsg );
                        System.err.println( "Login failed for "+ login );
                    }
                }

            }else {
                String msg = "Username or Password is wrong\n";
                outputStream.write( msg.getBytes() );
            }
        }
    }

    private void send(String msg)throws IOException {
        if (login != null) {    //connect without login
            outputStream.write( msg.getBytes() );
        }
    }
}
