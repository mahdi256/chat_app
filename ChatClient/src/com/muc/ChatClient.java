package com.muc;


import javax.swing.tree.ExpandVetoException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;


public class ChatClient{
    private String serverName;
    private static int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;
    private BufferedReader inFromUser;
    private PrintWriter clientOut;
    private String username;
    private String password;

    private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();
    private ArrayList<MessageListener> messageListeners = new ArrayList<>();

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException {
        Random rand = new Random();
        serverPort = rand.nextInt((9999 - 9998) + 1) + 9998;
        System.out.println(serverPort);
        ChatClient client = new ChatClient("localhost", serverPort);
        client.addUserStatusListener(new UserStatusListener() {
            @Override
            public void online(String login) {
                System.out.println("ONLINE: " + login);
            }

            @Override
            public void offline(String login) {
                System.out.println("OFFLINE: " + login);
            }
        });

        client.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(String fromLogin, String msgBody) {
                System.out.println("Nachricht von " + fromLogin + " : " + msgBody);
            }
        });

        if (!client.connect()) {
            System.err.println("Connect failed.");
        } else {
            System.out.println("Connect successful");

            if (client.login()) {
                System.out.println("Login successful");

                //client.msg("test2", "Hello World!");
            } else {
                System.err.println("Login failed");
            }

            //client.logoff();
        }
    }

    public void msg(String sendTo, String msgBody) throws IOException {
        String cmd = "msg " + sendTo + " " + msgBody + "\n";
        clientOut.print(cmd);
        clientOut.flush();
    }

    public boolean login() throws IOException {
        System.out.println("Username:");
        username = inFromUser.readLine();;
        System.out.println("Passwort:");
        password = inFromUser.readLine();
        String cmd = "login " + username + " " + password + "\n";
        clientOut.print(cmd);
        clientOut.flush();

        String response = bufferedIn.readLine();
        System.out.println("Response Line:" + response);

        if ("ok login".equalsIgnoreCase(response)) {
            startMessageReader();
            return true;
        } else {
            return false;
        }
    }

    public void logoff() throws IOException {
        String cmd = "logoff\n";
        serverOut.write(cmd.getBytes());
    }

    private void startMessageReader() {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                readMessageLoop();
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                sendMessageLoop();
            }
        };
        t1.start();
        t2.start();
    }

    private void readMessageLoop() {
        try {
            String clientIn;

            while (true) {
                if((clientIn = bufferedIn.readLine()) != null ) {
                    System.out.println(clientIn);
                    String[] tokens = clientIn.split(" ");
                    if (tokens != null && tokens.length > 0) {
                        String cmd = tokens[0];
                        if ("online".equalsIgnoreCase(cmd)) {
                            handleOnline(tokens);
                        } else if ("offline".equalsIgnoreCase(cmd)) {
                            handleOffline(tokens);
                        } else if ("msg".equalsIgnoreCase(cmd)) {
                            String[] tokensMsg = clientIn.split(" ", 3);
                            handleMessage(tokensMsg);
                        }
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessageLoop(){
        try {
            String userIn;
            while (true) {
                if ((userIn = inFromUser.readLine()) != null){
                    this.msg("test2", userIn);
                    System.out.println(userIn);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private void handleMessage(String[] tokensMsg) {
        String login = tokensMsg[1];
        String msgBody = tokensMsg[2];

        for(MessageListener listener : messageListeners) {
            listener.onMessage(login, msgBody);
        }
    }

    private void handleOffline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners) {
            listener.offline(login);
        }
    }

    private void handleOnline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners) {
            listener.online(login);
        }
    }

    public boolean connect() {
        try {
            this.socket = new Socket(serverName, serverPort);
            System.out.println("Client port is " + socket.getLocalPort());
            this.serverOut = socket.getOutputStream();
            this.serverIn = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            clientOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUserStatusListener(UserStatusListener listener) {
        userStatusListeners.add(listener);
    }

    public void removeUserStatusListener(UserStatusListener listener) {
        userStatusListeners.remove(listener);
    }

    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }


}
