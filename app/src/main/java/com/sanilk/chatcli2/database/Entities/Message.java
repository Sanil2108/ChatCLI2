package com.sanilk.chatcli2.database.Entities;

/**
 * Created by sanil on 9/12/17.
 */

public class Message {
    public String timeAndDate;
    public String contents;
    public int encryptDuration;

    public Message(String contents, String timeAndDate, int encryptDuration){
        this.timeAndDate=timeAndDate;
        this.contents=contents;
        this.encryptDuration=encryptDuration;
    }

    public Message(String contents, int encryptDuration){
        this.encryptDuration=encryptDuration;
        this.contents=contents;
    }
}
