package com.muc;

/**
 * Created by Mahmoud on 03,2020
 */
public interface UserStatusListener {
    public void online(String login);
    public void offline(String login);
}
