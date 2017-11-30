package com.sanilk.chatcli2.communication;

import android.util.Log;

import com.sanilk.chatcli2.communication.response.sign_up.SignUpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Admin on 16-06-2017.
 */

public class ClientComm {
    private Client receiver;
    private Client sender;

    static ClientComm clientComm=new ClientComm();

    public static ClientComm getInstance(){
        return clientComm;
    }

    private ClientComm(){

    }

    public static void checkIfClientIsAuthentic(DataOutputStream dos, Client sender){
        try{
            String finalXML="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                    "<request>\n" +
                    "    <type>AUTHENTICATE</type>\n" +
                    "    <sender_nick>"+sender.getNick()+"</sender_nick>\n" +
                    "    <sender_password>"+sender.getPass()+"</sender_password>\n" +
                    "</request>";
            dos.writeUTF(finalXML);
            dos.flush();
            dos.close();
        }catch (Exception e){
            System.out.println("Error in clientcomm");
            e.printStackTrace();
        }
    }

    public void registerClientComm(Client sender){
        this.sender=sender;
    }

    public void registerClientComm(Client receiver, Client sender){
        this.receiver=receiver;
        this.sender=sender;
    }

    public void sendMessage(String message, DataOutputStream dos){
        if(message!=null && message!="" && message!="\n") {
            try {
                message += "\n";
                String finalMessage="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                        "<request>\n" +
                        "    <type>SEND</type>\n" +
                        "    <sender_nick>"+sender.getNick()+"</sender_nick>\n" +
                        "    <receiver_nick>"+receiver.nick+"</receiver_nick>\n" +
                        "    <sender_password>"+sender.getPass()+"</sender_password>\n" +
                        "    <message>"+message+"</message>\n" +
                        "</request>";
                dos.writeUTF(finalMessage);
                dos.flush();
                dos.close();
            } catch (IOException e) {
                System.out.println("Exception in ClientComm : ");
                e.printStackTrace();
            }
        }
    }

    public void sendLogs(String logs, DataOutputStream dos){
        try{
            dos.writeUTF(sender.nick+":SEND_LOG:::"+logs);
            dos.flush();
            dos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static SignUpResponse signUp(DataOutputStream dos, String nick, String pass, InputStream in){
        try{
            String finalXML="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                    "<request>\n" +
                    "    <type>SIGN_UP</type>\n" +
                    "    <sender_nick>"+nick+"</sender_nick>\n" +
                    "    <sender_password>"+pass+"</sender_password>\n" +
                    "</request>";
            dos.writeUTF(finalXML);

        }catch (Exception e){
            System.out.println("Exception in ClientComm : ");
            e.printStackTrace();
        }

        return null;
    }

    public void checkMessages(DataOutputStream dos, String[] senders){
        try{
//            String allSenders="";
//            for(int i=0;i<senders.length;i++){
//                allSenders+=senders[i]+";";
//            }
            String finalXML="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                    "<request>\n" +
                    "    <type>CHECK</type>\n" +
                    "    <sender_nick>"+sender.nick+"</sender_nick>\n" +
                    "    <receivers>\n" +
                    "        <receivers_count>"+senders.length+"</receivers_count>\n";
            for(String sender:senders){
                finalXML+=
                        "<receiver>\n" +
                        "   <receiver_name>"+sender+"</receiver_name>\n" +
                        "</receiver>\n";
            }
            finalXML+=
                    "    </receivers>\n" +
                    "    <sender_password>"+sender.getPass()+"</sender_password>\n" +
                    "</request>";

//            dos.writeUTF(sender.nick + ":SEND:" + "sanil2" + ":" + "abc");
            dos.writeUTF(finalXML);
            dos.flush();
            dos.close();
        }catch (Exception e){
            System.out.println("Exception in ClientComm : ");
            e.printStackTrace();
        }
    }


    public void receiveMessage(DataOutputStream dos){
        try{
            String finalXML="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                    "<request>\n" +
                    "    <type>RECEIVE</type>\n" +
                    "    <sender_nick>"+sender+"</sender_nick>\n" +
                    "    <receiver_nick>"+receiver+"</receiver_nick>\n" +
                    "    <sender_password>"+sender.getPass()+"</sender_password>\n" +
                    "</request>";
            dos.writeUTF(finalXML);
            dos.flush();
            dos.close();
        }catch (IOException e){
            System.out.println("Exception in ClientComm : ");
            e.printStackTrace();
        }
    }
}