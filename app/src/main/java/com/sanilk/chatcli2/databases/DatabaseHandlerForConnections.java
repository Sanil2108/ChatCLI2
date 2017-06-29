package com.sanilk.chatcli2.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Admin on 29-06-2017.
 */

public class DatabaseHandlerForConnections {

    private DatabaseHelper databaseHelper;
    private String[] allColumns=DatabaseHelper.CONNECTIONS_TABLE_COLUMNS_NAMES;

    public DatabaseHandlerForConnections(Context context){
        databaseHelper=new DatabaseHelper(context);
    }

    public void newConnection(String nick){
        ArrayList<String> currentUsers=getAllUsers();
        for(String string:currentUsers){
            if(nick.equals(string)){
                return;
            }
        }
        enterUser(nick);
    }

    private void enterUser(String nick){
        ContentValues contentValues=new ContentValues();
        contentValues.put(allColumns[0], nick);

        SQLiteDatabase database=databaseHelper.getWritableDatabase();
        database.insert(DatabaseHelper.CONNECTIONS_TABLE_NAME, null, contentValues);
        database.close();
    }

    public ArrayList<String> getAllUsers(){
        ArrayList<String> allUsers=new ArrayList<>();

        SQLiteDatabase database=databaseHelper.getReadableDatabase();
        Cursor cursor=database.query(DatabaseHelper.CONNECTIONS_TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            allUsers.add(cursor.getString(0));
            cursor.moveToNext();
        }

        return allUsers;
    }

}
