package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Created by Mahmoud on 03,2020
 */
public class UserGraphInterface extends JPanel implements UserStatusListener {
    private final ChatClient client;

    private JList<String> userListUI;
    private DefaultListModel<String>userListModel;




    public UserGraphInterface(ChatClient client) {
        this.client= client;
        this.client.addUserStatusListener( this );
        userListModel=new DefaultListModel<>();
        userListUI = new JList<>(userListModel);
        setLayout( new BorderLayout() );
        add(new JScrollPane( userListUI ), BorderLayout.CENTER);
        userListUI.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()>1){
                    String login = userListUI.getSelectedValue(); //to know which user is clicked on.
                    MessagePane messagePane= new MessagePane(client, login);
                    JFrame f = new JFrame( "Message: "+ login );
                    f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
                    f.setSize( 500,500 );
                    f.getContentPane().add( messagePane,BorderLayout.CENTER );
                    f.setVisible( true );

                }
            }
        } );
    }

    public static void main(String[] args){
        ChatClient client= new ChatClient( "localhost", 9999);

        UserGraphInterface userGraphInterface = new UserGraphInterface(client);
        JFrame frame = new JFrame( "User List" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 300,500 );
        frame.getContentPane().add( userGraphInterface , BorderLayout.CENTER );
        frame.setVisible( true );

        if (client.connect()){
           try {
              client.login();
          }catch (IOException e){
              e.printStackTrace();
          }
        }

    }

    @Override
    public void online(String login) {
        userListModel.addElement( login );

    }

    @Override
    public void offline(String login) {
        userListModel.removeElement( login );

    }
}
