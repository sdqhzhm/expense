package com.sky.expense.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sky on 2014/5/8.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "provider.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TRANSACTIONS_TABLE_NAME 	= "transactions";

    public static final String KEY_ROW_ID 	= "_id";
    public static final String KEY_NAME 	= "name";
    public static final String KEY_TYPE 	= "type";
    public static final String KEY_AMOUNT 		= "amount";
    public static final String KEY_DESCRIPTION 	= "description";
    public static final String KEY_TIMESTAMP 	= "timestamp";



    private static final String TRANSACTIONS_TABLE_CREATE = "create table if not exists " + TRANSACTIONS_TABLE_NAME + " ("
            + KEY_ROW_ID 		+ " integer primary key autoincrement, "
            + KEY_NAME 			+ " text, "
            + KEY_TYPE 			+ " varchar(255) not null, "
            + KEY_AMOUNT 		+ " real default 0, "
            + KEY_DESCRIPTION 	+ " text, "
            + KEY_TIMESTAMP 	+ " integer not null, "
            + ");";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRANSACTIONS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
