package com.sanilk.chatcli2.themes.dos;

import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sanilk.chatcli2.R;
import com.sanilk.chatcli2.themes.ThemeActivity;
import com.sanilk.chatcli2.themes.ThemeComms;

import java.util.ArrayList;
import java.util.HashMap;

public class DOSThemeActivity extends ThemeActivity {

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


    //Sender is the current user and receiver is the other guy.
    //I know, confusing
    private final static String SENDER="sanil";
    private String currentReceiver="";

    private boolean connected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dostheme);

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

    }

    public void handleMessageAgain(Message msg){
        MessageTypeAndMessage messageTypeAndMessage=(MessageTypeAndMessage)msg.obj;
        String message=messageTypeAndMessage.message;
        MESSAGE_TYPE messageType=messageTypeAndMessage.messageType;

        senderTextView = new TextView(context);
        messageTextView = new TextView(context);

        if(message!=null && message!="") {
            LinearLayout linearLayout=new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(0,0,0,0);
//                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(this));
            senderTextView.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
            messageTextView.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
            if(Build.VERSION.SDK_INT>23) {
                switch (messageType){
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
                        senderTextView.setText(SENDER + ">");
                        senderTextView.setTextColor(getColor(R.color.DOSSender));
                        messageTextView.setTextColor(getColor(R.color.DOSSender));
                        break;
                }
            }else{
                switch (messageType){
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
                        senderTextView.setText(SENDER + ">");
                        senderTextView.setTextColor(getResources().getColor(R.color.DOSSender));
                        messageTextView.setTextColor(getResources().getColor(R.color.DOSSender));
                        break;
                }
            }
            senderTextView.setGravity(1);
            messageTextView.setGravity(3);
            messageTextView.setText(message);
            linearLayout.addView(senderTextView);
            linearLayout.addView(messageTextView);
            mainContainer.addView(linearLayout);
        }
    }

    public void execButton(){
        String completeCommand = cli.getText().toString();
        cli.setText("");
        splitAndProcessCommand(completeCommand);
    }

    public void splitAndProcessCommand(String cmd){
//        if(cmd.toCharArray()[0] == '@'){
            //It is a command
            String[] cmds=cmd.split(" ");
            processCommand(cmds);
//        }else{
//            displayMessage("Invalid");
//        }
    }

    public void processCommand(String[] cmd){
        String receiver;
        switch (cmd[0]){
            case "@conn":
                try {
                    receiver = cmd[1];
                    displayMessage("Establishing connection to "+receiver+" ...", MESSAGE_TYPE.DEFAULT);
                    //maybe add a percentage thing here. would need delete message method which deletes the last message though
                    establishConnection(SENDER, receiver);
                    connected=true;
                    currentReceiver=receiver;
                    displayMessage("\nConnection succesfully established", MESSAGE_TYPE.DEFAULT);
                }catch (ArrayIndexOutOfBoundsException e){
                    displayMessage("ERROR, Array out of bounds exception, you sure you provided the name of the user you wanted to connect to ??", MESSAGE_TYPE.DEFAULT);
                }
                break;
            case "@disconn":
                try {
                    displayMessage("breaking connection to " + currentReceiver + " ...", MESSAGE_TYPE.DEFAULT);
                    destroyConnection(SENDER, currentReceiver);
                    displayMessage("Connection succesfully broken", MESSAGE_TYPE.DEFAULT);
                }catch (ArrayIndexOutOfBoundsException e){
                    displayMessage("ERROR, Array out of bounds exception, you sure you provided the name of the user you wanted to disconnect to ??", MESSAGE_TYPE.DEFAULT);
                }
                currentReceiver="";
                connected=false;
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
            case "@logout":
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
        themeComms=null;
    }

    @Override
    public void displayMessage(String message, ThemeActivity.MESSAGE_TYPE messageType) {
        Log.d("DOSThemeActivity", message);
        receiverThreadRunnable.setMessage(message, messageType);
    }

    @Override
    public void sendMessage(String message) {
        themeComms.sendMessage(message);
    }

    @Override
    public void receiveMessage(){
        if(themeCommsRegistered) {
            displayMessage(themeComms.receiveMessages(), MESSAGE_TYPE.RECEIVED);
        }
    }

    @Override
    public void registerThemeComms(String user, String receiver) {
        themeComms=new ThemeComms(user, receiver);
        themeCommsRegistered=true;
    }

    private class ReceiverThreadRunnable implements Runnable{
        String message="";
        ThemeActivity.MESSAGE_TYPE messageType;

        public void setMessage(String string, ThemeActivity.MESSAGE_TYPE messageType){
            message+=string;
            this.messageType=messageType;
        }

        @Override
        public void run() {
            while(true) {
                Message msg=Message.obtain();
                MessageTypeAndMessage messageTypeAndMessage=new MessageTypeAndMessage(messageType, message);
                msg.obj=messageTypeAndMessage;
                uiHandler.sendMessage(msg);
                message="";
                receiveMessage();
                try {
                    Thread.sleep(500);
                }catch (Exception e){

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
