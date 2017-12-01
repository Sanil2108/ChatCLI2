package com.sanilk.chatcli2.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 29-06-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="MAIN";
    private static final int DB_VERSION=6;

    //Connections table
    protected static final String CONNECTIONS_TABLE_NAME="CONNECTIONS_TABLE";
    private static final String[] CONNECTIONS_TABLE_COLUMNS={
            "username text", "sendername text"
    };
    protected static final String[] CONNECTIONS_TABLE_COLUMNS_NAMES={
            "username","sendername"
    };

    //Users table
    public static final String USERS_TABLE_NAME="USERS";
    public static final String[] USERS_TABLE_COLUMNS={
            "nick TEXT PRIMARY KEY", "numberOfMessagesRecd INTEGER", "numberOfMessagesSent INTEGER"
    };
    public static final String[] USERS_TABLE_COLUMNS_NAMES={
            "nick", "numberOfMessagesRecd", "numberOfMessagesSent"
    };

    //MessageByUsers table
    public static final String MESSAGE_BY_USERS_TABLE_NAME="MessageByUser";
    public static final String[] MESSAGE_BY_USERS_TABLE_COLUMNS={
            "messageByUserId INTEGER PRIMARY KEY AUTOINCREMENT", "nick TEXT", "messageId INTEGER" ,
            "FOREIGN KEY(messageId) REFERENCES Message(messageId)", "FOREIGN KEY(nick) references User(nick)"
    };
    public static final String[] MESSAGE_BY_USERS_TABLE_COLUMNS_NAMES={
            "messageByUserId", "nick", "messageId"
    };

    //Messages table
    public static final String MESSAGES_TABLE_NAME="Message";
    public static final String[] MESSAGES_TABLE_COLUMNS={
            "messageId INTEGER PRIMARY KEY AUTOINCREMENT",
            "contents TEXT",
            "timeAndDate TEXT",
            "encryptionDuration"
    };
    public static final String[] MESSAGES_TABLE_COLUMNS_NAMES={
            "messageId",
            "contents",
            "timeAndDate",
            "encryptionDuration"
    };


    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createConnectionsTable(sqLiteDatabase);
        createUserTable(sqLiteDatabase);
        createMessagesTable(sqLiteDatabase);
        createMessageByUserTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + i + " to "
                        + i1 + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+CONNECTIONS_TABLE_NAME+";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+USERS_TABLE_NAME+";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+MESSAGES_TABLE_NAME+";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+MESSAGE_BY_USERS_TABLE_NAME+";");
        onCreate(sqLiteDatabase);
    }

    private void createUserTable(SQLiteDatabase sqLiteDatabase){
        String sql="CREATE TABLE "+USERS_TABLE_NAME+"(";
        for(int i=0;i<USERS_TABLE_COLUMNS.length;i++){
            sql+=USERS_TABLE_COLUMNS[i];
            if(i<USERS_TABLE_COLUMNS.length-1){
                sql+=", ";
            }
        }
        sql+=");";
        sqLiteDatabase.execSQL(sql);

    }

    private void createMessageByUserTable(SQLiteDatabase sqLiteDatabase){
        String sql="CREATE TABLE "+MESSAGE_BY_USERS_TABLE_NAME+"(";
        for(int i=0;i<MESSAGE_BY_USERS_TABLE_COLUMNS.length;i++){
            sql+=MESSAGE_BY_USERS_TABLE_COLUMNS[i];
            if(i<MESSAGE_BY_USERS_TABLE_COLUMNS.length-1){
                sql+=", ";
            }
        }
        sql+=");";
        sqLiteDatabase.execSQL(sql);
    }

    private void createMessagesTable(SQLiteDatabase sqLiteDatabase){
        String sql="CREATE TABLE "+MESSAGES_TABLE_NAME+"(";
        for(int i=0;i<MESSAGES_TABLE_COLUMNS.length;i++){
            sql+=MESSAGES_TABLE_COLUMNS[i];
            if(i<MESSAGES_TABLE_COLUMNS.length-1){
                sql+=", ";
            }
        }
        sql+=");";
        sqLiteDatabase.execSQL(sql);
    }

    private void createConnectionsTable(SQLiteDatabase sqLiteDatabase){
        String sql="CREATE TABLE "+CONNECTIONS_TABLE_NAME+"(";
        for(int i=0;i<CONNECTIONS_TABLE_COLUMNS.length;i++){
            sql+=CONNECTIONS_TABLE_COLUMNS[i];
            if(i!=CONNECTIONS_TABLE_COLUMNS.length-1){
                sql+=", ";
            }
        }
        sql+=");";
        sqLiteDatabase.execSQL(sql);
    }
}
