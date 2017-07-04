package com.sanilk.chatcli2.themes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Admin on 18-06-2017.
 */

public abstract class ThemeActivity extends Activity {
    public LinearLayout selectedLinearLayout;
    ThemeComms themeComms;

    public enum MESSAGE_TYPE{
        SENT, RECEIVED, DEFAULT, ERROR
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ThemeActivity(){

    }

    public void registerThemeComms(String user, String pass){
        themeComms=new ThemeComms(user, pass);
    }

    public void displayMessage(String message, MESSAGE_TYPE message_type){
        System.out.println(message);
    }

    public void sendMessage(String message){

    }

    public void receiveMessage(){
    }

    public void checkMessages(){

    }
}
