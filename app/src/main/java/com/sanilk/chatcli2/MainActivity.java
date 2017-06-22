package com.sanilk.chatcli2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sanilk.chatcli2.communication.Client;
import com.sanilk.chatcli2.communication.MainCommunication;
import com.sanilk.chatcli2.themes.dos.DOSThemeActivity;

public class MainActivity extends Activity {
    Button sendButton;
    Button receiverButton;

    EditText usernameEditText;
    EditText messageEditText;
    EditText receiverEditText;

    MainCommunication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=new Intent(this, new DOSThemeActivity().getClass());
        this.startActivity(intent);

//        usernameEditText=(EditText)findViewById(R.id.usernameEditText);
//        messageEditText=(EditText)findViewById(R.id.messageEditText);
//        receiverEditText=(EditText)findViewById(R.id.receiverEditText);
//
//        sendButton=(Button)findViewById(R.id.sendButton);
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String message=messageEditText.getText().toString();
//                String username=usernameEditText.getText().toString();
//                String receiver=receiverEditText.getText().toString();
//
//                communication=new MainCommunication(username, receiver, getMainActivity());
////                System.out.println();
//                communication.sendMessage(message);
//
//            }
//        });
//
//        receiverButton=(Button)findViewById(R.id.receiverButton);
//        receiverButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                communication.startReceiving();
//            }
//        });
    }

    private MainActivity getMainActivity(){
        return this;
    }

    public void newMessageReceived(String message){
        //I have to call this from the main thread instead of a helper thread
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        System.out.println(message);
    }
}
