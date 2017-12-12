package com.sanilk.chatcli2.themes;

import android.content.ContentValues;

import com.sanilk.chatcli2.MainActivity;
import com.sanilk.chatcli2.communication.Client;
import com.sanilk.chatcli2.communication.MainCommunication;
import com.sanilk.chatcli2.database.DatabaseOpenHelper;
import com.sanilk.chatcli2.database.Entities.Message;
import com.sanilk.chatcli2.themes.dos.DOSThemeActivity;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Created by Admin on 18-06-2017.
 */

public class ThemeComms {
    public DOSThemeActivity dosThemeActivity;
    MainCommunication communication;

    public ArrayList<Message> messages;
//    public String newMessage="";
    public String newCheckedMessage="";

    public ThemeComms(String user, String password, String receiver, DOSThemeActivity dosThemeActivity){
        communication=new MainCommunication(user, password, receiver, this);
        communication.startReceiving();
        this.dosThemeActivity=dosThemeActivity;
        messages=new ArrayList<>();
    }

    public ThemeComms(String user, String password, DOSThemeActivity dosThemeActivity){
        communication=new MainCommunication(user, password, this);
        this.dosThemeActivity=dosThemeActivity;
        messages=new ArrayList<>();
    }

    public ArrayList<Message> receiveMessages(){
        ArrayList<Message> temp=new ArrayList<>();
        for(int i=0;i<messages.size();i++){
            temp.add(messages.get(i));
            temp.remove(i);
            i--;
        }
        return temp;
    }

    public static void signUpClient(String nick, String pass, DOSThemeActivity dosThemeActivity){
        MainCommunication.signUpClient(nick, pass, dosThemeActivity);
    }

//    public String checkMessages(String user, DatabaseHandlerForConnections databaseHandlerForConnections){
//        ArrayList<String> allSendersList=new ArrayList<>();
//        for(ContentValues contentValues:allInfo){
//            if(contentValues.get("user").equals(user)){
//                allSendersList.add((String)contentValues.get("sender"));
//            }
//        }
//        String[] allSenders=new String[allSendersList.size()];
//        for(int i=0;i<allSenders.length;i++){
//            allSenders[i]=allSendersList.get(i);
//        }
//        communication.checkMessages(allSenders);
//        String temp=newMessage;
//        newMessage="";
//        return temp;
//    }

    public static boolean checkIfClientIsAuthentic(String sender, String pass, DOSThemeActivity dosThemeActivity){
        return MainCommunication.isClientAuthentic(sender, pass, dosThemeActivity);
    }

    public void newMessagesChecked(String newCheckedMessage){
        this.newCheckedMessage=newCheckedMessage;
    }

    public void newMessageReceived(Message message){
        messages.add(message);
    }

    public void disconnect(){
        communication.stopReceiving();
    }

    public void sendMessage(Message message){
        communication.sendMessage(message);
    }

    public void sendLogs(String logs){
        communication.sendLogs(logs);
    }

}
