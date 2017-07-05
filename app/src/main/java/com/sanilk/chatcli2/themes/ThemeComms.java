package com.sanilk.chatcli2.themes;

import com.sanilk.chatcli2.MainActivity;
import com.sanilk.chatcli2.communication.Client;
import com.sanilk.chatcli2.communication.MainCommunication;
import com.sanilk.chatcli2.databases.DatabaseHandlerForConnections;

import java.util.ArrayList;

/**
 * Created by Admin on 18-06-2017.
 */

public class ThemeComms {
    MainCommunication communication;

    public String newMessage="";
    public String newCheckedMessage="";

    public ThemeComms(String user, String password, String receiver){
        communication=new MainCommunication(user, password, receiver, this);
        communication.startReceiving();
    }

    public ThemeComms(String user, String password){
        communication=new MainCommunication(user, password, this);
    }

    public String receiveMessages(){
        String temp=newMessage;
        newMessage="";
        return temp;
    }

    public static void signUpClient(String nick, String pass){
        MainCommunication.signUpClient(nick, pass);
    }

    public String checkMessages(DatabaseHandlerForConnections databaseHandlerForConnections){
        ArrayList<String> allSendersList=databaseHandlerForConnections.getAllUsers();
        String[] allSenders=new String[allSendersList.size()];
        for(int i=0;i<allSenders.length;i++){
            allSenders[i]=allSendersList.get(i);
        }
        communication.checkMessages(allSenders);
        String temp=newMessage;
        newMessage="";
        return temp;
    }

    public static boolean checkIfClientIsAuthentic(String sender, String pass){
        return MainCommunication.isClientAuthentic(sender, pass);
    }

    public void newMessagesChecked(String newCheckedMessage){
        this.newCheckedMessage=newCheckedMessage;
    }

    public void newMessageReceived(String message){
        newMessage+=message;
    }

    public void disconnect(){
        communication.stopReceiving();
    }

    public void sendMessage(String message){
        communication.sendMessage(message);
    }

}
