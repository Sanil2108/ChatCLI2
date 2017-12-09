package com.sanilk.chatcli2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sanilk.chatcli2.database.Entities.User;

/**
 * Created by sanil on 9/12/17.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static int DATABASE_VERSION=4;
    private static String DATABASE_NAME="CHAT_CLI_DATABASE";
    private Context context;

    public DatabaseOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    //Connections table
    private final static String CONNECTIONS_TABLE_NAME="Connections";
    private final static String[] CONNECTIONS_TABLE_COLUMNS={
            "connectionsId INTEGER PRIMARY KEY",
            "clientUserId TEXT",
            "connectedUserId TEXT",
            "FOREIGN KEY(clientUserId) REFERENCES User(nick)",
            "FOREIGN KEY(connectedUserId) REFERENCES User(nick)"
    };
    private final static String[] CONNECTIONS_TABLE_COLUMN_NAMES={
            "connectionsId",
            "clientUserId",
            "connectedUserId",
    };

    private void dropConnectionsTable(SQLiteDatabase db){
        String sql="DROP TABLE IF EXISTS "+CONNECTIONS_TABLE_NAME+";";
        db.execSQL(sql);
    }

    private void createConnectionsTable(SQLiteDatabase db){
        String sql="CREATE TABLE "+CONNECTIONS_TABLE_NAME+" (";
        for(int i=0;i<CONNECTIONS_TABLE_COLUMNS.length;i++){
            sql+=CONNECTIONS_TABLE_COLUMNS[i];
            if(i!=CONNECTIONS_TABLE_COLUMNS.length-1){
                sql+=", ";
            }
        }
        sql+=");";
        db.execSQL(sql);

        ContentValues contentValues=new ContentValues();
        contentValues.put(CONNECTIONS_TABLE_COLUMN_NAMES[0], 0);
        contentValues.put(CONNECTIONS_TABLE_COLUMN_NAMES[1], "NA");
        contentValues.put(CONNECTIONS_TABLE_COLUMN_NAMES[2], "NA");
        db.insert(
                CONNECTIONS_TABLE_NAME,
                null,
                contentValues
        );
    }

    //This checks whether user1 is connected to user2 or not and not the other way around
    public boolean isConnection(User user1, User user2){
        Cursor cursor=this.getReadableDatabase().rawQuery(
                "select * from "+CONNECTIONS_TABLE_NAME+" where ("+CONNECTIONS_TABLE_COLUMN_NAMES[1]+"=? and "+CONNECTIONS_TABLE_COLUMN_NAMES[2]+"=?);",
                new String[]{user1.nick, user2.nick}
        );
        int temp=cursor.getCount();
        return cursor.getCount()>0;
    }

    public void insertConnection(User user1, User user2){
//        String sql="insert into "+CONNECTIONS_TABLE_NAME+" Values ((select (Select max("+CONNECTIONS_TABLE_COLUMN_NAMES[0]+") from "+CONNECTIONS_TABLE_NAME+") +1), ?, ?);";
//        this.getWritableDatabase().rawQuery(
//                sql,
//                new String[]{user1.nick, user2.nick}
//        );

        Cursor cursor=this.getReadableDatabase().rawQuery(
                "SELECT MAX("+CONNECTIONS_TABLE_COLUMN_NAMES[0]+") FROM "+CONNECTIONS_TABLE_NAME,
                null
        );
        if(cursor.moveToFirst()){
            int newId=cursor.getInt(0)+1;
            ContentValues contentValues=new ContentValues();
            contentValues.put(CONNECTIONS_TABLE_COLUMN_NAMES[0], newId);
            contentValues.put(CONNECTIONS_TABLE_COLUMN_NAMES[1], user1.nick);
            contentValues.put(CONNECTIONS_TABLE_COLUMN_NAMES[2], user2.nick);
            getWritableDatabase().insert(
                    CONNECTIONS_TABLE_NAME,
                    null,
                    contentValues
            );
        }
    }

    public User[] getAllConnections(User user){
        String sql="select "+CONNECTIONS_TABLE_COLUMN_NAMES[2]+" from "+CONNECTIONS_TABLE_NAME+" where "+CONNECTIONS_TABLE_COLUMN_NAMES[1]+"=\'"+user.nick+"\';";
        Cursor cursor=this.getReadableDatabase().rawQuery(
                sql,
//                new String[]{user.nick}
                null
        );
        if(cursor.moveToFirst()){
            User[] users=new User[cursor.getCount()];
            int i=0;
            while(true){
                users[i]=new User(cursor.getString(0));
                i++;
                if(!cursor.moveToNext()){
                    break;
                }
            }
            return users;
        }
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createConnectionsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropConnectionsTable(db);

        onCreate(db);
    }
}
