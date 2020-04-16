package com.muc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.*;
import java.io.FileReader;
import javax.swing.text.html.HTMLEditorKit;



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

    String[] lastChats = {"user a", "user b", "user c", "user d","user a", "user b", "user c", "user d","user a", "user b", "user c", "user d","user a", "user b", "user c", "user d" ,"user a", "user b", "user c", "user d","user a", "user b", "user c", "user d" };
    String[][] lastChatsPreview = {{"user a", "Me: vdmlfmvlkfdmvldkfmv"}, {"user b", "user b: dmklmlsmfösf,sfl,öd,"}, {"user c", "me: dfgkglkfoeföodskföoekfok"}, {"user d", "me: fdfklmliefmwefopmeo"}};
    String[][] chatHistory = {{"me", "14.04.2020 - 20:10", "erste Nachricht &#129409;"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"}, {"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"}, {"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"},{"me", "14.04.2020 - 20:10", "erste Nachricht"}, {"user a", "14.04.2020 - 20:11", "zweite Nachricht"}};
    int numberOfMessages = chatHistory.length;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GUI gui = new GUI();
                gui.messanger();
            }
        });
    }

    public void login() {
        messangerFrame.setVisible(false);
        loginFrame = new JFrame(title);
        usernameField = new JTextField(15);
        passwordField = new JTextField(15);

        JLabel chooseUsernameLabel = new JLabel("Username:");
        JButton enterServer = new JButton("Enter Chat Server");
        //enterServer.addActionListener(new enterServerButtonListener());
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

    }

    public void messanger() {

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

        JPanel messagePanel = new JPanel();
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
            if (chatHistory[i][0] != "me") {
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
            JTextPane typeBar = new JTextPane();
            typeBar.setLayout(new BoxLayout(typeBar, BoxLayout.X_AXIS));
            JTextArea typeBarTextField = new JTextArea();


            JButton typeBarEmojiButton = new JButton("<html>&#128513</html");
            JButton typeBarSendButton = new JButton(">");
            typeBar.add(typeBarTextField);
            typeBar.add(typeBarEmojiButton);
            typeBar.add(typeBarSendButton);
            JPanel messageAndTypePanel = new JPanel();
            messageAndTypePanel.setLayout(new BoxLayout(messageAndTypePanel, BoxLayout.Y_AXIS));
            messageAndTypePanel.setMinimumSize(new Dimension(100, 100));
            messageAndTypePanel.add(messagePane);
            messageAndTypePanel.add(typeBar);
            rightPanel.add(messageAndTypePanel, BorderLayout.CENTER);
            rightPanel.add(currentChatBar, BorderLayout.NORTH);
            //rightPanel.add(typeBar, BorderLayout.SOUTH);


            //messangerFrame.add()


            JPanel searchContactsBar = new JPanel();
            searchContactsBar.setLayout(new BorderLayout());

            JTextField searchContactsField = new JTextField();
            //ImageIcon searchImage = new ImageIcon("loupe.png");

            JButton searchContactsButton = new JButton();
            try {
                Image img = ImageIO.read(getClass().getResource("loupe.png"));
                //Image img = im.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH ) ;
                //BufferedImage thumbnail = resize(img, Method.ULTRA_QUALITY, 125, OP_ANTIALIAS);
                searchContactsButton.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                System.out.println(ex);
            }

            searchContactsButton.setPreferredSize(new Dimension(30, 30));
            searchContactsButton.setMaximumSize(new Dimension(30, 30));


            //searchContactsButton.setIcon(new ImageIcon("loupe.png"));

            leftPanel.add(searchContactsBar, BorderLayout.NORTH);

            searchContactsBar.add(searchContactsField, BorderLayout.CENTER);
            searchContactsBar.add(searchContactsButton, BorderLayout.EAST);

            JPanel lastChatsPanel = new JPanel();
            int numberChats = lastChats.length;
            //lastChatsPanel.setLayout(new GridLayout(numberChats, 1));
            lastChatsPanel.setLayout(new GridBagLayout());

            for (int j = 0; j < numberChats; j++) {
                JButton button = new JButton(lastChats[j]);//lastChats[i]);
                //button.setPreferredSize(new Dimension(100, 100));
                //   Dimension d = button.getPreferredSize();
                // d.height = 50;
                //button.setPreferredSize(d);
                button.setFont(new Font("Arial", Font.PLAIN, 17));
                button.setHorizontalAlignment(SwingConstants.LEFT);
                button.setVerticalAlignment(SwingConstants.TOP);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = j;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                if (j + 1 == numberChats) {
                    gbc.weightx = 1.0;
                    gbc.weighty = 1.0;
                }
                lastChatsPanel.add(button, gbc);
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