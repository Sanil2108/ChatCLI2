package com.sanilk.chatcli2.themes;

import com.sanilk.chatcli2.MainActivity;
import com.sanilk.chatcli2.communication.MainCommunication;

/**
 * Created by Admin on 18-06-2017.
 */

public class ThemeComms {
    MainCommunication communication;

    String newMessage="";

    public ThemeComms(String user, String pass){
        communication=new MainCommunication(user, pass, this);
        communication.startReceiving();
    }

    public String receiveMessages(){
        String temp=newMessage;
        newMessage="";
        return temp;
    }

    public void newMessageReceived(String message){
        //I have to call this from the main thread instead of a helper thread
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//        System.out.println(message);
        newMessage=message;
    }

    public void sendMessage(String message){
        communication.sendMessage(message);
    }

}
