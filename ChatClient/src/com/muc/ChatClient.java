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
        
     // folgender Code ist optional und implementiert zwei Methoden um auszugeben wenn client offline oder online
        client.addUserStatusListener(new UserStatusListener() { //fügt das Objekt in dem das Interface UserStatusListener implementiert wurde, in die ArrayList UserStatusListener ein
            @Override
            public void online(String login) {
                System.out.println("ONLINE: " + login); //gibt Online und den username des clients aus, der online gegangen ist
            }

            @Override
            public void offline(String login) {
                System.out.println("OFFLINE: " + login); //gibt offline und den username des clients aus, der offline gegangen ist
            }
        });
     // optionaler Code Ende

     // Interface MessageListener wird implementiert; onMessage gibt eine Nachricht und deren Absender aus, die der Methode übergegeben werden
        client.addMessageListener(new MessageListener() { //fügt das Objekt in dem das Interface MessageListener implementiert wurde, in die ArrayList MessageListener ein
            @Override
            public void onMessage(String fromLogin, String msgBody) {
                System.out.println("Nachricht von " + fromLogin + " : " + msgBody); //gibt eine Nachricht aus und von wem diese Nachrichst stammt
            }
        });

     
        if (!client.connect()) { //connect-Methode wird aufgerufen und Socket wird erstellt
            System.err.println("Connect failed.");
        } else {
            System.out.println("Connect successful");

            if (client.login()) { //
                System.out.println("Login successful");

                //client.msg("test2", "Hello World!");
            } else {
                System.err.println("Login failed");
            }

            //client.logoff();
        }
    }

    public void msg(String sendTo, String msgBody) throws IOException { //Methode die eine Nachricht an den Server versendet
        String cmd = "msg " + sendTo + " " + msgBody + "\n"; //Command wird erstellt aus Empfänger und Nachricht
        clientOut.print(cmd);
        clientOut.flush(); // Command wird an Server gesendet
    }

    public boolean login() throws IOException { //Methode 
        System.out.println("Username:");
        username = inFromUser.readLine();; // Der vom User eigegebene Username wird eingelesen
        System.out.println("Passwort:");
        password = inFromUser.readLine(); // Das vom User eigegebene Passwort wird eingelesen
        String cmd = "login " + username + " " + password + "\n"; //Commando zum login wird aus username und passwort zusammengesetzt
        clientOut.print(cmd);
        clientOut.flush(); //Commando wird an Server gesendet

        String response = bufferedIn.readLine(); // Client wartet auf Nachricht vom Server
        System.out.println("Response Line:" + response);

        if ("ok login".equalsIgnoreCase(response)) { //Server sendet "ok login", wenn erfoglreich eingeloggt
            startMessageReader();
            return true;
        } else {
            return false; //wenn Server nicht "ok login" sendet, endet login() mit return false -> Login ist gescheitert
        }
    }

    public void logoff() throws IOException { //Methode sendet Befehl "logoff/n" an den Server
       String cmd = "logoff\n";
        serverOut.write(cmd.getBytes()); //Nachricht wird an Server gesendet (flush wird nicht gebraucht, da \n)
       //eventuell Methoden removeUserStatusListener und removeMessageListener aufrufen um logoff zu versollständigen
    }

    private void startMessageReader() { //Methode erstellt zwei Threads für das senden und empfangen von Nachrichten
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

    private void readMessageLoop() { //Methode die auf Nachrichten vom Server wartet und diese entsprechend verarbeitet
        try {
            String clientIn;

            while (true) { //endlosschleife
                if((clientIn = bufferedIn.readLine()) != null ) { //Empfangene Nachricht wird in ClientIn geschrieben
                    System.out.println(clientIn); //Ausgabe nur zum kontrolle! User sollte das Commando nicht sehen
                    String[] tokens = clientIn.split(" "); //Commando wird Teile geteilt
                    if (tokens != null && tokens.length > 0) {
                        String cmd = tokens[0]; //cmd = Erster Teil des Commandos                      
                        if ("online".equalsIgnoreCase(cmd)) { //prüft ob cmd gleich "online", Groß und Kleinschreibung wird ignoriert
                            handleOnline(tokens); //geteilter Befehl wird als Array mitgegeben
                        } else if ("offline".equalsIgnoreCase(cmd)) {
                            handleOffline(tokens);
                        } else if ("msg".equalsIgnoreCase(cmd)) { //prüft ob cmd gleich "msg", Groß und Kleinschreibung wird ignoriert
                            String[] tokensMsg = clientIn.split(" ", 3); //die eingegangene Nachricht clientIn wird neu geteilt (diesmal nur in drei Teile, damit nicht bei Leerzeichen in der Nachricht geteilt wird)
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

    private void sendMessageLoop(){ //Methode, die 
        try {
            String userIn;
            while (true) { //Endlosschleife
                if ((userIn = inFromUser.readLine()) != null){ //Wenn Benutzer eine Nachricht in die Konsole eingibt, die nicht leer ist
                    this.msg("test2", userIn); //this = dieser client; msg wird ausgeführt und empfänger und Nachricht wereden mitgegeben //ToDo
                    System.out.println(userIn); // Nachticht, die gesendet wird, wird auch beim Sender ausgegeben
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
        String login = tokensMsg[1]; //login = username
        String msgBody = tokensMsg[2]; //msgBody = empfangene Text-Nachricht

        for(MessageListener listener : messageListeners) { 
            listener.onMessage(login, msgBody); //führt bei jedem Objekt der ArrayList messageListeners die Methode onMessage aus und gibt den username und die Nachricht mit
        }
    }

    private void handleOffline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners) {
            listener.offline(login);
        }
    }

    private void handleOnline(String[] tokens) { 
        String login = tokens[1]; //login ist username
        for(UserStatusListener listener : userStatusListeners) {
            listener.online(login); // führt bei jedem Objekt der ArrayList userStatusListeners die Methode online aus und gibt den username mit
        }
    }

    public boolean connect() { //Methode erstellt einen Socket, einen BufferedReader (inFromUser) und einen PrintWriter (clientOut)
        try {
            this.socket = new Socket(serverName, serverPort); //erstellt einen Socket zum Server
            System.out.println("Client port is " + socket.getLocalPort()); // Ausgabe auf welchem Port der Socket beim Client läuft
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
