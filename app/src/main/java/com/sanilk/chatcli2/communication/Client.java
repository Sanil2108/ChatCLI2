package com.sanilk.chatcli2.communication;

/**
 * Created by Admin on 16-06-2017.
 */

public class Client {
    String nick;
    private String pass;

    public String getNick() {
        return nick;
    }

    public Client(String nick, String pass){
        this.nick=nick;
        this.pass=pass;
    }

    public String getPass() {
        return pass;
    }

    @Override
    public String toString(){
        return nick;
    }
}
