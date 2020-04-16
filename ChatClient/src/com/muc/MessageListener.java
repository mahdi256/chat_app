package com.muc;

/**
 * Created by Mahmoud on 03,2020
 */

    public interface MessageListener { //Interface, dass eine Methode onMessage implementiert
        public void onMessage(String fromLogin, String msgBody);
    }

