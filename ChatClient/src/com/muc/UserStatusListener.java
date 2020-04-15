package com.muc;

/**
 * Created by Mahmoud on 03,2020
 */
public interface UserStatusListener { //Interface, dass die beiden Methoden online und offline beinhaltet
    public void online(String login);
    public void offline(String login);
}
