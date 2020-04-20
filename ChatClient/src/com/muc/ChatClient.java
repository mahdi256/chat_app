package com.muc;


import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


class ChatClient{
    private String serverName;
    private static int serverPort;
    private Socket socket = null;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedReader bufferedIn;
    private PrintWriter clientOut;
    private String username;
    private static GUI userInterface;
    private String activeChat;

    // Konstruktor, dem der Server-Name und der Server-Port übergeben wird
    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    //Main-Methode zum Chat-Client zu starten
    public static void main(String[] args) throws IOException {
        Random rand = new Random();
        serverPort = rand.nextInt((9999 - 9998) + 1) + 9998; //Es wird zufällig zwischen dem Server an Port 9998 und Port 9999 gewählt
        System.out.println(serverPort); // Port des Servers mit dem kommuniziert wird, wird ausgegeben
        ChatClient client = new ChatClient("localhost", serverPort); //Objekt von ChatClient wird erstellt mit Server Port und Server Adresse

        userInterface = new GUI(client);
        userInterface.login();

    }

    //Methode erstellt einen Thread für das ständige empfangen von Nachrichten vom Server
    private void startMessageReader() {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                readMessageLoop();
            }
        };
        t1.start();

    }

    // Methode, die vom GUI aufgerufen wird, wenn das Login-Fenster geschlossen wird
    public void loginClosed(){
        if(!(socket == null)){
            System.out.println("loginClosed() aufgerufen, Logoff-Nachricht wird an Server gesendet");
            clientOut.println("logoff");
            clientOut.flush();
        }
        System.exit(0);
    }

    //Methode, die den Befehl "logoff" an den Server sendet
    public void logOff(){
        System.out.println("logoff() aufgerufen, Logoff-Nachricht wird an Server gesendet");
        clientOut.println("logoff");
        clientOut.flush();
    }

    public void setServerPort(int port){
        this.serverPort = port;
    }

    public int getServerPort(){
        return this.serverPort;
    }

    //Methode wird vom GUI aufgerufen wenn ein Chat geöffnet wird und sendet Nachricht mit Befehl an Server
    public void openChat(String chatName){
        clientOut.println("openChat " + chatName);
        clientOut.flush();
        activeChat = chatName;
    }

    //Methode wird vom GUI aufgerufen wenn ein neuer Chat erstellt wird und sendet Nachricht mit Befehl an Server
    public void createChat(String allOtherUsers){
        if(!allOtherUsers.contains("#" + username + "#")) { //prüft ob Nutzer, zu dem der Client gehört, in Nutzerliste vorhanden ist
            String participants = allOtherUsers + username + "#"; // zu den übergebenen anderen usern fügen wir uns selbst hinzu
            participants = "createChat " + participants;
            clientOut.println(participants);
            clientOut.flush();
        } else {
            JOptionPane.showMessageDialog(userInterface.loginFrame,
                    "Es ist nicht möglich einen Chat mit sich selbst zu erstellen! Bitte versuchen Sie es erneut und geben Sie ihren Nutzernamen nicht in das Feld ein.",
                    "Fehler",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    //Methode, die auf Nachrichten vom Server wartet und diese entsprechend verarbeitet
    private void readMessageLoop() {
        try {
            String clientIn;
            while (true) {
                if((clientIn = bufferedIn.readLine()) != null ) { //Empfangene Nachricht wird in ClientIn geschrieben
                    System.out.println("Vom Client empfangene Nachricht: " + clientIn);
                    String[] tokens = clientIn.split(" ", 2);
                    if (tokens != null && tokens.length > 0) {
                        String msg = tokens[0];
                        if("chatList".equalsIgnoreCase(msg)) {
                            handleChatList(tokens);
                        } else if("chat".equalsIgnoreCase(msg)){
                            String[] tokens2 = clientIn.split( " ", 3);
                            handleChat(tokens2);
                        } else if("logoff".equalsIgnoreCase(msg)){
                            try {
                                socket.close();
                                System.out.println("Socket wurde geschlossen");
                                System.exit(0);
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(userInterface.loginFrame,
                    "Derzeit ist keine Verbindung zum Server möglich! Sie werden nun ausgeloggt.",
                    "Fehler",
                    JOptionPane.WARNING_MESSAGE);
            userInterface.backToLogin();
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Methode, die eine eingegangene Chat-Liste umformt und ans GUI weiterleitet
    private void handleChatList(String[] tokens){
        String[] chatList = tokens[1].split(" ");
        userInterface.interfaceReceiveNewUserList(chatList);
    }

    //Methode, die einen eingegangenen Chat umfort und ans GUI weiterleitet
    private void handleChat(String[] tokens){
        String chatname = tokens[1];
        String[] messages = tokens[2].split("#!!!#");
        String[][] chatHistory = new String[messages.length][3];
        String[] parameters;
        for(int i = 0;i<messages.length; i++){
            parameters = messages[i].split("#!#%#");
            for(int j=0; j<3;j++){
                chatHistory[i][j] = parameters[j];
            }
        }
        if(activeChat != null){
            if(activeChat.equalsIgnoreCase(chatname)) { // es wird geprüft ob der eingegangene Chat in der GUI gerade geöffnet ist
                userInterface.interfaceReceiveMessage(chatHistory);
            }
        }

    }

    //Methode erstellt einen Socket, einen BufferedReader (inFromUser) und einen PrintWriter (clientOut)
    public boolean connect() {
        try {
            this.socket = new Socket(serverName, serverPort);
            System.out.println("Der Socket läuft auf dem Port: " + socket.getLocalPort());
            this.outputStream = socket.getOutputStream();
            this.inputStream = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(inputStream));
            clientOut = new PrintWriter(new OutputStreamWriter(outputStream));
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(userInterface.loginFrame,
                    "Derzeit ist keine Verbindung zum Server möglich! Der Socket am Port " + serverPort +" scheint nicht erreichbar zu sein.",
                    "Fehler",
                    JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    //Methode die vom GUI aufgerufen wird und eine Chat-Nachricht an den Server versendet
    public void sendMessage(String Message) throws IOException {
        if(activeChat == null) return; //es ist nicht möglich eine Nachricht zu versenden, wenn kein Chat ausgewählt wurde
           String ChatName = activeChat;

        Date date = new Date();
        String time = date+"";
        time = time.split(" ")[3];
        String cmd = "message " + ChatName + " " + username + " " + time + " " + Message ; //Command wird erstellt aus Empfänger und Nachricht
        clientOut.println(cmd);
        clientOut.flush();
    }

    //Diese Methode wird vom GUI aufgerufen und sendet die vom Nutzer eingegebene Login-Daten an den Server
    public String[] login(String username, String password, String entryMethod) throws IOException {
        String msg = "login " + username + " " + password + " " + entryMethod;
        clientOut.println(msg);
        clientOut.flush();
        String response = bufferedIn.readLine();
        if ("login successful".equalsIgnoreCase(response)) {
            this.username = username;
            clientOut.println("chatList");
            clientOut.flush();
            String chatlist = bufferedIn.readLine();
            String[] components = chatlist.split(" ", 2);
            String[] chatListArray = components[1].split(" ");
            startMessageReader();
            return chatListArray;
        } else if("login failed".equalsIgnoreCase(response)) {
            return null;
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
    }

}