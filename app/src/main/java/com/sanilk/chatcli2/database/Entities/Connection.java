package com.sanilk.chatcli2.database.Entities;

/**
 * Created by sanil on 9/12/17.
 */

public class Connection {
    public User clientUser;
    public User connectedUser;

    public Connection(User clientUser, User connectedUser){
        this.connectedUser=connectedUser;
        this.clientUser=clientUser;
    }
}
