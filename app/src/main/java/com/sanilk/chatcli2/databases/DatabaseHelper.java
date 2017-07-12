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
    private static final int DB_VERSION=5;

    //Connections table
    protected static final String CONNECTIONS_TABLE_NAME="CONNECTIONS_TABLE";
    private static final String[] CONNECTIONS_TABLE_COLUMNS={
            "username text, sendername text"
    };
    protected static final String[] CONNECTIONS_TABLE_COLUMNS_NAMES={
            "username","sendername"
    };


    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
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

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + i + " to "
                        + i1 + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+CONNECTIONS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
