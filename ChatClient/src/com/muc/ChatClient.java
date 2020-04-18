package com.muc;


import javax.swing.tree.ExpandVetoException;
import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


class ChatClient{
    private String serverName;
    private static int serverPort;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedReader bufferedIn;
    private BufferedReader inFromUser;
    private PrintWriter clientOut;
    private String username;
    private String password;
    private static GUI userInterface;

    private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>(); // ArrayList, die Objekte von Klassen enth�lt, die das Interface UserStatusListener implementiert haben
    private ArrayList<MessageListener> messageListeners = new ArrayList<>(); // ArrayList, die Objekte von Klassen enth�lt, die das Interface MessageListener implementiert haben

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException {  //main wird beim start des client ausgef�hrt
        Random rand = new Random();
        serverPort = rand.nextInt((9999 - 9998) + 1) + 9998;
        System.out.println(serverPort); // Port des Servers mit dem kommuniziert wird, wird ausgegeben
        ChatClient client = new ChatClient("localhost", serverPort); //Objekt von ChatClient wird erstellt mit Server Port und Server Adresse

        userInterface = new GUI(client);
        userInterface.login();

        /*if (!client.connect()) { //connect-Methode wird aufgerufen und Socket wird erstellt
            System.err.println("Connect failed.");
        } else {
            System.out.println("Connect successful");
        }*/
// rufe UI (login) auf!
// Aufruf der Login-Methode wird in Action Listener der GUI verschoben
//            if (client.login()) {
//                System.out.println("Login successful");
//
//                //client.msg("test2", "Hello World!");
//            } else {
//                System.err.println("Login failed");
//            }
//
//            //client.logoff();

    }

    //ToDo startMessageReader(Chat[])
    private void startMessageReader() { //Methode erstellt zwei Threads f�r das senden und empfangen von Nachrichten
        Thread t1 = new Thread() {
            @Override
            public void run() {
                readMessageLoop();
            }
        };
//        Thread t2 = new Thread() {
//            @Override
//            public void run() {
//                sendMessageLoop();
//            }
//        };
        t1.start();
//        t2.start();
// hier wird GUI Chatseite aufgerufen!
// neue Methode: ladeCHats() aufrufen
    }

    public void openChat(String chatName){

    }

    private void readMessageLoop() { //Methode die auf Nachrichten vom Server wartet und diese entsprechend verarbeitet
        try {
            String clientIn;

            while (true) { //endlosschleife
                if((clientIn = bufferedIn.readLine()) != null ) { //Empfangene Nachricht wird in ClientIn geschrieben
                    System.out.println(clientIn); //Ausgabe nur zum kontrolle! User sollte das Commando nicht sehen
                    String[] tokens = clientIn.split(" "); //Commando wird Teile geteilt
                    if (tokens != null && tokens.length > 0) {
                        String msg = tokens[0]; //cmd = Erster Teil des Commandos
                        if("chatList".equalsIgnoreCase(msg)){
                            handleChatList(tokens);
                        }
                        if("chat".equalsIgnoreCase(msg)){
                            handleChat(tokens);
                        }
                        /*if ("online".equalsIgnoreCase(cmd)) { //pr�ft ob cmd gleich "online", Gro� und Kleinschreibung wird ignoriert
                            handleOnline(tokens); //geteilter Befehl wird als Array mitgegeben
                        } else if ("offline".equalsIgnoreCase(cmd)) {
                            handleOffline(tokens);
                        } else if ("msg".equalsIgnoreCase(cmd)) { //pr�ft ob cmd gleich "msg", Gro� und Kleinschreibung wird ignoriert
                            String[] tokensMsg = clientIn.split(" ", 3); //die eingegangene Nachricht clientIn wird neu geteilt (diesmal nur in drei Teile, damit nicht bei Leerzeichen in der Nachricht geteilt wird)
                            handleMessage(tokensMsg);
                        }*/
                        //else if wenn Kommando vom Server in dem der vorher angefragte CHatverlauf steht
                        //dann wird neue Methode, die unten beschrieben ist, ausgef�hrt

                        //else if wenn Kommando vom Server kommt in dem alle Chats des user gesndet werden
                        //dann wird neue Methode ladeAlleChats() aufgerufen
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
        String[] chatList = Arrays.copyOfRange(tokens, 1, tokens.length-1);
        userInterface.interfaceReceiveNewUserList(chatList);
    }

    private void handleChat(String[] tokens){
        String[] messages = tokens[1].split("#!!!#");
        String[][] chatHistory = new String[messages.length][3];
        for(int i = 0;i<messages.length; i++){
            for(int j=0; j<3;j++){
                String[] parameters = messages[i].split("#!#%#");
                chatHistory[i][j] = parameters[j];
            }
        }
    }

    public boolean connect() { //Methode erstellt einen Socket, einen BufferedReader (inFromUser) und einen PrintWriter (clientOut)
        try {
            this.socket = new Socket(serverName, serverPort); //erstellt einen Socket zum Server
            System.out.println("Client port is " + socket.getLocalPort()); // Ausgabe auf welchem Port der Socket beim Client l�uft
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


    /* optionale Methoden online/offline */
    //     // folgender Code ist optional und implementiert zwei Methoden um auszugeben wenn client offline oder online
//        client.addUserStatusListener(new UserStatusListener() { //f�gt das Objekt in dem das Interface UserStatusListener implementiert wurde, in die ArrayList UserStatusListener ein
//            @Override
//            public void online(String login) {
//                System.out.println("ONLINE: " + login); //gibt Online und den username des clients aus, der online gegangen ist
//            }
//
//            @Override
//            public void offline(String login) {
//                System.out.println("OFFLINE: " + login); //gibt offline und den username des clients aus, der offline gegangen ist
//            }
//        });
//     // optionaler Code Ende

    // Interface MessageListener wird implementiert; onMessage gibt eine Nachricht und deren Absender aus, die der Methode �bergegeben werden
//        client.addMessageListener(new MessageListener() { //f�gt das Objekt in dem das Interface MessageListener implementiert wurde, in die ArrayList MessageListener ein
//            @Override
//            public void onMessage(String fromLogin, String msgBody) {
//                System.out.println("Nachricht von " + fromLogin + " : " + msgBody); //gibt eine Nachricht aus und von wem diese Nachrichst stammt
//            }
//        });

    // Msg-Methode, wird bei Nutzung eines UIs unn�tig
     /*  public void msg(String sendTo, String msgBody) throws IOException { //Methode die eine Nachricht an den Server versendet
        String cmd = "msg " + sendTo + " " + msgBody + "\n"; //Command wird erstellt aus Empf�nger und Nachricht
// erg�nzen, dass nachricht auch mehrere empf�nger haben kann (Loop)
        clientOut.print(cmd);
        clientOut.flush(); // Command wird an Server gesendet
    }*/

    // boolean Login
    public boolean login(String username, String password, String entryMethod) throws IOException { //ben�tigt Parameter: username und passwort
        String msg = "login " + username + " " + password + " " + entryMethod + "\n"; //Commando zum login wird aus username und passwort zusammengesetzt
        clientOut.print(msg);
        clientOut.flush(); //Commando wird an Server gesendet
        String response = bufferedIn.readLine(); // Client wartet auf Nachricht vom Server
        System.out.println("Response Line:" + response);
// Antwort des Servers: "ok login Chat1 Chat2 Chat3 ChatN"
// Speichere alle Chats ab
// rufe startMessageReader(Chat[])
        if ("login successful".equalsIgnoreCase(response)) { //Server sendet "ok login", wenn erfoglreich eingeloggt
//          startMessageReader(); //Anzeige der Chat-Startseite
            clientOut.print("chatList");
            clientOut.flush();
            return true;
        } else if("login failed".equalsIgnoreCase(response)) {
            return false; //wenn Server nicht "ok login" sendet, endet login() mit return false -> Login ist gescheitert
        }else if("registration successful".equalsIgnoreCase(response)){
            clientOut.print("chatList");
            clientOut.flush();
            return true;
        }else if("registration failed".equalsIgnoreCase(response)){
            return false;
        }
        return false;
        // Einf�gen, dass Nachricht ausgegeben wird, wenn sich user neu registriert hat
    }

    // logoff, durch UI useless
      /* public void logoff() throws IOException { //Methode sendet Befehl "logoff/n" an den Server
       String cmd = "logoff\n";
        serverOut.write(cmd.getBytes()); //Nachricht wird an Server gesendet (flush wird nicht gebraucht, da \n)
       //eventuell Methoden removeUserStatusListener und removeMessageListener aufrufen um logoff zu versollst�ndigen
    }*/

    // sendMessageLoop
    //    private void sendMessageLoop(){ //Methode, die
//        try {
//            String userIn;
//            while (true) { //Endlosschleife
//                if ((userIn = inFromUser.readLine()) != null){ //Wenn Benutzer eine Nachricht in die Konsole eingibt, die nicht leer ist
//                    this.msg("test2", userIn); //this = dieser client; msg wird ausgef�hrt und empf�nger und Nachricht wereden mitgegeben //ToDo
//                    System.out.println(userIn); // Nachticht, die gesendet wird, wird auch beim Sender ausgegeben
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            try {
//                socket.close();
//            } catch (IOException ioe) {
//                ioe.printStackTrace();
//            }
//        }
//    }

    // handleMessage
       /* private void handleMessage(String[] tokensMsg) {
        String login = tokensMsg[1]; //login = username
        String msgBody = tokensMsg[2]; //msgBody = empfangene Text-Nachricht
        // pr�fen ob Chat bereits ge�ffnet
        // wenn ja: Chat aktualisieren
        // wenn nein: nix tun
        //
        //        for(MessageListener listener : messageListeners) {
        //            listener.onMessage(login, msgBody); //f�hrt bei jedem Objekt der ArrayList messageListeners die Methode onMessage aus und gibt den username und die Nachricht mit
        //        }
    }*/

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
            listener.online(login); // f�hrt bei jedem Objekt der ArrayList userStatusListeners die Methode online aus und gibt den username mit
        }
    }*/

    // Listener
         /*public void addUserStatusListener(UserStatusListener listener) {
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
    }*/
}