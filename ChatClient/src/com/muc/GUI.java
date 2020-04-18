package com.muc;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;



public class GUI {

    String      title     = "Messanger";
    GUI         gui;
    JFrame      messangerFrame    = new JFrame(title);
    JButton     sendMessage;
    JTextField  messageBox;
    JTextArea   chatBox;
    JTextField  usernameField;
    JTextField  passwordField;
    JFrame      loginFrame;
    JPanel messagePanel = new JPanel();
    String[][] chatHistory;
    String username;
    ChatClient client;
    JPanel messageAndTypePanel = new JPanel();
    JScrollPane messagePane;

    public GUI(ChatClient client){
        this.client = client;
    }


    String[] lastChats = {}; //{"user a", "user b", "user c", "user d","user a", "user b", "user c", "user d","user a", "user b", "user c", "user d","user a", "user b", "user c", "user d" ,"user a", "user b", "user c", "user d","user a", "user b", "user c", "user d" };
    //String[][] lastChatsPreview = {{"user a", "Me: vdmlfmvlkfdmvldkfmv"}, {"user b", "user b: dmklmlsmfösf,sfl,öd,"}, {"user c", "me: dfgkglkfoeföodskföoekfok"}, {"user d", "me: fdfklmliefmwefopmeo"}};
    // {/* nur das hier zurückgeben*/{"me", "14.04.2020 - 20:10", "erste Nachricht &#129409;"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"}, {"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"}, {"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"}};
    //List<String[]> chatHistory1 = Arrays.asList(chatHistoryArray);
    //List<String[]> chatHistory = new ArrayList<>(chatHistory1);

    //int numberOfMessages = chatHistory.();
   /* public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                */
               // GUI gui = new GUI();
                //gui.emojis();
                //gui.messanger();
              //  gui.login();
          //  }
      //  });
    //}


    public boolean interfaceLogin(String[] loginData){
        // ihr könnt Login-Daten konsumieren
        if(client.connect()){
            try{
                if(client.login(loginData[0], loginData[1], loginData[2])){
                    return true;
                }else{
                    return false;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public void interfaceAddUser(String user){
        // wird aufgerufen, wenn ein neuer Chat erstellt wird
        client.createChat(user);

    }


    public void interfaceReceiveNewUserList(String[] userList){
        // ihr ruft die auf

        // ich baue links die User-Liste neu auf
    }

    public void interfaceSendClickedChat(String clickedChat){
        client.openChat(clickedChat);
    }

    public void interfaceUIReceiveNewChatHistory(String[] newChatHistory){
        // ihr ruft die auf
        // ich baue den Chatverlauf neu auf
    }

    public void interfaceSendMessage(String sentMessage){
        try {
            client.sendMessage(sentMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[][] sentMessageArray = {{"antwort user", "zeit", sentMessage}};
        interfaceReceiveMessage(sentMessageArray);
    }


    public void interfaceReceiveMessage(String[][] receivedMessage){
        updateChatHistory(receivedMessage);
    }

    public void updateChatHistory(String[][] newChatHistory){
        messagePanel.removeAll();
        messagePanel.revalidate();
        messagePanel.repaint();

        int numberOfMessages = newChatHistory.length;
        messagePanel.setLayout(new GridBagLayout());
        for (int i = 0; i < numberOfMessages; i++) {

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

            // links oder rechts je nach Sender/Empfänger
            if (newChatHistory[i][0] != username) {
                metaDataLabel.setText("<html> <font size=\"3\">"+ newChatHistory[i][1] + "</font><br/><font size=\"5\">" + newChatHistory[i][2] + "</font></html>");
            }

            else {
                metaDataLabel.setText("<html> <div align=\"right\"> <font size=\"3\">"+ newChatHistory[i][1] + "</font><br/><font size=\"5\">" + newChatHistory[i][2] + "</font></div></html>");
            }
            messagePanel.add(metaDataLabel, gbc1);
            //metaDataLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            //metaDataLabel.setBorder(BorderFactory.createLineBorder(Color.black));
            //JLabel emptyMetaDataLabel = new JLabel();
            //messagePanel.add(emptyMetaDataLabel);
            //JLabel messageLabel = new JLabel(chatHistory[i][2], SwingConstants.LEFT);
            //messageLabel.setBorder(BorderFactory.createLineBorder(Color.black));
            //messagePanel.add(messageLabel);
            //messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            //JLabel emptyMessageLabel = new JLabel();
            //messagePanel.add(emptyMessageLabel);

           /* } else {
                //JLabel emptyMetaDataLabel = new JLabel();
                //messagePanel.add(emptyMetaDataLabel);
                JLabel metaDataLabel = new JLabel(chatHistory[i][0] + "   " + chatHistory[i][1],SwingConstants.RIGHT);
                messagePanel.add(metaDataLabel);
                metaDataLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                //JLabel emptyMessageLabel = new JLabel();
                //messagePanel.add(emptyMessageLabel);
                JLabel messageLabel = new JLabel(chatHistory[i][2],SwingConstants.RIGHT);
                messagePanel.add(messageLabel);
                messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                //messageLabel.setBorder(BorderFactory.createLineBorder(Color.black));
                //metaDataLabel.setBorder(BorderFactory.createLineBorder(Color.black));



            } */

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



        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = messagePane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });




    } // Bestätigung zurück?

    public void login() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setVisible(true);
        //loginFrame = new JFrame("Login");
        usernameField = new JTextField(20);
        passwordField = new JTextField(20);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");


        JLabel empty1 = new JLabel("");
        JLabel empty2 = new JLabel("");
        JLabel empty3 = new JLabel("");
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JLabel successMessageLabel = new JLabel("Please enter your crendentials!");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String[] loginDataArray= {usernameField.getText(), passwordField.getText(), "login"};
                    boolean success = interfaceLogin(loginDataArray);
                    if(success){
                        username = usernameField.getText();
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
                boolean success = interfaceLogin(loginDataArray);
                if(success){
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

        //enterServer.addActionListener(new enterServerButtonListener());
        /*
        JPanel loginPanel = new JPanel(new GridBagLayout());

        GridBagConstraints loginRight = new GridBagConstraints();
        loginRight.insets = new Insets(0, 0, 0, 10);
        loginRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints loginLeft = new GridBagConstraints();
        loginLeft.anchor = GridBagConstraints.WEST;
        loginLeft.insets = new Insets(0, 10, 0, 10);
        // loginRight.weightx = 2.0;
        loginRight.fill = GridBagConstraints.HORIZONTAL;
        loginRight.gridwidth = GridBagConstraints.REMAINDER;

        loginPanel.add(chooseUsernameLabel, loginLeft);
        loginPanel.add(usernameField, loginRight);
        loginPanel.add(passwordField, loginRight);

        loginFrame.add(BorderLayout.CENTER, loginPanel);
        loginFrame.add(BorderLayout.SOUTH, enterServer);
        loginFrame.setSize(300, 300);
        loginFrame.setVisible(true);

        */

    }

    public void emojis(JEditorPane typeField) {
        JFrame emojiFrame = new JFrame("Emojis");
        emojiFrame.setVisible(true);
        /*
        try {
            Image img = ImageIO.read(getClass().getResource("emojis.png"));
            Image im = img.getScaledInstance( 420, 480,  java.awt.Image.SCALE_SMOOTH ) ;
            emojiFrame.add(new JLabel(new ImageIcon(im)));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        */
        JEditorPane emojiPane = new JEditorPane();
        //emojiFrame.add(new JLabel(new ImageIcon("emojis.png")));
        emojiPane.setEditable(false);
        emojiPane.setContentType("text/html");
        emojiPane.setText("<html><font size=\"6\" font-family=\"courier\">Faces & People<br/>&#x1F600 &#x1F601 &#x1F602 &#x1F603 &#x1F604 &#x1F605 &#x1F606 &#x1F607 &#x1F608 &#x1F609 &#x1F60A &#x1F60B &#x1F60C &#x1F60D &#x1F60E &#x1F60F &#x1F610 &#x1F611 &#x1F612 &#x1F613 &#x1F614 &#x1F615 &#x1F616 &#x1F617 &#x1F618 &#x1F619 &#x1F61A &#x1F61B &#x1F61C &#x1F61D &#x1F61E &#x1F61F &#x1F620 &#x1F621 &#x1F622 &#x1F623 &#x1F624 &#x1F625 &#x1F626 &#x1F627 &#x1F628 &#x1F629 &#x1F62A &#x1F62B &#x1F62C &#x1F62D &#x1F630 &#x1F631 &#x1F632 &#x1F633 &#x1F634 &#x1F635 &#x1F636 &#x1F637 &#x1F638 &#x1F639 &#x1F63A &#x1F63B &#x1F63C &#x1F63D &#x1F63E &#x1F63F &#x1F640 &#x1F641 &#x1F642 &#x1F643 &#x1F644 &#x1F645 &#x1F646 &#x1F647 &#x1F648 &#x1F649 &#x1F64A &#x1F64B &#x1F64C &#x1F64D &#x1F64E &#x1F64F &#x1F590 &#x1F917 &#x1F923 &#x1F930 &#x1F936 &#x1F491 &#x1F46D &#x1F46C &#x1F46B &#x1F46A &#x1F44D &#x1F44E &#x1F4A9 &#x1F4AA &#x1F497<br/>" +
                "Traveling & Places <br/>&#x1F30E &#x1F680 &#x1F681 &#x1F682 &#x1F683 &#x1F684 &#x1F685 &#x1F686 &#x1F687 &#x1F688 &#x1F689 &#x1F68A &#x1F68B &#x1F68C &#x1F68D &#x1F68E &#x1F68F &#x1F690 &#x1F691 &#x1F692 &#x1F693 &#x1F694 &#x1F695 &#x1F696 &#x1F697 &#x1F698 &#x1F699 &#x1F6A0 &#x1F6A1 &#x1F6A2 &#x1F6A3 &#x1F6A4 <br/>" +  //&#x1F6A5 &#x1F6A6 &#x1F6A7 &#x1F6A8 &#x1F6A9 &#x1F6AA &#x1F6AB &#x1F6AC &#x1F6AD &#x1F6AE &#x1F6AF &#x1F6B0 &#x1F6B1 &#x1F6B2 &#x1F6B3 &#x1F6B4 &#x1F6B5 &#x1F6B6 &#x1F6B7 &#x1F6B8 &#x1F6B9 &#x1F6BA &#x1F6BB &#x1F6BC &#x1F6BD &#x1F6BE &#x1F6BF &#x1F6C0 &#x1F6C1 &#x1F6C2 &#x1F6C3 &#x1F6C4 &#x1F6C5 &#x1F6C6 &#x1F6C7 &#x1F6C8 &#x1F6C9 &#x1F6CA &#x1F6CB &#x1F6CC &#x1F6CD &#x1F6CE &#x1F6CF &#x1F6D0 &#x1F6D1 &#x1F6D2 &#x1F6F0 &#x1F6F1 &#x1F6F2 &#x1F6F3 &#x1F6F4 &#x1F6F5 &#x1F6F6
                "Food & Drinks <br/> &#x1F95C &#x1F33D &#x1F345 &#x1F346 &#x1F347 &#x1F348 &#x1F349 &#x1F34A &#x1F34B &#x1F34C &#x1F34D &#x1F34E &#x1F34F &#x1F350 &#x1F351 &#x1F352 &#x1F353 &#x1F378 &#x1F379 &#x1F37A &#x1F37B &#x1F37C &#x1F37D &#x1F37E" +
                "<br/> Animals <br/>&#x1F400 &#x1F401 &#x1F402 &#x1F403 &#x1F404 &#x1F405 &#x1F406 &#x1F407 &#x1F408 &#x1F409 &#x1F40A &#x1F40B &#x1F40C &#x1F40D &#x1F40E &#x1F40F &#x1F410 &#x1F411 &#x1F412 &#x1F413 &#x1F414 &#x1F415 &#x1F416 &#x1F417 &#x1F418 &#x1F419 &#x1F41A &#x1F41B &#x1F41C &#x1F41D &#x1F41E &#x1F41F &#x1F420 &#x1F421 &#x1F422 &#x1F423 &#x1F424 &#x1F425 &#x1F426 &#x1F427 &#x1F428 &#x1F429 &#x1F42A &#x1F42B &#x1F42C &#x1F42D &#x1F42E &#x1F42F &#x1F430 &#x1F431 &#x1F432 &#x1F433 &#x1F434 &#x1F435 &#x1F436 &#x1F437 &#x1F438 &#x1F439 &#x1F43A &#x1F43B &#x1F43C &#x1F43D &#x1F43E &#x1F43F<font>");
        emojiPane.setFont(new Font("Arial", Font.PLAIN, 5));

        //EmojiClickListener clickEmoji = new EmojiClickListener(typeField);
        emojiPane.addMouseListener(new EmojiClickListener(typeField));
        emojiFrame.add(emojiPane);
        //emojiFrame.setLayout(new GridLayout());
        emojiFrame.setSize(new Dimension(1000, 400));
        emojiFrame.setResizable(false);

    }



    public void messanger() {

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

//        messangerFrame.setLayout(new BoxLayout(messangerFrame.getContentPane(), BoxLayout.X_AXIS));
        messangerFrame.setLayout(new BoxLayout(messangerFrame.getContentPane(), BoxLayout.X_AXIS));

        //leftPanel.setPreferredSize(new Dimension((int)Math.round(w*0.2), h));

        // leftPanel.setMaximumSize(new Dimension(5000, 100000));
        messangerFrame.setLayout(new GridBagLayout());
        GridBagConstraints messangerFrame1 = new GridBagConstraints();
        GridBagConstraints messangerFrame2 = new GridBagConstraints();
        GridBagConstraints messangerFrame3 = new GridBagConstraints();

        messangerFrame1.gridx = 0;
        messangerFrame2.gridy = 0;
        messangerFrame2.gridx = 1;
        messangerFrame2.gridy = 0;
        messangerFrame2.gridwidth = 2;
        //messangerFrame3.gridx = 0;
        //messangerFrame3.gridy = 0;
        //messangerFrame3.gridwidth = 3;

        messangerFrame1.weightx = 1.0;
        messangerFrame1.weighty = 1.0;
        messangerFrame1.fill = GridBagConstraints.BOTH;
        messangerFrame2.fill = GridBagConstraints.BOTH;

        messangerFrame2.weightx = 1.0;
        messangerFrame2.weighty = 1.0;

        messangerFrame.add(leftPanel, messangerFrame1);
        messangerFrame.add(rightPanel, messangerFrame2);
        //messangerFrame.add(topPanel);

        JLabel currentChatBar = new JLabel("Daniel Kossack");
        currentChatBar.setFont(new Font("Arial", Font.PLAIN, 25));
        /*JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setEditorKit(new HTMLEditorKit());

        String filename = "C:/Users/d073453/Documents/01-DHBW/TP4/Verteilte Systeme/chat_app/ChatClient/src/com/muc/chatverlauf.html";

        try {
            FileReader fr = new FileReader(filename);
            editorPane.read(fr, filename);

        }
        catch (Exception ex) {
        System.out.println(ex);

        }*/

        int numberOfMessages = 0;
        messagePanel.setLayout(new GridBagLayout());
        for (int i = 0; i < numberOfMessages; i++) {

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
                //metaDataLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                //metaDataLabel.setBorder(BorderFactory.createLineBorder(Color.black));
                //JLabel emptyMetaDataLabel = new JLabel();
                //messagePanel.add(emptyMetaDataLabel);
                //JLabel messageLabel = new JLabel(chatHistory[i][2], SwingConstants.LEFT);
                //messageLabel.setBorder(BorderFactory.createLineBorder(Color.black));
                //messagePanel.add(messageLabel);
                //messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                //JLabel emptyMessageLabel = new JLabel();
                //messagePanel.add(emptyMessageLabel);

           /* } else {
                //JLabel emptyMetaDataLabel = new JLabel();
                //messagePanel.add(emptyMetaDataLabel);
                JLabel metaDataLabel = new JLabel(chatHistory[i][0] + "   " + chatHistory[i][1],SwingConstants.RIGHT);
                messagePanel.add(metaDataLabel);
                metaDataLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                //JLabel emptyMessageLabel = new JLabel();
                //messagePanel.add(emptyMessageLabel);
                JLabel messageLabel = new JLabel(chatHistory[i][2],SwingConstants.RIGHT);
                messagePanel.add(messageLabel);
                messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                //messageLabel.setBorder(BorderFactory.createLineBorder(Color.black));
                //metaDataLabel.setBorder(BorderFactory.createLineBorder(Color.black));



            } */

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



        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = messagePane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });




        //DefaultStyledDocument document = new DefaultStyledDocument();
            //JTextPane messageTextPane = new JTextPane(document);
            //JScrollPane messagePane = new JScrollPane(messageTextPane);
            //messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
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
                    interfaceSendMessage(typeBarTextField.getText()); // Zeitstempel & Sender werden vom Server gesetzt, damit keine Manipulation möglich ist, oder?
                    System.out.println(typeBarTextField.getText());
                }
            });
            //typeBarSendButton.setMinimumSize(new Dimension(20, 100));
            JPanel typeBarButtonPanel = new JPanel();
            typeBarButtonPanel.setLayout(new GridLayout(2,1));
            typeBarButtonPanel.setMinimumSize(new Dimension(10, 100));
            typeBarButtonPanel.add(typeBarEmojiButton);
            typeBarButtonPanel.add(typeBarSendButton);

        typeBar.add(typeBarTextFieldScrollPane);
            typeBar.add(typeBarButtonPanel);
        //    typeBar.add(typeBarSendButton);
            JPanel messageAndTypePanel = new JPanel();
            messageAndTypePanel.setLayout(new BoxLayout(messageAndTypePanel, BoxLayout.Y_AXIS));
            typeBar.setMinimumSize(new Dimension(100, 200));
            messageAndTypePanel.add(messagePane);
            messageAndTypePanel.add(typeBar);
            rightPanel.add(messageAndTypePanel, BorderLayout.CENTER);
            rightPanel.add(currentChatBar, BorderLayout.NORTH);
            //rightPanel.add(typeBar, BorderLayout.SOUTH);


            //messangerFrame.add()


            JPanel searchContactsBar = new JPanel();
            searchContactsBar.setLayout(new BorderLayout());

            // user zum neuen Chat beginnen suchen
            JTextField searchContactsField = new JTextField();
            //ImageIcon searchImage = new ImageIcon("loupe.png");

            // neuen Chat beginnen -> bei Click erhält GUI neuen (meist leeren) Verlauf
            JButton searchContactsButton = new JButton("+");
            try {
                Image img = ImageIO.read(getClass().getResource("loupe.png"));
                //Image img = im.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH ) ;
                //BufferedImage thumbnail = resize(img, Method.ULTRA_QUALITY, 125, OP_ANTIALIAS);
                //searchContactsButton.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                System.out.println(ex);
            }

            searchContactsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    interfaceAddUser(searchContactsField.getText());
                }
            });
            searchContactsButton.setPreferredSize(new Dimension(39, 39));
            searchContactsButton.setMaximumSize(new Dimension(39, 39));


            //searchContactsButton.setIcon(new ImageIcon("loupe.png"));

            leftPanel.add(searchContactsBar, BorderLayout.NORTH);

            searchContactsBar.add(searchContactsField, BorderLayout.CENTER);
            searchContactsBar.add(searchContactsButton, BorderLayout.EAST);

            JPanel lastChatsPanel = new JPanel();
            int numberChats = lastChats.length;
            //lastChatsPanel.setLayout(new GridLayout(numberChats, 1));
            lastChatsPanel.setLayout(new GridBagLayout());

            for (int j = 0; j < numberChats; j++) {
                JButton userButton = new JButton(lastChats[j]);//lastChats[i]);
                userButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        interfaceSendClickedChat(userButton.getText());
                    }
                });
                //button.setPreferredSize(new Dimension(100, 100));
                //   Dimension d = button.getPreferredSize();
                // d.height = 50;
                //button.setPreferredSize(d);
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
                //gbc.weightx = 1.0;
                //gbc.weighty = 1.0;
                JPanel filler = new JPanel();
                filler.setOpaque(false);
                lastChatsPanel.add(filler, gbc);
            }

            JScrollPane lastChatsScrollPane = new JScrollPane(lastChatsPanel);
            leftPanel.add(lastChatsScrollPane, BorderLayout.CENTER);

        /*

        JPanel southPanel = new JScrollPane();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new sendMessageButtonListener());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        messangerFrame.add(mainPanel);
        messangerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        messangerFrame.setSize(470, 300);
        messangerFrame.setVisible(true);
    }

    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (messageBox.getText().length() < 1) {
                // do nothing
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                chatBox.append("<" + username + ">:  " + messageBox.getText()
                        + "\n");
                messageBox.setText("");
            }
            messageBox.requestFocusInWindow();
        }
    }

    */
        }}
/*
        class enterServerButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                String username;

                username = usernameField.getText();
                if (username.length() < 1) {
                    System.out.println("No!");
                } else {
                    loginFrame.setVisible(false);
                    messanger();
                }
            }

        }

 */