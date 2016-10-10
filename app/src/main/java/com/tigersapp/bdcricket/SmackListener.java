package com.tigersapp.bdcricket;

import org.jivesoftware.smack.packet.Message;
/**
 * @author Ripon
 */

public interface SmackListener {

    public void onLoginSuccess();
    public void onLoginFailed();
    public void onReceiveMessage(Message message);
}
