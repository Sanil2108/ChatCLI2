package com.sanilk.chatcli2.communication;

import android.util.Log;
import android.widget.Toast;

import com.sanilk.chatcli2.MainActivity;
import com.sanilk.chatcli2.communication.response.XMLParser;
import com.sanilk.chatcli2.communication.response.authenticate.AuthenticateResponse;
import com.sanilk.chatcli2.communication.response.receive.ReceiveResponse;
import com.sanilk.chatcli2.communication.response.send.SendResponse;
import com.sanilk.chatcli2.communication.response.sign_up.SignUpResponse;
import com.sanilk.chatcli2.database.Entities.Message;
import com.sanilk.chatcli2.themes.ThemeComms;
import com.sanilk.chatcli2.themes.dos.DOSThemeActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Admin on 16-06-2017.
 */

public class MainCommunication {

//    private static final String ADDRESS="https://20171028t065813-dot-chat-cli.appspot.com/Servlet1";
//    private static final String ADDRESS="http://10.0.2.2:8080/Servlet1";
    private static final String ADDRESS="https://chatcli.herokuapp.com/";
    private static final String DEFAULT_PASSWORD="root";

    Client receiver;
    Client sender;

    static Thread signUpThread;
    Thread inputThread;
    Thread logSenderThread;
    Thread outputThread;
    Thread checkThread;

    ClientComm clientComm;

    static URL url;

    LogSenderHandler logSenderHandler;
    CheckHandler checkHandler;
    SenderHandler senderHandler;
    ReceiverHandler receiverHandler;
    static SignUpHandler signUpHandler;

    ThemeComms themeComms;

    private boolean receiving=false;

    private boolean stopReceiving=false;

    //This constructor is used only for checking messages
    public MainCommunication(String sender, String password, ThemeComms themeComms){
        this.sender=new Client(sender, password);
        this.themeComms=themeComms;
        try {
            url = new URL(ADDRESS);
            clientComm = ClientComm.getInstance();

            checkHandler=new CheckHandler();
            checkThread=new Thread(checkHandler);
            checkThread.start();

//            senderHandler=new SenderHandler();
//            inputThread = new Thread(senderHandler);
//            inputThread.start();

            clientComm.registerClientComm(this.sender);

        }catch (Exception e){
            System.out.println("Exception in MainCommunication");
            e.printStackTrace();
        }
    }

    public MainCommunication(String sender, String password, String receiver, ThemeComms themeComms){
        this.themeComms=themeComms;

        this.receiver=new Client(receiver, password);
        this.sender=new Client(sender, password);

        try {
            url = new URL(ADDRESS);
            clientComm = ClientComm.getInstance();

            senderHandler=new SenderHandler(themeComms.dosThemeActivity);
            inputThread = new Thread(senderHandler);
            inputThread.start();
            receiverHandler=new ReceiverHandler();

            clientComm.registerClientComm(this.receiver, this.sender);

            outputThread=new Thread(receiverHandler);

            checkHandler=new CheckHandler();
            checkThread=new Thread(checkHandler);
            checkThread.start();


        }catch (Exception e){
            System.out.println("Exception in MainCommunication");
            e.printStackTrace();
        }
    }

    public void sendLogs(String logs){
        logSenderHandler=new LogSenderHandler(logs);
        logSenderThread=new Thread(logSenderHandler);
        logSenderThread.start();
    }

    public static void signUpClient(String nick, String pass, DOSThemeActivity dosThemeActivity){
        signUpHandler=new SignUpHandler(nick, pass, dosThemeActivity);

        signUpThread=new Thread(signUpHandler);
        signUpThread.start();
    }

    static boolean authentic;
    static boolean authenticatingThreadFinished=false;
    public static boolean isClientAuthentic(final String sender, final String pass, final DOSThemeActivity dosThemeActivity){

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                if(isClientAuthenticFromInnerClass(sender, pass, dosThemeActivity)){
                    authentic=true;
                }else{
                    authentic=false;
                }
            }
        });
        authenticatingThreadFinished=false;
        t.start();
        while(!authenticatingThreadFinished) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return authentic;
    }

    private static boolean isClientAuthenticFromInnerClass(String sender, String pass, DOSThemeActivity dosThemeActivity){
        String finalMessage="";
        try {
            if(url==null){
                url=new URL(ADDRESS);
            }
            HttpURLConnection conn5 = (HttpURLConnection) url.openConnection();
            conn5.setRequestMethod("POST");
            conn5.setDoInput(true);
            conn5.setDoOutput(true);

            DataOutputStream dos=new DataOutputStream(conn5.getOutputStream());
            ClientComm.checkIfClientIsAuthentic(dos, new Client(sender, pass));
            dos.flush();
            dos.close();

            DataInputStream din=new DataInputStream(conn5.getInputStream());
            String response=din.readUTF();
            AuthenticateResponse authenticateResponse=XMLParser.parseAuthenticateResponse(response);

            din.close();
            conn5.disconnect();
            authenticatingThreadFinished=true;

            if(authenticateResponse.isAuthentic()){
                dosThemeActivity.displayMessage("You are now logged in. ", DOSThemeActivity.MESSAGE_TYPE.DEFAULT);
                return true;
            }else{
                dosThemeActivity.displayMessage("Couldn't log you in. Error code - "+authenticateResponse.getErrorCode()+
                        "\nError details - "+authenticateResponse.getErrorDetails(), DOSThemeActivity.MESSAGE_TYPE.ERROR);
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }


    public void startReceiving(){
        if(!receiving) {
            receiving = true;
            outputThread.start();
        }
    }

    public void stopReceiving(){
        stopReceiving=true;
    }

    public void sendMessage(final Message message){
//        if(inputThread.isAlive()){
//            inputThread.
//        }
        senderHandler.newMessage(message);
    }

    public void checkMessages(String[] allSenders){
        checkHandler.startChecking(allSenders);
    }

    public static class LoginHandler implements Runnable{
        static final int MILLISECONDS=2000;

        String nick="";
        String pass="";
        DOSThemeActivity dosThemeActivity;

        public LoginHandler(String nick, String pass, DOSThemeActivity dosThemeActivity){
            this.nick=nick;
            this.pass=pass;
            this.dosThemeActivity=dosThemeActivity;
        }

        @Override
        public void run(){
            try{

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public static class AuthenticationHandler implements Runnable{
        static final int MILLISECONDS=2000;

        String nick="";
        String pass="";
        DOSThemeActivity dosThemeActivity;

        public AuthenticationHandler(String nick, String pass, DOSThemeActivity dosThemeActivity, ThemeComms comms){
            this.nick=nick;
            this.pass=pass;
            this.dosThemeActivity=dosThemeActivity;
        }

        @Override
        public void run(){
            while(true) {
                try {

                    Thread.sleep(MILLISECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class SignUpHandler implements Runnable{
        static final int MILLISECONDS=2000;

        String nick=null;
        String pass=null;
        DOSThemeActivity dosThemeActivity;

        public SignUpHandler(String nick, String pass, DOSThemeActivity dosThemeActivity){
            this.nick=nick;
            this.pass=pass;
            this.dosThemeActivity=dosThemeActivity;
        }

        @Override
        public void run(){
            try{
//                while(true) {
                if (url == null) {
                    url = new URL(ADDRESS);
                }
                HttpURLConnection conn4 = (HttpURLConnection) url.openConnection();
                conn4.setRequestMethod("POST");
                conn4.setDoInput(true);
//                conn4.setReadTimeout(2000);
                conn4.setDoOutput(true);
                conn4.connect();
                DataOutputStream dos = null;

                try {
                    dos = new DataOutputStream(conn4.getOutputStream());
                } catch (ConnectException e) {
                    e.printStackTrace();
                }

                //basically check if this response object has the field successful as true or not
                //if it does, break out of the while loop
//                    InputStream in = conn4.getInputStream();

                ClientComm.signUp(dos, nick, pass, null);

                dos.flush();
                dos.close();

                String response="";
                InputStream in = conn4.getInputStream();
                DataInputStream din=new DataInputStream(in);
                response=din.readUTF();
                in.close();

                conn4.disconnect();

                SignUpResponse signUpResponse= XMLParser.parseSignUpResponse(response);
                if(signUpResponse.isSuccessful()){
                    dosThemeActivity.displayMessage("Successfully signed up!", DOSThemeActivity.MESSAGE_TYPE.DEFAULT);
                }else{
                    dosThemeActivity.displayMessage("Couldn't sign you up. Error code - "+signUpResponse.getErrorCode()
                            +"\nError details - "+signUpResponse.getErrorDetails(), DOSThemeActivity.MESSAGE_TYPE.ERROR);
                }

                try{
                    Thread.sleep(MILLISECONDS);
                }catch (Exception e){
                    e.printStackTrace();
                }

//                }

            }catch (Exception e){
                Log.d("MainCommunication", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    private class ReceiverHandler implements Runnable{
        private static final long MILLISECONDS=1000;

        @Override
        public void run() {
            while(true){
                try{
                    if(stopReceiving){
                        stopReceiving=false;
                        break;
                    }
                    Thread.sleep(MILLISECONDS);

                    HttpURLConnection conn2=(HttpURLConnection)url.openConnection();
                    conn2.setRequestMethod("POST");
                    conn2.setDoOutput(true);
                    conn2.setDoInput(true);

                    //Reading from the Servlet
                    DataOutputStream dos=new DataOutputStream(conn2.getOutputStream());
                    clientComm.receiveMessage(dos);

                    dos.flush();
                    dos.close();

                    DataInputStream din=new DataInputStream(conn2.getInputStream());
                    String response=din.readUTF();

                    ReceiveResponse receiveResponse=XMLParser.parseReceiveResponse(response);

                    ArrayList<Message> messages=receiveResponse.messages;
                    for(Message message:messages){
                        themeComms.newMessageReceived(message);
                    }

                    din.close();

                    conn2.disconnect();

                }catch(Exception e){
                    System.out.println("Exception in inputThread");
                    e.printStackTrace();
                }
            }
        }

    }

    private class CheckHandler implements Runnable{
        String[] allSenders;
        boolean checking=false;

        public CheckHandler(){

        }

        public void startChecking(String[] allSenders){
            this.allSenders=allSenders;
            checking=true;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(1000);
                    if (checking) {
                        HttpURLConnection conn3 = (HttpURLConnection) url.openConnection();
                        conn3.setRequestMethod("POST");
                        conn3.setDoOutput(true);
                        conn3.setDoInput(true);
//                    conn3.setReadTimeout(1000);

                        //Writing to the servlet
                        DataOutputStream dos = new DataOutputStream(conn3.getOutputStream());

                        clientComm.checkMessages(dos, allSenders);

                        dos.flush();
                        dos.close();

                        int status=conn3.getResponseCode();
                        if(status != HttpURLConnection.HTTP_OK){
                            Log.d("MainCommunication", "Something wrong");
                        }

                        InputStream in = conn3.getInputStream();
                        int c;
                        String out="";
                        while ((c = in.read()) != -1) {
                            System.out.print((char) c);
                            out+=((char)c)+"";
                        }
                        in.close();
                        Log.d("MainCommunications", out);

                        if(out!="" && out!=null){
                            themeComms.newMessagesChecked(out);
                        }

                        conn3.disconnect();

                        //Now, start receiving
//                        Thread.sleep(1000);
//
//                        HttpURLConnection conn2=(HttpURLConnection)url.openConnection();
//                        conn2.setRequestMethod("POST");
//                        conn2.setDoOutput(true);
//                        conn2.setDoInput(true);
//
//                        //Reading from the Servlet
//                        DataOutputStream dos2=new DataOutputStream(conn2.getOutputStream());
//                        clientComm.receiveMessage(dos2);
//
//                        InputStream in2=conn2.getInputStream();
//                        String message="";
//                        int c2;
//                        while((c2=in2.read())!=-1){
//                            System.out.print((char)c2);
//                            message+=(char)c2;
//                        }
//
//                        if(message!="") {
//                            themeComms.newMessageReceived(message);
//                        }
//
//                        dos2.flush();
//                        dos2.close();
//                        in2.close();
//
//                        conn2.disconnect();
                    }
                } catch (Exception e) {
                    System.out.println("Exception in checkThread");
                    e.printStackTrace();
                }
                checking=false;
            }
        }

    }

    private class LogSenderHandler implements Runnable{
        private String logs="";
        LogSenderHandler(String logs){
            this.logs=logs;
        }
        @Override
        public void run(){
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                //Writing to the servlet
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                clientComm.sendLogs(logs, dos);

                dos.flush();
                dos.close();

                InputStream in = conn.getInputStream();
                int c;
                while ((c = in.read()) != -1) {
                    System.out.print((char) c);
                }
                in.close();

                conn.disconnect();

            } catch (Exception e) {
                System.out.println("Exception in inputThread");
                e.printStackTrace();
            }
        }
    }

    private class SenderHandler implements Runnable{
        ArrayList<Message> messages;
        DOSThemeActivity dosThemeActivity;

        public SenderHandler(DOSThemeActivity dosThemeActivity){
            this.dosThemeActivity=dosThemeActivity;
            messages=new ArrayList<>();
        }

        public void newMessage(Message message){
            this.messages.add(message);
        }

        @Override
        public void run(){
            while(true) {
                try {
                    if(messages.size()!=0 && messages!=null) {

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        //Writing to the servlet
                        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                        clientComm.sendMessage(messages, dos);

                        dos.flush();
                        dos.close();

                        DataInputStream din=new DataInputStream(conn.getInputStream());
                        String response=din.readUTF();
                        SendResponse sendResponse=XMLParser.parseSendResponse(response);
                        if(sendResponse.isSuccessful()){
                            messages.clear();
                        }
                        din.close();

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
