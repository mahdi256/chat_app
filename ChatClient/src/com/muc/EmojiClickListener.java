package com.muc;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.*;
import java.io.FileReader;
import javax.swing.text.html.HTMLEditorKit;

public class EmojiClickListener extends MouseAdapter {

    private JEditorPane typefield;

    public EmojiClickListener(JEditorPane typeField){
        super();
        this.typefield = typeField;
    }

    @Override
    public void mouseClicked(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        String[] emojiHex = {"&#x1F600", "&#x1F601", "&#x1F602", "&#x1F603", "&#x1F604", "&#x1F605", "&#x1F606", "&#x1F607", "&#x1F608", "&#x1F609", "&#x1F60A", "&#x1F60B", "&#x1F60C", "&#x1F60D", "&#x1F60E", "&#x1F60F", "&#x1F610", "&#x1F611", "&#x1F612", "&#x1F613", "&#x1F614", "&#x1F615", "&#x1F616", "&#x1F617", "&#x1F618", "&#x1F619", "&#x1F61A", "&#x1F61B", "&#x1F61C", "&#x1F61D", "&#x1F61E", "&#x1F61F", "&#x1F620", "&#x1F621", "&#x1F622", "&#x1F623", "&#x1F624", "&#x1F625", "&#x1F626", "&#x1F627", "&#x1F628", "&#x1F629", "&#x1F62A", "&#x1F62B", "&#x1F62C", "&#x1F62D", "&#x1F630", "&#x1F631", "&#x1F632", "&#x1F633", "&#x1F634", "&#x1F635", "&#x1F636", "&#x1F637", "&#x1F638", "&#x1F639", "&#x1F63A", "&#x1F63B", "&#x1F63C", "&#x1F63D", "&#x1F63E", "&#x1F63F", "&#x1F640", "&#x1F641", "&#x1F642", "&#x1F643", "&#x1F644", "&#x1F645", "&#x1F646", "&#x1F647", "&#x1F648", "&#x1F649", "&#x1F64A", "&#x1F64B", "&#x1F64C", "&#x1F64D", "&#x1F64E", "&#x1F64F", "&#x1F590", "&#x1F917", "&#x1F923", "&#x1F930", "&#x1F936", "&#x1F491", "&#x1F46D", "&#x1F46C", "&#x1F46B", "&#x1F46A", "&#x1F44D", "&#x1F44E", "&#x1F4A9", "&#x1F4AA", "&#x1F497", "", "", "",
                "&#x1F30E", "&#x1F680", "&#x1F681", "&#x1F682", "&#x1F683", "&#x1F684", "&#x1F685", "&#x1F686", "&#x1F687", "&#x1F688", "&#x1F689", "&#x1F68A", "&#x1F68B", "&#x1F68C", "&#x1F68D", "&#x1F68E", "&#x1F68F", "&#x1F690", "&#x1F691", "&#x1F692", "&#x1F693", "&#x1F694", "&#x1F695", "&#x1F696", "&#x1F697", "&#x1F698", "&#x1F699", "&#x1F6A0", "&#x1F6A1", "&#x1F6A2", "&#x1F6A3", "&#x1F6A4",
                "&#x1F95C", "&#x1F33D", "&#x1F345", "&#x1F346", "&#x1F347", "&#x1F348", "&#x1F349", "&#x1F34A", "&#x1F34B", "&#x1F34C", "&#x1F34D", "&#x1F34E", "&#x1F34F", "&#x1F350", "&#x1F351", "&#x1F352", "&#x1F353", "&#x1F378", "&#x1F379", "&#x1F37A", "&#x1F37B", "&#x1F37C", "&#x1F37D", "&#x1F37E", "","","","","","","","",
                "&#x1F400", "&#x1F401", "&#x1F402", "&#x1F403", "&#x1F404", "&#x1F405", "&#x1F406", "&#x1F407", "&#x1F408", "&#x1F409", "&#x1F40A", "&#x1F40B", "&#x1F40C", "&#x1F40D", "&#x1F40E", "&#x1F40F", "&#x1F410", "&#x1F411", "&#x1F412", "&#x1F413", "&#x1F414", "&#x1F415", "&#x1F416", "&#x1F417", "&#x1F418", "&#x1F419", "&#x1F41A", "&#x1F41B", "&#x1F41C", "&#x1F41D", "&#x1F41E", "&#x1F41F", "&#x1F420", "&#x1F421", "&#x1F422", "&#x1F423", "&#x1F424", "&#x1F425", "&#x1F426", "&#x1F427", "&#x1F428", "&#x1F429", "&#x1F42A", "&#x1F42B", "&#x1F42C", "&#x1F42D", "&#x1F42E", "&#x1F42F", "&#x1F430", "&#x1F431", "&#x1F432", "&#x1F433", "&#x1F434", "&#x1F435", "&#x1F436", "&#x1F437", "&#x1F438", "&#x1F439", "&#x1F43A", "&#x1F43B", "&#x1F43C", "&#x1F43D", "&#x1F43E", "&#x1F43F"};




        String clickedEmoji = "";
        int i =0;
       /* if(x>0 && x<30 && y>38 && y<68){
            clickedEmoji = emojiHex[0];
        }*/
        if(y>36 && y<66){
            i = i + 0*32;
        }
        else if(y>35.5+30 && y<65.5+30){
            i = i + 1*32;
        }
        else if(y>35.5+2*30 && y<65.5+2*30){
            i = i + 2*32;
        }
        else if(y>35.5*2+3*30 && y<65.5+35.5+3*30){
            i = i + 3*32;
        }
        else if(y>35.5*3+4*30 && y<65.5+2*35.5+4*30){
            i = i + 4*32;
        }
        else if(y>35.5*4+5*30 && y<65.5+3*35.5+5*30){
            i = i + 5*32;
        }
        else if(y>35.5*4+6*30 && y<66+3*35.5+6*30){
            i = i + 6*32;
        }

        else{
            i=95; // irgendein Wert, der nicht mit einem Emoji belet ist, aber trotzdem als "" im Array ist
        }
        if(i!=95)
        {
        i = i + ((int)(x/29.9));}


        clickedEmoji = emojiHex[i];
        String oldText = typefield.getText();
        typefield.setText(oldText.substring(0,oldText.length()-16) + clickedEmoji + "</body></html>");
               //System.out.println(oldText.substring(0,oldText.length()-16) + clickedEmoji + "</body></html>");
               //System.out.println(oldText);
               System.out.println(clickedEmoji);
    }


}
