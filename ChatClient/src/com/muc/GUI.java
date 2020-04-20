package com.muc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

// Allgemeines:
// Mit "interface" beginnende Methoden dienen als Schnittstelle zwischen dem UI und dem Chat-Client und haben den Zweck, die Entwicklung übersichtlicher und einfacher zu gestalten.
// Es soll dadurch vermieden werden, dass Chat-Client-Methoden direkt in GUI-Methoden, in denen UI-Komponenten erstellt werden, aufgerufen werden.

public class GUI {

    String      title     = "Messanger";
    JFrame      messangerFrame    = new JFrame(title);
    JTextField  usernameField;
    JTextField  passwordField;
    JFrame      loginFrame;
    JPanel messageAndTypePanel = new JPanel();
    JPanel messagePanel = new JPanel();
    String[][] chatHistory = {{"","","Please open or create a chat"}};
    String username;
    ChatClient client;
    JPanel lastChatsPanel = new JPanel();
    JLabel currentChatBar = new JLabel();
    int numberOfMessages;

    public GUI(ChatClient client){
        this.client = client;
    }


    String[] lastChats = {};


    public void backToLogin(){
        messangerFrame.dispose();
        login();
    }


    public void interfaceLoginClosed(){
        client.loginClosed();
    }

    public void interfaceLogoff(){
        client.logOff();
    }

    // es werden die eingegebenen Login-Daten überprüft
    public String[] interfaceLogin(String[] loginData){
        boolean correctLogin = true;
        if(loginData[0].contains(" ") || loginData[0].contains("#") || loginData[0].isEmpty() || loginData[0] == null || loginData[1].contains(" ") || loginData[1].contains("#") || loginData[1].isEmpty() || loginData[1] == null){
            correctLogin = false;
        }
        if(correctLogin) {
            if(client.connect()){
                try {
                    String[] chatlist = client.login(loginData[0], loginData[1], loginData[2]);
                    return chatlist;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }else{
                int serverPort = client.getServerPort();
                if(serverPort==9999){
                    serverPort=9998;
                }else {
                    serverPort=9999;
                }
                client.setServerPort(serverPort);
                if(client.connect()) {
                    try {
                        String[] chatlist = client.login(loginData[0], loginData[1], loginData[2]);
                        return chatlist;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            System.out.println("Login Daten falsch");
        }
        return null;
    }

    // Wird beim Klick auf den "+"-Button aufgerufen und erstellt einen neuen Chat mit dem/den übergebenen User(n)
    public void interfaceAddUser(String user){
        user = "#" + user.replace(" ","#") + "#";
        client.createChat(user);
    }

    // wird vom Chat-Client aufgerufen, wenn der User einen neuen Chat gestartet hat oder ein anderer User einen Chat mit dem User gestartet hat, um dem UI eine neue User-List (= Chat-List) zu übergeben. Die linke Seite des UIs wird daraufhin aktualisiert
    public void interfaceReceiveNewUserList(String[] userList){
        for(int i=0; i<userList.length;i++){
            userList[i] = userList[i].replace("#", " ");
        }
        updateUserList(userList);
    }

    // wird aufgerufen, wenn der User links auf einen Chat klickt
    public void interfaceSendClickedChat(String clickedChat){
        currentChatBar.setText(clickedChat);
        System.out.println("participants werden von GUI an openchat gegeben als : "+clickedChat.replace(" ", "#"));
        client.openChat(clickedChat.replace(" ", "#"));

    }

    public void interfaceUIReceiveNewChatHistory(String[] newChatHistory){
    }

    // Wird durch den Klick auf den Senden-Button aufgerufen und gibt die Nachricht weiter oder zeigt eine Fehlermeldung bei leeren Nachrichten
    public void interfaceSendMessage(String sentMessage){
        System.out.println(sentMessage);

        try {
            if(sentMessage.isEmpty() || sentMessage == null || sentMessage == ""){
                JOptionPane.showMessageDialog(messangerFrame,
                        "Es dürfen keine leeren Nachrichten versendet werden!",
                        "Fehler",
                        JOptionPane.WARNING_MESSAGE);

            } else {
                client.sendMessage(sentMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // wird vom Chat-Client aufgerufen wenn dieser eine neue Nachricht vom Server oder vom UI erhält, um den Chatverlauf zu aktualisieren 
    public void interfaceReceiveMessage(String[][] receivedMessage){
        updateChatHistory(receivedMessage);
    }

    // lädt die Parent-UI-Komponente, die links die vorhandenen Chats anzeigt neu 
    public void updateUserList(String[] lastChats) {
        lastChatsPanel.removeAll();
        lastChatsPanel.setLayout(new GridBagLayout());
        int numberChats = lastChats.length;
        if (!(lastChats[0] == "" && numberChats == 1)) {
            for (int j = 0; j < numberChats; j++) {
                JButton userButton = new JButton(lastChats[j]);//lastChats[i]);
                userButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        interfaceSendClickedChat(userButton.getText());
                    }
                });

                userButton.setFont(new Font("Arial", Font.PLAIN, 17));
                userButton.setHorizontalAlignment(SwingConstants.LEFT);
                userButton.setVerticalAlignment(SwingConstants.TOP);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = j;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                if (j + 1 == numberChats) {
                    gbc.weightx = 1.0;
                    gbc.weighty = 1.0;
                }
                lastChatsPanel.add(userButton, gbc);
            }
            for (int k = 0; k < numberChats; k++) {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = numberChats;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                
                JPanel filler = new JPanel();
                filler.setOpaque(false);
                lastChatsPanel.add(filler, gbc);
            }
            lastChatsPanel.revalidate();
            lastChatsPanel.repaint();
        }
    }

    
    
    // lädt die Parent-UI-Komponente, die rechts den Chatverlauf anzeigt neu
    public void updateChatHistory(String[][] newChatHistory){
        messagePanel.removeAll();


        int numberOfMessages = newChatHistory.length;
        for (int i = numberOfMessages-1; i > (-1); i--) {

            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 0;
            gbc1.gridy = i;
            gbc1.fill = GridBagConstraints.HORIZONTAL;
            gbc1.anchor = GridBagConstraints.NORTHWEST;
            if (i + 1 == numberOfMessages) {
                gbc1.weightx = 1.0;
                gbc1.weighty = 1.0;
            }

            JEditorPane metaDataLabel = new JEditorPane();
            metaDataLabel.setEditable(false);
            metaDataLabel.setContentType("text/html");




            if (newChatHistory[i][0] != username) {
                metaDataLabel.setText("<html> <font size=\"3\">"+ newChatHistory[i][0] + " schrieb um " + newChatHistory[i][1] + ":" + "</font><br/><font size=\"5\">" + newChatHistory[i][2] + "</font></html>");
            }

            else {
                metaDataLabel.setText("<html> <div align=\"right\"> <font size=\"3\">"+ newChatHistory[i][1] + "</font><br/><font size=\"5\">" + newChatHistory[i][2] + "</font></div></html>");
            }
            messagePanel.add(metaDataLabel, gbc1);


        }
        for (int i = 0; i < numberOfMessages; i++) {
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 0;
            gbc2.gridy = numberOfMessages;
            gbc2.fill = GridBagConstraints.HORIZONTAL;
            gbc2.anchor = GridBagConstraints.NORTHWEST;
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            messagePanel.add(filler, gbc2);
        }


        messagePanel.revalidate();
        messagePanel.repaint();

    }

    
    // baut das Login-Fenster auf
    public void login() {
        loginFrame = new JFrame("Login");
        loginFrame.setVisible(true);
        //loginFrame = new JFrame("Login");
        usernameField = new JTextField(20);
        passwordField = new JTextField(20);

        loginFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                interfaceLoginClosed();
                e.getWindow().dispose();
            }
        });

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");


        JLabel empty1 = new JLabel("");
        JLabel empty2 = new JLabel("");
        JLabel empty3 = new JLabel("");
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JLabel successMessageLabel = new JLabel("Please enter your credentials!");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] loginDataArray= {usernameField.getText(), passwordField.getText(), "login"};
                String[] userList = interfaceLogin(loginDataArray);
                if(userList != null){
                    for(int i=0; i<userList.length;i++){
                        userList[i] = userList[i].replace("#", " ");
                    }
                    lastChats = userList;

                    username = usernameField.getText();
                    System.out.println("login" + username);
                    loginFrame.setVisible(false);
                    messanger();

                }
                else {
                    successMessageLabel.setText("Login failed");

                }

            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] loginDataArray= {usernameField.getText(), passwordField.getText(), "register"};
                String[] userList = interfaceLogin(loginDataArray);
                if(userList!=null){
                    for(int i=0; i<userList.length;i++){
                        userList[i] = userList[i].replace("#", " ");
                    }
                    lastChats = userList;

                    username = usernameField.getText();
                    loginFrame.setVisible(false);
                    messanger();
                }
                else {
                    successMessageLabel.setText("Choose another username!");

                }

            }
        });
        loginFrame.setLayout(new FlowLayout());
        loginFrame.add(usernameLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(empty1);
        loginFrame.add(loginButton);
        loginFrame.add(empty2);
        loginFrame.add(registerButton);
        loginFrame.add(empty3);
        loginFrame.add(successMessageLabel);
        loginFrame.pack();
        
    }

    // baut das Emoji-Fenster auf
    public void emojis(JEditorPane typeField) {
        JFrame emojiFrame = new JFrame("Emojis");
        emojiFrame.setVisible(true);

        JEditorPane emojiPane = new JEditorPane();
        emojiPane.setEditable(false);
        emojiPane.setContentType("text/html");
        emojiPane.setText("<html><font size=\"6\" font-family=\"courier\">Faces & People<br/>&#x1F600 &#x1F601 &#x1F602 &#x1F603 &#x1F604 &#x1F605 &#x1F606 &#x1F607 &#x1F608 &#x1F609 &#x1F60A &#x1F60B &#x1F60C &#x1F60D &#x1F60E &#x1F60F &#x1F610 &#x1F611 &#x1F612 &#x1F613 &#x1F614 &#x1F615 &#x1F616 &#x1F617 &#x1F618 &#x1F619 &#x1F61A &#x1F61B &#x1F61C &#x1F61D &#x1F61E &#x1F61F &#x1F620 &#x1F621 &#x1F622 &#x1F623 &#x1F624 &#x1F625 &#x1F626 &#x1F627 &#x1F628 &#x1F629 &#x1F62A &#x1F62B &#x1F62C &#x1F62D &#x1F630 &#x1F631 &#x1F632 &#x1F633 &#x1F634 &#x1F635 &#x1F636 &#x1F637 &#x1F638 &#x1F639 &#x1F63A &#x1F63B &#x1F63C &#x1F63D &#x1F63E &#x1F63F &#x1F640 &#x1F641 &#x1F642 &#x1F643 &#x1F644 &#x1F645 &#x1F646 &#x1F647 &#x1F648 &#x1F649 &#x1F64A &#x1F64B &#x1F64C &#x1F64D &#x1F64E &#x1F64F &#x1F590 &#x1F917 &#x1F923 &#x1F930 &#x1F936 &#x1F491 &#x1F46D &#x1F46C &#x1F46B &#x1F46A &#x1F44D &#x1F44E &#x1F4A9 &#x1F4AA &#x1F497<br/>" +
                "Traveling & Places <br/>&#x1F30E &#x1F680 &#x1F681 &#x1F682 &#x1F683 &#x1F684 &#x1F685 &#x1F686 &#x1F687 &#x1F688 &#x1F689 &#x1F68A &#x1F68B &#x1F68C &#x1F68D &#x1F68E &#x1F68F &#x1F690 &#x1F691 &#x1F692 &#x1F693 &#x1F694 &#x1F695 &#x1F696 &#x1F697 &#x1F698 &#x1F699 &#x1F6A0 &#x1F6A1 &#x1F6A2 &#x1F6A3 &#x1F6A4 <br/>" +  //&#x1F6A5 &#x1F6A6 &#x1F6A7 &#x1F6A8 &#x1F6A9 &#x1F6AA &#x1F6AB &#x1F6AC &#x1F6AD &#x1F6AE &#x1F6AF &#x1F6B0 &#x1F6B1 &#x1F6B2 &#x1F6B3 &#x1F6B4 &#x1F6B5 &#x1F6B6 &#x1F6B7 &#x1F6B8 &#x1F6B9 &#x1F6BA &#x1F6BB &#x1F6BC &#x1F6BD &#x1F6BE &#x1F6BF &#x1F6C0 &#x1F6C1 &#x1F6C2 &#x1F6C3 &#x1F6C4 &#x1F6C5 &#x1F6C6 &#x1F6C7 &#x1F6C8 &#x1F6C9 &#x1F6CA &#x1F6CB &#x1F6CC &#x1F6CD &#x1F6CE &#x1F6CF &#x1F6D0 &#x1F6D1 &#x1F6D2 &#x1F6F0 &#x1F6F1 &#x1F6F2 &#x1F6F3 &#x1F6F4 &#x1F6F5 &#x1F6F6
                "Food & Drinks <br/> &#x1F95C &#x1F33D &#x1F345 &#x1F346 &#x1F347 &#x1F348 &#x1F349 &#x1F34A &#x1F34B &#x1F34C &#x1F34D &#x1F34E &#x1F34F &#x1F350 &#x1F351 &#x1F352 &#x1F353 &#x1F378 &#x1F379 &#x1F37A &#x1F37B &#x1F37C &#x1F37D &#x1F37E" +
                "<br/> Animals <br/>&#x1F400 &#x1F401 &#x1F402 &#x1F403 &#x1F404 &#x1F405 &#x1F406 &#x1F407 &#x1F408 &#x1F409 &#x1F40A &#x1F40B &#x1F40C &#x1F40D &#x1F40E &#x1F40F &#x1F410 &#x1F411 &#x1F412 &#x1F413 &#x1F414 &#x1F415 &#x1F416 &#x1F417 &#x1F418 &#x1F419 &#x1F41A &#x1F41B &#x1F41C &#x1F41D &#x1F41E &#x1F41F &#x1F420 &#x1F421 &#x1F422 &#x1F423 &#x1F424 &#x1F425 &#x1F426 &#x1F427 &#x1F428 &#x1F429 &#x1F42A &#x1F42B &#x1F42C &#x1F42D &#x1F42E &#x1F42F &#x1F430 &#x1F431 &#x1F432 &#x1F433 &#x1F434 &#x1F435 &#x1F436 &#x1F437 &#x1F438 &#x1F439 &#x1F43A &#x1F43B &#x1F43C &#x1F43D &#x1F43E &#x1F43F<font>");
        emojiPane.setFont(new Font("Arial", Font.PLAIN, 5));

        emojiPane.addMouseListener(new EmojiClickListener(typeField));
        emojiFrame.add(emojiPane);
        emojiFrame.setSize(new Dimension(1000, 400));
        emojiFrame.setResizable(false);

    }


    // Baut das Messanger-Fenster (Haupt-Fenster) auf
    public void messanger() {
        messangerFrame.setTitle(username);
        messangerFrame.setSize(new Dimension(1000,500));
        messangerFrame.setMinimumSize(new Dimension(1000,500));
        messangerFrame.setVisible(true);
        Rectangle r = messangerFrame.getBounds();
        int h = r.height;
        int w = r.width;
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        messangerFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                interfaceLogoff();
                e.getWindow().dispose();
            }
        });

        messangerFrame.setLayout(new BoxLayout(messangerFrame.getContentPane(), BoxLayout.X_AXIS));


        messangerFrame.setLayout(new GridBagLayout());
        GridBagConstraints messangerFrame1 = new GridBagConstraints();
        GridBagConstraints messangerFrame2 = new GridBagConstraints();
        GridBagConstraints messangerFrame3 = new GridBagConstraints();

        messangerFrame1.gridx = 0;
        messangerFrame2.gridy = 0;
        messangerFrame2.gridx = 1;
        messangerFrame2.gridy = 0;
        messangerFrame2.gridwidth = 2;


        messangerFrame1.weightx = 1.0;
        messangerFrame1.weighty = 1.0;
        messangerFrame1.fill = GridBagConstraints.BOTH;
        messangerFrame2.fill = GridBagConstraints.BOTH;

        messangerFrame2.weightx = 1.0;
        messangerFrame2.weighty = 1.0;

        messangerFrame.add(leftPanel, messangerFrame1);
        messangerFrame.add(rightPanel, messangerFrame2);

        currentChatBar.setFont(new Font("Arial", Font.PLAIN, 25));



        numberOfMessages = chatHistory.length;

        messagePanel.setLayout(new GridBagLayout());
        for (int i = numberOfMessages-1; i > (-1); i--) {

            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 0;
            gbc1.gridy = i;
            gbc1.fill = GridBagConstraints.HORIZONTAL;
            gbc1.anchor = GridBagConstraints.NORTHWEST;
            if (i + 1 == numberOfMessages) {
                gbc1.weightx = 1.0;
                gbc1.weighty = 1.0;
            }

            JEditorPane metaDataLabel = new JEditorPane();
            metaDataLabel.setEditable(false);
            metaDataLabel.setContentType("text/html");
            if (chatHistory[i][0] != username) {
                metaDataLabel.setText("<html> <font size=\"3\">"+ chatHistory[i][1] + "</font><br/><font size=\"5\">" + chatHistory[i][2] + "</font></html>");
            }
            else {
                metaDataLabel.setText("<html> <div align=\"right\"> <font size=\"3\">"+ chatHistory[i][1] + "</font><br/><font size=\"5\">" + chatHistory[i][2] + "</font></div></html>");
            }
            messagePanel.add(metaDataLabel, gbc1);


        }
        for (int i = 0; i < numberOfMessages; i++) {
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 0;
            gbc2.gridy = numberOfMessages;
            gbc2.fill = GridBagConstraints.HORIZONTAL;
            gbc2.anchor = GridBagConstraints.NORTHWEST;
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            messagePanel.add(filler, gbc2);
        }



        JScrollPane messagePane = new JScrollPane(messagePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        JPanel typeBar = new JPanel();
        typeBar.setLayout(new BoxLayout(typeBar, BoxLayout.X_AXIS));
        JEditorPane typeBarTextField = new JEditorPane();
        JScrollPane typeBarTextFieldScrollPane = new JScrollPane(typeBarTextField);
        typeBarTextField.setContentType("text/html");
        typeBarTextField.setText("<html></html>");
        JButton typeBarEmojiButton = new JButton("emojis");
        typeBarEmojiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emojis(typeBarTextField);
            }
        });
        JButton typeBarSendButton = new JButton("send");
        typeBarSendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String interfaceSendMessageString = typeBarTextField.getText().replace("<html>", "").replace("</p>","").replace("<head>", "").replace("<body>", "").replace("</html>", "").replace("</head>", "").replace("</body>", "").replace("<p style=\"margin-top: 0\">", "").replace("\n", "").replace("\n", ""); // Zeitstempel & Sender werden vom Server gesetzt, damit keine Manipulation möglich ist, oder?
                String[] splitStr = interfaceSendMessageString.split("\\s+");
                String newString = "";
                for (String str : splitStr){
                    if(str != ""){
                        newString = newString + str + " ";}
                }
                interfaceSendMessage(newString.substring(1));
                typeBarTextField.setText("");
            }
        });
        JPanel typeBarButtonPanel = new JPanel();
        typeBarButtonPanel.setLayout(new GridLayout(2,1));
        typeBarButtonPanel.setMinimumSize(new Dimension(10, 100));
        typeBarButtonPanel.add(typeBarEmojiButton);
        typeBarButtonPanel.add(typeBarSendButton);

        typeBar.add(typeBarTextFieldScrollPane);
        typeBar.add(typeBarButtonPanel);
        messageAndTypePanel.setLayout(new BoxLayout(messageAndTypePanel, BoxLayout.Y_AXIS));
        typeBar.setMinimumSize(new Dimension(100, 200));
        messageAndTypePanel.add(messagePane);
        messageAndTypePanel.add(typeBar);
        rightPanel.add(messageAndTypePanel, BorderLayout.CENTER);
        rightPanel.add(currentChatBar, BorderLayout.NORTH);




        JPanel searchContactsBar = new JPanel();
        searchContactsBar.setLayout(new BorderLayout());

        //user zum neuen Chat beginnen suchen
        JTextField searchContactsField = new JTextField();

        //bei Click wird neuer Chat erstellt
        JButton searchContactsButton = new JButton("+");

        searchContactsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interfaceAddUser(searchContactsField.getText());
                searchContactsField.setText("");
            }
        });
        searchContactsButton.setPreferredSize(new Dimension(49, 49));
        searchContactsButton.setMaximumSize(new Dimension(49, 49));



        leftPanel.add(searchContactsBar, BorderLayout.NORTH);

        searchContactsBar.add(searchContactsField, BorderLayout.CENTER);
        searchContactsBar.add(searchContactsButton, BorderLayout.EAST);
        int numberChats = lastChats.length;
        lastChatsPanel.setLayout(new GridBagLayout());
        String[] compare = {""};
        if(!(Arrays.equals(compare, lastChats))) {
            for (int j = 0; j < numberChats; j++) {
                JButton userButton = new JButton(lastChats[j]);//lastChats[i]);
                userButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        interfaceSendClickedChat(userButton.getText());
                    }
                });
                userButton.setFont(new Font("Arial", Font.PLAIN, 17));
                userButton.setHorizontalAlignment(SwingConstants.LEFT);
                userButton.setVerticalAlignment(SwingConstants.TOP);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = j;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                if (j + 1 == numberChats) {
                    gbc.weightx = 1.0;
                    gbc.weighty = 1.0;
                }
                lastChatsPanel.add(userButton, gbc);
            }
            for (int k = 0; k < numberChats; k++) {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = numberChats;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                JPanel filler = new JPanel();
                filler.setOpaque(false);
                lastChatsPanel.add(filler, gbc);
            }
        }

        JScrollPane lastChatsScrollPane = new JScrollPane(lastChatsPanel);
        leftPanel.add(lastChatsScrollPane, BorderLayout.CENTER);

    }}
