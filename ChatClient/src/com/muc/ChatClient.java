package com.muc;


import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


class ChatClient{
    private String serverName;
    private static int serverPort;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedReader bufferedIn;
    private PrintWriter clientOut;
    private String username;
    private String password;
    private static GUI userInterface;
    private String activeChat;

    private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>(); // ArrayList, die Objekte von Klassen enthält, die das Interface UserStatusListener implementiert haben
    private ArrayList<MessageListener> messageListeners = new ArrayList<>(); // ArrayList, die Objekte von Klassen enthält, die das Interface MessageListener implementiert haben

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException {  //main wird beim start des client ausgeführt
        Random rand = new Random();
        serverPort = rand.nextInt((9999 - 9998) + 1) + 9998;
        System.out.println(serverPort); // Port des Servers mit dem kommuniziert wird, wird ausgegeben
        ChatClient client = new ChatClient("localhost", serverPort); //Objekt von ChatClient wird erstellt mit Server Port und Server Adresse

        userInterface = new GUI(client);
        userInterface.login();

    }

    //ToDo startMessageReader(Chat[])
    private void startMessageReader() { //Methode erstellt zwei Threads für das senden und empfangen von Nachrichten
        Thread t1 = new Thread() {
            @Override
            public void run() {
                readMessageLoop();
            }
        };

        t1.start();

    }

    public void logOff(){
        clientOut.println("logOff");
        clientOut.flush();
    }

    public void setServerPort(int port){
        this.serverPort = port;
    }

    public int getServerPort(){
        return this.serverPort;
    }

    public void openChat(String chatName){
        clientOut.println("openChat " + chatName);
        clientOut.flush();
        activeChat = chatName;
    }

    public void createChat(String allOtherUsers){
        String participants = allOtherUsers + username + "#"; // zu den übergebenen anderen usern fügen wir uns selbst hinzu
        participants = "createChat " + participants;
        System.out.println(participants); // participants format: #username#username#
        clientOut.println(participants);
        clientOut.flush();
    }

    private void readMessageLoop() { //Methode die auf Nachrichten vom Server wartet und diese entsprechend verarbeitet
        try {
            String clientIn;

            while (true) { //endlosschleife
                System.out.println("Client wartet auf eingehende Commands");
                if((clientIn = bufferedIn.readLine()) != null ) { //Empfangene Nachricht wird in ClientIn geschrieben
                    System.out.println(clientIn); //Ausgabe nur zum kontrolle! User sollte das Commando nicht sehen
                    String[] tokens = clientIn.split(" ", 2); //Commando wird Teile geteilt
                    if (tokens != null && tokens.length > 0) {
                        String msg = tokens[0]; //cmd = Erster Teil des Commandos
                        if("chatList".equalsIgnoreCase(msg)) {
                            handleChatList(tokens);
                        } else if("chat".equalsIgnoreCase(msg)){
                            String[] tokens2 = clientIn.split( " ", 3);
                            handleChat(tokens2);
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

    private void handleChatList(String[] tokens){
        String[] chatList = tokens[1].split(" ");
        userInterface.interfaceReceiveNewUserList(chatList);
    }

    private void handleChat(String[] tokens){
        String chatname = tokens[1];
        String[] messages = tokens[2].split("#!!!#");
        String[][] chatHistory = new String[messages.length][3];
       String[] parameters;
        System.out.println("Tokens[2]:" + tokens[2]);
        System.out.println("Message[0]: "+messages[0]);
        for(int i = 0;i<messages.length; i++){
            parameters = messages[i].split("#!#%#");
            for(int x= 0; x < parameters.length;x++){
                System.out.println(parameters[x]);
            }
            for(int j=0; j<3;j++){
                chatHistory[i][j] = parameters[j];
            }
        }
        if(activeChat==null||activeChat.equalsIgnoreCase(chatname)) {
            userInterface.interfaceReceiveMessage(chatHistory);
        }

    }

    public boolean connect() { //Methode erstellt einen Socket, einen BufferedReader (inFromUser) und einen PrintWriter (clientOut)
        try {
            this.socket = new Socket(serverName, serverPort); //erstellt einen Socket zum Server
            System.out.println("Client port is " + socket.getLocalPort()); // Ausgabe auf welchem Port der Socket beim Client läuft
            this.outputStream = socket.getOutputStream();
            this.inputStream = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(inputStream));
            clientOut = new PrintWriter(new OutputStreamWriter(outputStream));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

        //"me", "14.04.2020 - 20:10", "erste Nachricht &#129409;"
    public void sendMessage(String Message) throws IOException { //Methode die eine Nachricht an den Server versendet
        if(activeChat == null) return; //es ist nicht möglich eine Nachricht zu versenden, wenn kein Chat ausgewählt wurde
           String ChatName = activeChat;
           System.out.println("->im Client verarbeitete Message: " + Message);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(cal.getTime());

        String cmd = "message " + ChatName + " " + username + " " + time + " " + Message ; //Command wird erstellt aus Empfänger und Nachricht
           System.out.println("gesendeter Massage Command: " + cmd);
        clientOut.println(cmd);
        clientOut.flush(); // Command wird an Server gesendet
    }

    // boolean Login
    public String[] login(String username, String password, String entryMethod) throws IOException { //benötigt Parameter: username und passwort
        String msg = "login " + username + " " + password + " " + entryMethod; //Commando zum login wird aus username und passwort zusammengesetzt
        clientOut.println(msg);
        clientOut.flush(); //Commando wird an Server gesendet
        String response = bufferedIn.readLine(); // Client wartet auf Nachricht vom Server
        System.out.println("Response Line:" + response);
        if ("login successful".equalsIgnoreCase(response)) { //Server sendet "ok login", wenn erfoglreich eingeloggt
            this.username = username;
            clientOut.println("chatList");
            clientOut.flush();
            String chatlist = bufferedIn.readLine();
            String[] components = chatlist.split(" ", 2);
            String[] chatListArray = components[1].split(" ");
            startMessageReader();
            return chatListArray;
        } else if("login failed".equalsIgnoreCase(response)) {
            return null; //wenn Server nicht "ok login" sendet, endet login() mit return false -> Login ist gescheitert
        }else if("registration successful".equalsIgnoreCase(response)){
            this.username = username;
            clientOut.println("chatList");
            clientOut.flush();
            String chatlist = bufferedIn.readLine();
            String[] components = chatlist.split(" ", 2);
            String[] chatListArray = components[1].split(" ");
            startMessageReader();
            return chatListArray;
        }else if("registration failed".equalsIgnoreCase(response)){
            return null;
        }
        return null;
        // Einfügen, dass Nachricht ausgegeben wird, wenn sich user neu registriert hat
    }

    // handleOffline
    /*private void handleOffline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners) {
            listener.offline(login);
        }
    }*/

    // handleOnline
     /* private void handleOnline(String[] tokens) {
        String login = tokens[1]; //login ist username
        for(UserStatusListener listener : userStatusListeners) {
            listener.online(login); // führt bei jedem Objekt der ArrayList userStatusListeners die Methode online aus und gibt den username mit
        }
    }*/

    // Listener
    /*
    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }
    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }
    */
}