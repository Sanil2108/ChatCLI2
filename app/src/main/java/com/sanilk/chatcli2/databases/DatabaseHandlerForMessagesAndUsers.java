package com.sanilk.chatcli2.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sanil on 1/12/17.
 */

public class DatabaseHandlerForMessagesAndUsers {
    private DatabaseHelper databaseHelper;

    public DatabaseHandlerForMessagesAndUsers(Context context){
        databaseHelper=new DatabaseHelper(context);
    }

    public void newMessageReceived(String nick, String contents, int encryptionDuration){
        SQLiteDatabase database=databaseHelper.getWritableDatabase();
        String sql="INSERT INTO Message VALUES((SELECT (SELECT MAX(messageId) from Message)+1), \'"+contents+"\', DATETIME('now', 'localtime'), -1);\n" +
                "INSERT INTO MessageByUser VALUES((SELECT (SELECT MAX(messageByUserId) from MessageByUser) +1), \'"+nick+"\', " +
                "(SELECT MAX(messageId) from Message));";
        database.execSQL(sql);
        sql="INSERT INTO MessageByUser VALUES((SELECT (SELECT MAX(messageByUserId) from MessageByUser) +1), \'"+nick+"" +
                "\', (SELECT MAX(messageId) from Message));\n";
        database.execSQL(sql);
        sql="UPDATE User set numberOfMessagesRecd = (SELECT (SELECT numberOfMessagesRecd from User where nick=\'"+nick+"\') +1);";
        database.execSQL(sql);
    }

    public void newMessageSent(String nick, String contents, int encryptionDuration){

    }

    public void enterUser(String nick){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DatabaseHelper.USERS_TABLE_COLUMNS_NAMES[0], nick);
        contentValues.put(DatabaseHelper.USERS_TABLE_COLUMNS_NAMES[1], 0);
        contentValues.put(DatabaseHelper.USERS_TABLE_COLUMNS_NAMES[2], 0);
        databaseHelper.getWritableDatabase().insert(DatabaseHelper.USERS_TABLE_NAME, null, contentValues);
    }

}
