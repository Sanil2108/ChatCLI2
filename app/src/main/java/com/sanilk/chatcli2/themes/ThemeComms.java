package com.sanilk.chatcli2.themes;

import android.content.ContentValues;

import com.sanilk.chatcli2.MainActivity;
import com.sanilk.chatcli2.communication.Client;
import com.sanilk.chatcli2.communication.MainCommunication;
import com.sanilk.chatcli2.databases.DatabaseHandlerForConnections;
import com.sanilk.chatcli2.themes.dos.DOSThemeActivity;

import java.util.ArrayList;

/**
 * Created by Admin on 18-06-2017.
 */

public class ThemeComms {
    DOSThemeActivity dosThemeActivity;
    MainCommunication communication;

    public String newMessage="";
    public String newCheckedMessage="";

    public ThemeComms(String user, String password, String receiver, DOSThemeActivity dosThemeActivity){
        communication=new MainCommunication(user, password, receiver, this);
        communication.startReceiving();
        this.dosThemeActivity=dosThemeActivity;
    }

    public ThemeComms(String user, String password, DOSThemeActivity dosThemeActivity){
        communication=new MainCommunication(user, password, this);
        this.dosThemeActivity=dosThemeActivity;
    }

    public String receiveMessages(){
        String temp=newMessage;
        newMessage="";
        return temp;
    }

    public static void signUpClient(String nick, String pass, DOSThemeActivity dosThemeActivity){
        MainCommunication.signUpClient(nick, pass, dosThemeActivity);
    }

    public String checkMessages(String user, DatabaseHandlerForConnections databaseHandlerForConnections){
        ArrayList<ContentValues> allInfo=databaseHandlerForConnections.getAllConnections();
        ArrayList<String> allSendersList=new ArrayList<>();
        for(ContentValues contentValues:allInfo){
            if(contentValues.get("user").equals(user)){
                allSendersList.add((String)contentValues.get("sender"));
            }
        }
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

    public void sendLogs(String logs){
        communication.sendLogs(logs);
    }

}
