package com.sanilk.chatcli2.themes.dos;

import android.content.Context;
import android.graphics.Color;
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

    private final static String SENDER="sanil";

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
                String message=(String)msg.obj;
                if(message!=null && message!="") {
                    TextView newTextView = new TextView(context);
                    newTextView.setText(message);
                    newTextView.setTextColor(Color.WHITE);
                    mainContainer.addView(newTextView);
                }
            }
        };

        tempExecButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String completeCommand = cli.getText().toString();
                splitAndProcessCommand(completeCommand);
            }
        });

        receiverThreadRunnable=new ReceiverThreadRunnable();
        receiverThread=new Thread(receiverThreadRunnable);
        receiverThread.start();

    }

    public void splitAndProcessCommand(String cmd){
        if(cmd.toCharArray()[0] == '@'){
            //It is a command
            String[] cmds=cmd.split(" ");
            processCommand(cmds);
        }else{
            displayMessage("Invalid");
        }
    }

    public void processCommand(String[] cmd){
        switch (cmd[0]){
            case "@conn":
                String receiver = cmd[1];
                displayMessage("Establishing connection to "+receiver+" ...");
                //maybe add a percentage thing here. would need delete message method which deletes the last message though
                establishConnection(SENDER, receiver);
                break;
            case "@disconn":
                break;
        }
    }

    public void establishConnection(String sender, String receiver){
        registerThemeComms(sender, receiver);
    }

    @Override
    public void displayMessage(String message) {
        receiverThreadRunnable.setMessage(message);
    }

    @Override
    public void sendMessage() {
//        themeComms.sendMessage(messageEditText.getText().toString());
    }

    @Override
    public void receiveMessage(){
        if(themeCommsRegistered) {
            displayMessage(themeComms.receiveMessages());
        }
    }

    @Override
    public void registerThemeComms(String user, String receiver) {
        themeComms=new ThemeComms(user, receiver);
        themeCommsRegistered=true;
    }

    private class ReceiverThreadRunnable implements Runnable{
        String message="";

        public void setMessage(String string){
            message+=string;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(500);
                }catch (Exception e){

                }
                receiveMessage();
                Message msg=Message.obtain();
                msg.obj=message;
                uiHandler.sendMessage(msg);
                message="";
            }
        }
    }
}
