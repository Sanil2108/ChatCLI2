package com.sanilk.chatcli2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.text.DateFormat;
import java.util.Date;

import com.sanilk.chatcli2.database.Entities.Message;
import com.sanilk.chatcli2.database.Entities.User;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static int DATABASE_VERSION=9;
    private static String DATABASE_NAME="CHAT_CLI_DATABASE";
    private Context context;

    public DatabaseOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    //Users table
    //This table has no point if it only contains the attribute nick
    private final static String USER_TABLE_NAME="User";
    private final static String[] USER_TABLE_COLUMN_NAMES={
            "nick"
    };
    private final static String[] USER_TABLE_COLUMNS={
            "nick TEXT PRIMARY KEY"
    };

    private void dropUsersTable(SQLiteDatabase db){
        String sql="DROP TABLE IF EXISTS "+USER_TABLE_NAME;
        db.execSQL(sql);
    }

    private void createUserTable(SQLiteDatabase db){
        String sql="CREATE TABLE "+USER_TABLE_NAME+" (";
        for(int i=0;i<USER_TABLE_COLUMNS.length;i++){
            sql+=USER_TABLE_COLUMNS[i];
            if(i!=USER_TABLE_COLUMNS.length-1){
                sql+=", ";
            }
        }
        sql+=");";
        db.execSQL(sql);

        ContentValues contentValues=new ContentValues();
        contentValues.put(USER_TABLE_COLUMN_NAMES[0], "NA");
        db.insert(
                USER_TABLE_NAME,
                null,
                contentValues
        );
    }

    private void insertNewUser(User user){
        ContentValues contentValues=new ContentValues();
        contentValues.put(USER_TABLE_COLUMN_NAMES[0], user.nick);
        this.getWritableDatabase().insert(
                USER_TABLE_NAME,
                null,
                contentValues
        );
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
        return cursor.getCount()>0;
    }

    public void insertConnection(User user1, User user2){
        String sql="insert into "+CONNECTIONS_TABLE_NAME+" Values ((select (Select max("+CONNECTIONS_TABLE_COLUMN_NAMES[0]+") from "+CONNECTIONS_TABLE_NAME+") +1), ?, ?);";
        this.getWritableDatabase().rawQuery(
                sql,
                new String[]{user1.nick, user2.nick}
        );

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

    //Message table
    private final static String MESSAGE_TABLE_NAME="Message";
    private final static String[] MESSAGE_TABLE_COLUMN_NAMES={
            "messageId",
            "contents",
            "timeAndDate",
            "encryptDuration"
    };
    private final static String[] MESSAGE_TABLE_COLUMNS={
            "messageId INTEGER PRIMARY KEY",
            "contents TEXT",
            "timeAndDate TEXT",
            "encryptDuration INTEGER"
    };

    private void dropMessageTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+MESSAGE_TABLE_NAME+";");
    }

    private void createMessageTable(SQLiteDatabase db){
        String sql="CREATE TABLE "+MESSAGE_TABLE_NAME+"(";
        for(int i=0;i<MESSAGE_TABLE_COLUMNS.length;i++){
            sql+=MESSAGE_TABLE_COLUMNS[i];
            if(i!=MESSAGE_TABLE_COLUMNS.length-1){
                sql+=", ";
            }
        }
        sql+=");";
        db.execSQL(sql);

        ContentValues contentValues=new ContentValues();
        contentValues.put(MESSAGE_TABLE_COLUMN_NAMES[0], 0);
        contentValues.put(MESSAGE_TABLE_COLUMN_NAMES[1], "NA");
        contentValues.put(MESSAGE_TABLE_COLUMN_NAMES[2], "NA");
        contentValues.put(MESSAGE_TABLE_COLUMN_NAMES[3], -1);
        db.insert(
                MESSAGE_TABLE_NAME,
                null,
                contentValues
        );
    }

    private void insertMessage(Message message){
        Cursor cursor=this.getReadableDatabase().rawQuery(
                "SELECT MAX("+MESSAGE_TABLE_COLUMN_NAMES[0]+") FROM "+MESSAGE_TABLE_NAME+";",
                null
        );
        if(cursor.moveToFirst()){
            int newId = cursor.getInt(0) + 1;
            ContentValues contentValues = new ContentValues();
            contentValues.put(MESSAGE_TABLE_COLUMN_NAMES[0], newId);
            contentValues.put(MESSAGE_TABLE_COLUMN_NAMES[1], message.contents);
            contentValues.put(MESSAGE_TABLE_COLUMN_NAMES[2], message.timeAndDate);
            this.getWritableDatabase().insert(
                    MESSAGE_TABLE_NAME,
                    null,
                    contentValues
            );
        }
    }

    //MessageToUser table
    private final static String MESSAGE_TO_USER_TABLE_NAME="MessageToUser";
    private final static String[] MESSAGE_TO_USER_COLUMNS={
            "messageToUserId INTEGER PRIMARY KEY",
            "messageId INTEGER",
            "nick TEXT",
            "FOREIGN KEY(nick) REFERENCES User(nick)",
            "FOREIGN KEY(messageId) REFERENCES Message(messageId)"
    };
    private final static String[] MESSAGE_TO_USER_COLUMN_NAMES={
            "messageToUserId",
            "messageId",
            "nick"
    };

    private void dropMessageToUserTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+MESSAGE_TO_USER_TABLE_NAME+";");
    }

    private void createMessageToUserTable(SQLiteDatabase db){
        String sql="CREATE TABLE "+MESSAGE_TO_USER_TABLE_NAME+"(";
        for(int i=0;i<MESSAGE_TO_USER_COLUMNS.length;i++){
            sql+=MESSAGE_TO_USER_COLUMNS[i];
            if(i!=MESSAGE_TO_USER_COLUMNS.length-1){
                sql+=", ";
            }
        }
        sql+=");";
        db.execSQL(sql);

        ContentValues contentValues=new ContentValues();
        contentValues.put(MESSAGE_TO_USER_COLUMN_NAMES[0], 0);
        contentValues.put(MESSAGE_TO_USER_COLUMN_NAMES[1], 0);
        contentValues.put(MESSAGE_TO_USER_COLUMN_NAMES[2], "NA");
        db.insert(
                MESSAGE_TO_USER_TABLE_NAME,
                null,
                contentValues
        );
    }

    //Should be called after inserting the message
    private void insertNewMessageToUser(User user){
        //Getting new id
        Cursor cursor=this.getWritableDatabase().rawQuery(
                "SELECT MAX("+MESSAGE_TO_USER_COLUMN_NAMES[0]+") FROM "+MESSAGE_TO_USER_TABLE_NAME+";",
                null
        );
        if(!cursor.moveToFirst()){
            return;
        }
        int newId=cursor.getInt(0)+1;
        cursor.close();

        //Getting message id
        cursor=this.getWritableDatabase().rawQuery(
                "SELECT MAX("+MESSAGE_TABLE_COLUMN_NAMES[0]+") FROM "+MESSAGE_TABLE_NAME+";",
                null
        );
        if(!cursor.moveToFirst()){
            return;
        }
        int messageId=cursor.getInt(0);
        cursor.close();

        ContentValues contentValues=new ContentValues();
        contentValues.put(MESSAGE_TO_USER_COLUMN_NAMES[0], newId);
        contentValues.put(MESSAGE_TO_USER_COLUMN_NAMES[1], messageId);
        contentValues.put(MESSAGE_TO_USER_COLUMN_NAMES[2], user.nick);
        this.getWritableDatabase().insert(
                MESSAGE_TO_USER_TABLE_NAME,
                null,
                contentValues
        );
    }

    //MessageFromUser table
    private final static String MESSAGE_FROM_USER_TABLE_NAME="MessageFromUser";
    private final static String[] MESSAGE_FROM_USER_COLUMNS={
            "messageFromUserId INTEGER PRIMARY KEY",
            "messageId INTEGER",
            "nick TEXT",
            "FOREIGN KEY(nick) REFERENCES User(nick)",
            "FOREIGN KEY(messageId) REFERENCES Message(messageId)"
    };
    private final static String[] MESSAGE_FROM_USER_COLUMN_NAMES={
            "messageFromUserId",
            "messageId",
            "nick"
    };

    private void dropMessageFromUserTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+MESSAGE_FROM_USER_TABLE_NAME+";");
    }

    private void createMessageFromUserTable(SQLiteDatabase db){
        String sql="CREATE TABLE "+MESSAGE_FROM_USER_TABLE_NAME+"(";
        for(int i=0;i<MESSAGE_FROM_USER_COLUMNS.length;i++){
            sql+=MESSAGE_FROM_USER_COLUMNS[i];
            if(i!=MESSAGE_FROM_USER_COLUMNS.length-1){
                sql+=", ";
            }
        }
        sql+=");";
        db.execSQL(sql);

        ContentValues contentValues=new ContentValues();
        contentValues.put(MESSAGE_FROM_USER_COLUMN_NAMES[0], 0);
        contentValues.put(MESSAGE_FROM_USER_COLUMN_NAMES[1], 0);
        contentValues.put(MESSAGE_FROM_USER_COLUMN_NAMES[2], "NA");
        db.insert(
                MESSAGE_FROM_USER_TABLE_NAME,
                null,
                contentValues
        );
    }

    //Should be called after inserting the message
    private void insertNewMessageFromUser(User user){
        //Getting new id
        Cursor cursor=this.getWritableDatabase().rawQuery(
                "SELECT MAX("+MESSAGE_FROM_USER_COLUMN_NAMES[0]+") FROM "+MESSAGE_FROM_USER_TABLE_NAME+";",
                null
        );
        if(!cursor.moveToFirst()){
            return;
        }
        int newId=cursor.getInt(0)+1;
        cursor.close();

        //Getting message id
        cursor=this.getWritableDatabase().rawQuery(
                "SELECT MAX("+MESSAGE_TABLE_COLUMN_NAMES[0]+") FROM "+MESSAGE_TABLE_NAME+";",
                null
        );
        if(!cursor.moveToFirst()){
            return;
        }
        int messageId=cursor.getInt(0);
        cursor.close();

        ContentValues contentValues=new ContentValues();
        contentValues.put(MESSAGE_FROM_USER_COLUMN_NAMES[0], newId);
        contentValues.put(MESSAGE_FROM_USER_COLUMN_NAMES[1], messageId);
        contentValues.put(MESSAGE_FROM_USER_COLUMN_NAMES[2], user.nick);
        this.getWritableDatabase().insert(
                MESSAGE_FROM_USER_TABLE_NAME,
                null,
                contentValues
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createConnectionsTable(db);
        createUserTable(db);
        createMessageTable(db);
        createMessageToUserTable(db);
        createMessageFromUserTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropUsersTable(db);
        dropMessageTable(db);
        dropConnectionsTable(db);
        dropMessageToUserTable(db);
        dropMessageFromUserTable(db);

        onCreate(db);
    }

    public void newMessage(User sender, User receiver, Message message){
        insertMessage(message);
        insertNewMessageToUser(receiver);
        insertNewMessageFromUser(sender);
    }

    public Message[] getMessages(int n, User client, User connectedUser){
        //MessageFromUser table
        String sql=
                "select * from "+MESSAGE_TABLE_NAME+" WHere "+MESSAGE_TABLE_COLUMN_NAMES[0]+" in " +
                        "(select "+MESSAGE_FROM_USER_COLUMN_NAMES[1]+" from "+MESSAGE_FROM_USER_TABLE_NAME+" where "+MESSAGE_FROM_USER_COLUMN_NAMES[2]+"=\'"+client.nick+"\') " +
                        "and "+MESSAGE_TABLE_COLUMN_NAMES[0]+" in (select "+MESSAGE_TO_USER_COLUMN_NAMES[1]+" from "+MESSAGE_TO_USER_TABLE_NAME+" where "+
                        MESSAGE_TO_USER_COLUMN_NAMES[2]+"=\'"+connectedUser.nick+"\');";
        Cursor cursor=this.getWritableDatabase().rawQuery(
                sql,
                null
        );
        Message[] messagesFrom=new Message[cursor.getCount()];
        int i=0;
        if(cursor.moveToFirst()){
            messagesFrom[i]=new Message(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            cursor.moveToNext();
        }
        cursor.close();

        //MessageToUser table
        sql=
                "select * from "+MESSAGE_TABLE_NAME+" WHere "+MESSAGE_TABLE_COLUMN_NAMES[0]+" in " +
                        "(select "+MESSAGE_TO_USER_COLUMN_NAMES[1]+" from "+MESSAGE_TO_USER_TABLE_NAME+" where "+MESSAGE_TO_USER_COLUMN_NAMES[2]+"=\'"+client.nick+"\') " +
                        "and "+MESSAGE_TABLE_COLUMN_NAMES[0]+" in (select "+MESSAGE_FROM_USER_COLUMN_NAMES[1]+" from "+MESSAGE_FROM_USER_TABLE_NAME+" where "+
                        MESSAGE_FROM_USER_COLUMN_NAMES[2]+"=\'"+connectedUser.nick+"\');";
        cursor=this.getWritableDatabase().rawQuery(
                sql,
                null
        );
        Message[] messagesTo=new Message[cursor.getCount()];
        i=0;
        if(cursor.moveToFirst()){
            messagesTo[i]=new Message(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            cursor.moveToNext();
        }
        return null;
    }
}
