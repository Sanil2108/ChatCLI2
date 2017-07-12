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

    public void newConnection(String currentUser, String otherUser){
        ArrayList<ContentValues> currentUsers=getAllConnections();
        for(ContentValues contentValues:currentUsers){
            if(contentValues.get("user").equals(currentUser) && contentValues.get("sender").equals(otherUser)){
                return;
            }
        }
        enterUser(currentUser, otherUser);
    }

    private void enterUser(String currentUser, String senderNick){
        ContentValues contentValues=new ContentValues();
        contentValues.put(allColumns[0], currentUser);
        contentValues.put(allColumns[1], senderNick);

        SQLiteDatabase database=databaseHelper.getWritableDatabase();
        database.insert(DatabaseHelper.CONNECTIONS_TABLE_NAME, null, contentValues);

        System.out.println("New connection entered - "+senderNick+" for user "+currentUser);

        database.close();
    }

//    public ArrayList<String> getAllUsers(){
//        ArrayList<String> allUsers=new ArrayList<>();
//
//        SQLiteDatabase database=databaseHelper.getReadableDatabase();
//        Cursor cursor=database.query(DatabaseHelper.CONNECTIONS_TABLE_NAME, allColumns, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        while(!cursor.isAfterLast()){
//            allUsers.add(cursor.getString(0));
//            cursor.moveToNext();
//        }
//
//        return allUsers;
//    }

    public ArrayList<ContentValues> getAllConnections(){
        ArrayList<ContentValues> allConnections=new ArrayList<>();

        SQLiteDatabase database=databaseHelper.getReadableDatabase();
        Cursor cursor=database.query(DatabaseHelper.CONNECTIONS_TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ContentValues contentValues=new ContentValues(2);
            contentValues.put("user", cursor.getString(0));
            contentValues.put("sender", cursor.getString(1));
            allConnections.add(contentValues);
            cursor.moveToNext();
        }

        database.close();

        return allConnections;
    }

}
