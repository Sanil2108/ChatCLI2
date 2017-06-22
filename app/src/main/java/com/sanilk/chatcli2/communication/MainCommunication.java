package com.sanilk.chatcli2.communication;

import com.sanilk.chatcli2.MainActivity;
import com.sanilk.chatcli2.themes.ThemeComms;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 16-06-2017.
 */

public class MainCommunication {

    private static final String ADDRESS="http://10.0.2.2:9999/Test1/Servlet1";
    private static final String DEFAULT_PASSWORD="raymon11";

    Client receiver;
    Client sender;

    Thread inputThread;
    Thread outputThread;

    ClientComm clientComm;

    URL url;

    SenderHandler senderHandler;
    ReceiverHandler receiverHandler;

    ThemeComms themeComms;

    private boolean receiving=false;

    public MainCommunication(String sender, String receiver, ThemeComms themeComms){
        this.themeComms=themeComms;

        this.receiver=new Client(receiver, DEFAULT_PASSWORD);
        this.sender=new Client(sender, DEFAULT_PASSWORD);

        try {
            url = new URL(ADDRESS);
            clientComm = ClientComm.getInstance();

            senderHandler=new SenderHandler();
            inputThread = new Thread(senderHandler);
            inputThread.start();
            receiverHandler=new ReceiverHandler();

            clientComm.registerClientComm(this.receiver, this.sender);

            outputThread=new Thread(receiverHandler);

        }catch (Exception e){
            System.out.println("Exception in MainCommunication");
            e.printStackTrace();
        }
    }

    public void startReceiving(){
        if(!receiving) {
            outputThread.start();
            receiving = true;
        }
    }

    public void sendMessage(final String message){
//        if(inputThread.isAlive()){
//            inputThread.
//        }
        senderHandler.newMessage(message);
    }

    private class ReceiverHandler implements Runnable{
        private static final long MILLISECONDS=1000;

        @Override
        public void run() {
            while(true){
                try{
                    Thread.sleep(MILLISECONDS);

                    HttpURLConnection conn2=(HttpURLConnection)url.openConnection();
                    conn2.setRequestMethod("POST");
                    conn2.setDoOutput(true);
                    conn2.setDoInput(true);

                    //Reading from the Servlet
                    DataOutputStream dos=new DataOutputStream(conn2.getOutputStream());
                    clientComm.receiveMessage(dos);

                    InputStream in=conn2.getInputStream();
                    String message="";
                    int c;
                    while((c=in.read())!=-1){
                        System.out.print((char)c);
                        message+=(char)c;
                    }

                    if(message!="") {
                        themeComms.newMessageReceived(message);
                    }

                    dos.flush();
                    dos.close();
                    in.close();

                    conn2.disconnect();

                }catch(Exception e){
                    System.out.println("Exception in inputThread");
                    e.printStackTrace();
                }
            }
        }

    }

    private class SenderHandler implements Runnable{
        String message;

        public void newMessage(String message){
            this.message=message;
        }

        @Override
        public void run(){
            while(true) {
                try {
                    if(message!="") {

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        //Writing to the servlet
                        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                        String msg = message;
                        clientComm.sendMessage(msg, dos);

                        message = "";

                        dos.flush();
                        dos.close();

                        InputStream in = conn.getInputStream();
                        int c;
                        while ((c = in.read()) != -1) {
                            System.out.print((char) c);
                        }
                        in.close();

                        conn.disconnect();
                    }

                } catch (Exception e) {
                    System.out.println("Exception in inputThread");
                    e.printStackTrace();
                }
            }

        }

    }

}
