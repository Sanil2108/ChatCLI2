package com.sanilk.chatcli2.themes.dos;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sanilk.chatcli2.R;
import com.sanilk.chatcli2.communication.Client;
import com.sanilk.chatcli2.databases.DatabaseHandlerForConnections;
import com.sanilk.chatcli2.themes.ThemeComms;

import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class DOSThemeActivity extends Activity {

    public enum MESSAGE_TYPE{
        SENT, RECEIVED, DEFAULT, ERROR
    }

    public LinearLayout selectedLinearLayout;

    boolean themeCommsRegistered=false;

    Context context;

    LinearLayout mainContainer;
    EditText cli;

    ThemeComms themeComms;

    Thread receiverThread;
    Handler uiHandler;

    ReceiverThreadRunnable receiverThreadRunnable;

    Button tempExecButton;

    TextView senderTextView;
    TextView messageTextView;

    DOSThemeActivity dosThemeActivity=this;

    //SQLite code
    DatabaseHandlerForConnections databaseHandlerForConnections;

    private static final boolean LOGGING=false;

    //Sender is the current user and receiver is the other guy.
    //I know, confusing
//    private final static String SENDER="sanil";
    private String currentReceiver="";

    //This is the client who is logged in at the time
    private Client senderClient;
    private boolean loggedIn=false;

    private boolean connected=false;

    //More variables for selection
    private static final boolean TEXT_COLOR_CHANGE_ON_SELECTION=true;
    private MESSAGE_TYPE selectedMessageType=null;
    private TextView selectedSenderTextView=null;
    private TextView selectedMessageTextView=null;

    private static final String LOGIN_FILE_NAME="LOGIN_INFO";
    private static final String LOGIN_FILE_USER_KEY="USER_NAME";
    private static final String LOGIN_FILE_PASS_KEY="USER_PASSWORD";

    private static final String LOG_FILE_NAME="LOG_FILE";
    private static final String LOG_FILE_KEY="LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dostheme);

        databaseHandlerForConnections=new DatabaseHandlerForConnections(this);

//        getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        context=this;

        mainContainer=(LinearLayout) findViewById(R.id.main_container);
        cli=(EditText)findViewById(R.id.dos_cli);
        tempExecButton=(Button)findViewById(R.id.temp_exec_button);

        uiHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                handleMessageAgain(msg);
            }
        };

        tempExecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                execButton();
            }
        });

        receiverThreadRunnable=new ReceiverThreadRunnable();
        receiverThread=new Thread(receiverThreadRunnable);
        receiverThread.start();

        selectedLinearLayout=null;

        Client oldClient = getLastLogin();
        if(oldClient!=null){
            senderClient=oldClient;
            loggedIn=true;
            themeComms=new ThemeComms(senderClient.getNick(), senderClient.getPass(), dosThemeActivity);
            displayMessage("You are logged in as "+senderClient.getNick(), MESSAGE_TYPE.DEFAULT);
        }

    }

    private void storeLastLogin(){
        String userName = senderClient.getNick();
        String password = senderClient.getPass();
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(LOGIN_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.putString(LOGIN_FILE_USER_KEY, userName);
        editor.putString(LOGIN_FILE_PASS_KEY, password);
        editor.commit();
    }

    private Client getLastLogin(){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(LOGIN_FILE_NAME, MODE_PRIVATE);
        String user=sharedPreferences.getString(LOGIN_FILE_USER_KEY, "");String pass=sharedPreferences.getString(LOGIN_FILE_PASS_KEY, "");
        Client client=new Client(user, pass);
        if(user=="" || pass==""){
            return null;
        }
        return client;
    }

    public void handleMessageAgain(Message msg){
        MessageTypeAndMessage messageTypeAndMessage = (MessageTypeAndMessage) msg.obj;
        final String message = messageTypeAndMessage.message;
        final MESSAGE_TYPE messageType = messageTypeAndMessage.messageType;

        senderTextView = new TextView(context);
        messageTextView = new TextView(context);

        if (message != null && message != "") {
            final LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(0, 0, 0, 0);
//                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(this));
            senderTextView.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
            messageTextView.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
            if (Build.VERSION.SDK_INT > 23) {
                switch (messageType) {
                    case DEFAULT:
                        senderTextView.setText("SYSTEM>");
                        senderTextView.setTextColor(getColor(R.color.DOSText));
                        messageTextView.setTextColor(getColor(R.color.DOSText));
                        break;
                    case RECEIVED:
                        senderTextView.setText(currentReceiver + ">");
                        senderTextView.setTextColor(getColor(R.color.DOSReceiver));
                        messageTextView.setTextColor(getColor(R.color.DOSReceiver));
                        break;
                    case SENT:
                        senderTextView.setText(senderClient.getNick() + ">");
                        senderTextView.setTextColor(getColor(R.color.DOSSender));
                        messageTextView.setTextColor(getColor(R.color.DOSSender));
                        break;
                    case ERROR:
                        senderTextView.setText("ERROR>");
                        senderTextView.setTextColor(getColor(R.color.DOSErrorText));
                        messageTextView.setTextColor(getColor(R.color.DOSErrorText));
                        senderTextView.setBackgroundColor(getColor(R.color.DOSErrorBackground));
                        messageTextView.setBackgroundColor(getColor(R.color.DOSErrorBackground));
                        break;
                }
            } else {
                switch (messageType) {
                    case DEFAULT:
                        senderTextView.setText("SYSTEM>");
                        senderTextView.setTextColor(getResources().getColor(R.color.DOSText));
                        messageTextView.setTextColor(getResources().getColor(R.color.DOSText));
                        break;
                    case RECEIVED:
                        senderTextView.setText(currentReceiver + ">");
                        senderTextView.setTextColor(getResources().getColor(R.color.DOSReceiver));
                        messageTextView.setTextColor(getResources().getColor(R.color.DOSReceiver));
                        break;
                    case SENT:
                        senderTextView.setText(senderClient.getNick() + ">");
                        senderTextView.setTextColor(getResources().getColor(R.color.DOSSender));
                        messageTextView.setTextColor(getResources().getColor(R.color.DOSSender));
                        break;
                    case ERROR:
                        senderTextView.setText("ERROR>");
                        senderTextView.setTextColor(getResources().getColor(R.color.DOSErrorText));
                        messageTextView.setTextColor(getResources().getColor(R.color.DOSErrorText));
                        senderTextView.setBackgroundColor(getResources().getColor(R.color.DOSErrorBackground));
                        messageTextView.setBackgroundColor(getResources().getColor(R.color.DOSErrorBackground));
                        break;
                }
            }
            senderTextView.setGravity(1);
            messageTextView.setGravity(3);
            messageTextView.setText(message);
            linearLayout.addView(senderTextView);
            linearLayout.addView(messageTextView);

            mainContainer.addView(linearLayout);

            //ERRORs can't be selected
            if(messageType!=MESSAGE_TYPE.ERROR && messageType!=MESSAGE_TYPE.DEFAULT) {
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newTextViewSelected(linearLayout, (TextView)linearLayout.getChildAt(0), (TextView)linearLayout.getChildAt(1), messageType);
                    }
                });
            }
        }

    }

    private void newTextViewSelected(LinearLayout linearLayout, TextView senderTextView, TextView messageTextView, MESSAGE_TYPE messageType){

        if(selectedLinearLayout!=null) {
            if (Build.VERSION.SDK_INT > 23) {
                selectedLinearLayout.setBackgroundColor(getColor(R.color.DOSBackground));
                switch (selectedMessageType) {
                    case RECEIVED:
                        selectedSenderTextView.setTextColor(getColor(R.color.DOSReceiver));
                        selectedMessageTextView.setTextColor(getColor(R.color.DOSReceiver));
                        break;
                    case SENT:
                        selectedSenderTextView.setTextColor(getColor(R.color.DOSSender));
                        selectedMessageTextView.setTextColor(getColor(R.color.DOSSender));
                        break;
                }
            } else {
                selectedLinearLayout.setBackgroundColor(getResources().getColor(R.color.DOSBackground));
                switch (selectedMessageType) {
                    case RECEIVED:
                        selectedSenderTextView.setTextColor(getResources().getColor(R.color.DOSReceiver));
                        selectedMessageTextView.setTextColor(getResources().getColor(R.color.DOSReceiver));
                        break;
                    case SENT:
                        selectedSenderTextView.setTextColor(getResources().getColor(R.color.DOSSender));
                        selectedMessageTextView.setTextColor(getResources().getColor(R.color.DOSSender));
                        break;
                }
            }
        }


        if(!linearLayout.equals(selectedLinearLayout)) {
            selectedLinearLayout = linearLayout;
            selectedMessageType=messageType;
            selectedMessageTextView=messageTextView;
            selectedSenderTextView=senderTextView;
            if (Build.VERSION.SDK_INT > 23) {
                if(TEXT_COLOR_CHANGE_ON_SELECTION){
                    senderTextView.setTextColor(getColor(R.color.DOSHighlightedText));
                    messageTextView.setTextColor(getColor(R.color.DOSHighlightedText));
                }
                linearLayout.setBackgroundColor(getColor(R.color.DOSHighlightedBackground));
            } else {
                if(TEXT_COLOR_CHANGE_ON_SELECTION){
                    senderTextView.setTextColor(getResources().getColor(R.color.DOSHighlightedText));
                    messageTextView.setTextColor(getResources().getColor(R.color.DOSHighlightedText));
                }
                linearLayout.setBackgroundColor(getResources().getColor(R.color.DOSHighlightedBackground));
            }
        }else {
            selectedLinearLayout=null;
            selectedMessageTextView=null;
            selectedSenderTextView=null;
            selectedMessageType=null;
        }
    }

    public void execButton(){
        String completeCommand = cli.getText().toString();
        cli.setText("");
        if(LOGGING) {
            Date currentDateAndTime = new Date();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(LOG_FILE_NAME, MODE_PRIVATE);
            String logString = sharedPreferences.getString(LOG_FILE_KEY, "") + "\n" + currentDateAndTime + "\t" + completeCommand + "\t" +
                    ((senderClient == null) ? "null" : senderClient.getNick()) + "\t" +
                    ((senderClient == null) ? "null" : senderClient.getPass()) + "\t" +
                    loggedIn + "\t" + themeCommsRegistered + "\t" + currentReceiver + "\t" + connected;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LOG_FILE_KEY, logString);
            editor.commit();
        }
        splitAndProcessCommand(completeCommand);
    }

    public void splitAndProcessCommand(String cmd){
//        if(cmd.toCharArray()[0] == '@'){
            //It is a command
        String[] cmds=cmd.split(" ");
        if(!cmds[0].equals("@login") && !cmds[0].equals("@signup") && !loggedIn){
            displayMessage("You are not logged in. Log in using @login or sign up using @signup", MESSAGE_TYPE.ERROR);
            return;
        }
        processCommand(cmds);
//        }else{
//            displayMessage("Invalid");
//        }
    }

    public void processCommand(String[] cmd){
        String receiver;
        switch (cmd[0]){
            case "@login":
                if(!themeComms.checkIfClientIsAuthentic(cmd[1], cmd[2])){
                    displayMessage("Login attempt failed", MESSAGE_TYPE.ERROR);
                }else{
                    if(cmd.length<3){
                        displayMessage("Wrong number of arguments", MESSAGE_TYPE.ERROR);
                    }else {
                        senderClient = new Client(cmd[1], cmd[2]);
                        loggedIn = true;
                        displayMessage("Successfully logged in as "+senderClient.getNick(), MESSAGE_TYPE.DEFAULT);
                        storeLastLogin();
                    }
                }
                break;
            case "@sendlog":
                if(!LOGGING){
                    displayMessage("Logging function is right now disabled.", MESSAGE_TYPE.ERROR);
                }
                SharedPreferences logSharedPreferences=getApplicationContext().getSharedPreferences(LOG_FILE_NAME, MODE_PRIVATE);
                String logs=logSharedPreferences.getString(LOG_FILE_KEY, "");
                SharedPreferences.Editor editor=logSharedPreferences.edit();
                editor.putString(LOG_FILE_KEY, "");
                editor.commit();
                if(!loggedIn){
                    displayMessage("You must be logged in.", MESSAGE_TYPE.ERROR);
                }else {
                    themeComms.sendLogs(logs);
                    displayMessage("Thanks! For submitting logs. This helps us improve the application and your overall experience", MESSAGE_TYPE.DEFAULT);
                }
                break;
            case "@logout":
                if(loggedIn){
                    if(themeComms!=null) {
                        themeComms.disconnect();
                    }
                    loggedIn=false;
                    senderClient=null;
                    displayMessage("Successfully logged out.", MESSAGE_TYPE.DEFAULT);
                }else{
                    displayMessage("You are not logged in", MESSAGE_TYPE.ERROR);
                }
                break;
            case "@signup":
                themeComms.signUpClient(cmd[1], cmd[2], this);
                displayMessage("Use @login [username] [password] to log in", MESSAGE_TYPE.DEFAULT);
                break;
            case "@conn":
                try {
                    if(connected){
                        displayMessage("breaking connection to " + currentReceiver + " ...", MESSAGE_TYPE.DEFAULT);
                        destroyConnection(senderClient.getNick(), currentReceiver);
                        displayMessage("Connection successfully broken", MESSAGE_TYPE.DEFAULT);
                        currentReceiver = "";
                        connected = false;
                    }
                    receiver = cmd[1];
                    displayMessage("Establishing connection to "+receiver+" ...", MESSAGE_TYPE.DEFAULT);
                    //maybe add a percentage thing here. would need delete message method which deletes the last message though
                    establishConnection(senderClient.getNick(), receiver);
                    connected=true;
                    currentReceiver=receiver;
                    displayMessage("Connection succesfully established", MESSAGE_TYPE.DEFAULT);

                    databaseHandlerForConnections.newConnection(senderClient.getNick(), currentReceiver);

                }catch (ArrayIndexOutOfBoundsException e){
                    displayMessage("ERROR, Array out of bounds exception, you sure you provided the name of the user you wanted to connect to ??", MESSAGE_TYPE.ERROR);
                }
                break;
            case "@disconn":
                if(!connected){
                    displayMessage("You are not connected to anyone.", MESSAGE_TYPE.ERROR);
                }else {
                    displayMessage("breaking connection to " + currentReceiver + " ...", MESSAGE_TYPE.DEFAULT);
                    destroyConnection(senderClient.getNick(), currentReceiver);
                    displayMessage("Connection successfully broken", MESSAGE_TYPE.DEFAULT);
                    currentReceiver = "";
                    connected = false;
                }
                break;
            case "@help":
                String[] help=getResources().getStringArray(R.array.help);
                for(int i=0;i<help.length;i+=2){
                    displayMessage(help[i]+" : "+help[i+1]+"\n", MESSAGE_TYPE.DEFAULT);
                }
                break;
            case "@help_advanced":
                String[] helpAdvanced=getResources().getStringArray(R.array.help_advanced);
                for(int i=0;i<helpAdvanced.length;i+=2){
                    displayMessage(helpAdvanced[i]+" : "+helpAdvanced[i+1]+"\n", MESSAGE_TYPE.DEFAULT);
                }
                break;
            case "@list":
                ArrayList<ContentValues> allUsers=databaseHandlerForConnections.getAllConnections();
                String finalString="";
                for(ContentValues contentValues:allUsers){
                    if(contentValues.get("user").equals(senderClient.getNick())){
                        finalString+=contentValues.get("sender")+"\t";
                    }
                }
                displayMessage(finalString, MESSAGE_TYPE.DEFAULT);
                break;
            case "@check":
                checkMessages();
                break;
            case "@ping":
                break;
            default:
                if(connected){
                    String message="";
                    for(int i=0;i<cmd.length;i++){
                        message+=cmd[i]+" ";
                    }
                    displayMessage(message, MESSAGE_TYPE.SENT);
                    sendMessage(message);
                }
        }

    }

    public void establishConnection(String sender, String receiver){
        registerThemeComms(sender, receiver);
    }

    public void destroyConnection(String sender, String receiver){
        //Un-registering theme comms and let the garbage collector do the work
        themeCommsRegistered=false;
        themeComms.disconnect();
        themeComms=null;
    }

    public void displayMessage(String message, MESSAGE_TYPE messageType) {
        Log.d("DOSThemeActivity", message);
        if(LOGGING) {
            Date currentDateAndTime = new Date();
            String messageTypeForLogFile = "N/A";
            switch (messageType) {
                case DEFAULT:
                    messageTypeForLogFile = "SYSTEM";
                    break;
                case ERROR:
                    messageTypeForLogFile = "ERROR";
                    break;
                case RECEIVED:
                    messageTypeForLogFile = "RECEIVED";
                    break;
                case SENT:
                    messageTypeForLogFile = "SENT";
                    break;
            }
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(LOG_FILE_NAME, MODE_PRIVATE);
            String logString = sharedPreferences.getString(LOG_FILE_KEY, "") + "\n" + currentDateAndTime + "\tFrom display message " + messageTypeForLogFile + ":" + message + "\t" +
                    ((senderClient == null) ? "null" : senderClient.getNick()) + "\t" +
                    ((senderClient == null) ? "null" : senderClient.getPass()) + "\t" +
                    loggedIn + "\t" + themeCommsRegistered + "\t" + currentReceiver + "\t" + connected;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LOG_FILE_KEY, logString);
            editor.commit();
        }
        receiverThreadRunnable.setMessage(message, messageType);
    }

    public void checkMessages(){
        if(themeComms==null) {
            themeComms = new ThemeComms(senderClient.getNick(), senderClient.getPass(), this);
        }
//        checkingThread.start();
        displayMessage(themeComms.checkMessages(senderClient.getNick(), databaseHandlerForConnections), MESSAGE_TYPE.DEFAULT);
//        displayMessage(themeComms.newCheckedMessage, MESSAGE_TYPE.DEFAULT);
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(i<100){
                    if(themeComms==null){
                        themeComms=new ThemeComms(senderClient.getNick(), senderClient.getPass(), dosThemeActivity);
                    }
                    String temp=themeComms.newCheckedMessage;
                    if(temp!="" && temp!=null){
                        displayMessage(temp, MESSAGE_TYPE.DEFAULT);
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    }catch (Exception e){

                    }
                }
            }
        });
        t.start();
    }

    public void sendMessage(String message) {
        themeComms.sendMessage(message);
    }

    public void receiveMessage(){
        if(themeCommsRegistered) {
            if(themeComms==null){
                themeComms=new ThemeComms(senderClient.getNick(), senderClient.getPass(), dosThemeActivity);
            }
            String message=themeComms.receiveMessages();
            if(message!="" && message!=null) {
                displayMessage(message, MESSAGE_TYPE.RECEIVED);
            }
        }
    }

    public void registerThemeComms(String user, String receiver) {
        themeComms=new ThemeComms(user, senderClient.getPass(), receiver, dosThemeActivity);
        themeCommsRegistered=true;
    }

    private class ReceiverThreadRunnable implements Runnable{
        final static String TAG="ReceiverThreadRunnable";

        String message="";
        MESSAGE_TYPE messageType;

        public void setMessage(String string, MESSAGE_TYPE messageType){
            message+=string;
            this.messageType=messageType;
        }

        @Override
        public void run() {
            while(true) {
                receiveMessage();
                if(message!="") {
                    try {
                        Thread.sleep(100);
                    }catch (Exception e){
                        Log.wtf(TAG, "Error in receiving messages");
                    }
                    Message msg = Message.obtain();
                    MessageTypeAndMessage messageTypeAndMessage = new MessageTypeAndMessage(messageType, message);
                    msg.obj = messageTypeAndMessage;
                    uiHandler.sendMessage(msg);
                    message = "";
                    receiveMessage();
                }
            }
        }
    }

    private class MessageTypeAndMessage{
        MESSAGE_TYPE messageType;
        String message;

        public MessageTypeAndMessage(MESSAGE_TYPE messageType, String message){
            this.message=message;
            this.messageType=messageType;
        }
    }
}
