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

    public void registerClientComm(Client receiver, Client sender){
        this.receiver=receiver;
        this.sender=sender;
    }

    public void sendMessage(String message, DataOutputStream dos){
        try {
            message += "\n";
            dos.writeUTF(sender.nick + ":SEND:" + receiver.nick + ":" + message);
            dos.flush();
            dos.close();
        }catch (IOException e){
            System.out.println("Exception in ClientComm : ");
            e.printStackTrace();
        }
    }

    public void receiveMessage(DataOutputStream dos){
        try{
            dos.writeUTF(sender+":RECEIVE:");
            dos.flush();
            dos.close();
        }catch (IOException e){
            System.out.println("Exception in ClientComm : ");
            e.printStackTrace();
        }
    }
}