package com.sanilk.chatcli2.communication;

import java.io.DataOutputStream;
import java.io.IOException;

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
            dos.writeUTF(sender.nick+":AUTHENTICATE:::"+sender.getPass());
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
        try {
            message += "\n";
            dos.writeUTF(sender.nick + ":SEND:" + receiver.nick + ":" + message + ":" + sender.getPass());
            dos.flush();
            dos.close();
        }catch (IOException e){
            System.out.println("Exception in ClientComm : ");
            e.printStackTrace();
        }
    }

    public static void signUp(DataOutputStream dos, String nick, String pass){
        try{
            dos.writeUTF(nick+":SIGN_UP:::"+pass);
            dos.flush();
            dos.close();
        }catch (Exception e){
            System.out.println("Exception in ClientComm : ");
            e.printStackTrace();
        }
    }

    public void checkMessages(DataOutputStream dos, String[] senders){
        try{
            String allSenders="";
            for(int i=0;i<senders.length;i++){
                allSenders+=senders[i]+";";
            }
//            dos.writeUTF(sender.nick + ":SEND:" + "sanil2" + ":" + "abc");
            dos.writeUTF(sender.nick+":CHECK::"+allSenders+":"+sender.getPass());
            dos.flush();
            dos.close();
        }catch (Exception e){
            System.out.println("Exception in ClientComm : ");
            e.printStackTrace();
        }
    }


    public void receiveMessage(DataOutputStream dos){
        try{
            dos.writeUTF(sender+":RECEIVE:"+receiver+"::"+sender.getPass());
            dos.flush();
            dos.close();
        }catch (IOException e){
            System.out.println("Exception in ClientComm : ");
            e.printStackTrace();
        }
    }
}