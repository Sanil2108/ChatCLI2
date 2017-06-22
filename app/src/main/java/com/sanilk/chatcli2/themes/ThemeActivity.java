package com.sanilk.chatcli2.themes;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Admin on 18-06-2017.
 */

public abstract class ThemeActivity extends Activity {
    ThemeComms themeComms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ThemeActivity(){

    }

    public void registerThemeComms(String user, String pass){
        themeComms=new ThemeComms(user, pass);
    }

    public void displayMessage(String message){
        System.out.println(message);
    }

    public void sendMessage(){

    }

    public void receiveMessage(){
    }
}
