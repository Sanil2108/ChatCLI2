package com.sanilk.chatcli2.encryption;


import com.sanilk.chatcli2.themes.dos.DOSThemeActivity;

public abstract class CustomMessage{
    public CUSTOM_MESSAGE custom_message;


    public enum CUSTOM_MESSAGE{
        //To print a normal message on the screen
        NORMAL_MESSAGE,
        //To print an encrypted message on the screen
        ENCRYPTED_MESSAGE,
        //Used by encrypted message thread to update the message
        UPDATE_UI
    }
}
