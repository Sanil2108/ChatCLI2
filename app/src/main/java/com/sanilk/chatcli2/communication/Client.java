package com.sanilk.chatcli2.communication;

/**
 * Created by Admin on 16-06-2017.
 */

public class Client {
    String nick;
    String pass;

    Client(String nick, String pass){
        this.nick=nick;
        this.pass=pass;
    }

    @Override
    public String toString(){
        return nick;
    }
}
