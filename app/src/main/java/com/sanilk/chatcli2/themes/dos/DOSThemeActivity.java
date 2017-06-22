package com.sanilk.chatcli2.themes.dos;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sanilk.chatcli2.R;
import com.sanilk.chatcli2.themes.ThemeActivity;
import com.sanilk.chatcli2.themes.ThemeComms;

public class DOSThemeActivity extends ThemeActivity {
    private final static String DEFAULT_PASSWORD="root";

    EditText usernameEditText;
    EditText messageEditText;
    EditText receiverEditText;

    Button sendButton;
//    Button receiveButton;

    TextView messageTextView;

    ThemeComms themeComms;

    Thread receiverThread;
    Handler uiHandler;

    ReceiverThreadRunnable receiverThreadRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dostheme);

        receiverEditText=(EditText)findViewById(R.id.dos_receiver);
        usernameEditText=(EditText)findViewById(R.id.dos_user);
        messageEditText=(EditText)findViewById(R.id.dos_message_edit);
        sendButton=(Button)findViewById(R.id.dos_send);
//        receiveButton=(Button)findViewById(R.id.receiverButton);
        messageTextView=(TextView)findViewById(R.id.dos_message);

//        receiveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                receiveMessage();
//            }
//        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        registerThemeComms(usernameEditText.getText().toString(), receiverEditText.getText().toString());

        uiHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                String message=(String)msg.obj;
                messageTextView.setText(message);
            }
        };

        receiverThreadRunnable=new ReceiverThreadRunnable();
        receiverThread=new Thread(receiverThreadRunnable);
        receiverThread.start();

    }

    @Override
    public void displayMessage(String message) {
        super.displayMessage(message);
        receiverThreadRunnable.setMessage(message);
    }

    @Override
    public void sendMessage() {
        themeComms.sendMessage(messageEditText.getText().toString());
    }

    @Override
    public void receiveMessage(){
        displayMessage(themeComms.receiveMessages());
    }

    @Override
    public void registerThemeComms(String user, String receiver) {
        themeComms=new ThemeComms(user, receiver);
    }

    private class ReceiverThreadRunnable implements Runnable{
        String message="";

        public void setMessage(String string){
            message+=string;
        }

        @Override
        public void run() {
            while(true) {
                receiveMessage();
                Message msg=Message.obtain();
                msg.obj=message;
                uiHandler.sendMessage(msg);
            }
        }
    }
}
