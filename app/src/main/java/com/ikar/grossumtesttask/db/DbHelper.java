package com.ikar.grossumtesttask.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by iKar on 11/5/15.
 */
public class DbHelper extends SQLiteOpenHelper{

    static final String DB_NAME = "cashdesk_db";
    static final int DB_VERSION = 1;

    public static final String TABLE = "cashdesk";

    public static final String _ID = "_id";
    public static final String _DENOMINATION= "denomination";
    public static final String _INVENTORY= "inventory";

    static final String DB_CREATE = "create table "
            + TABLE + "("
            + _ID + " integer primary key autoincrement, "
            + _DENOMINATION + " integer, "
            + _INVENTORY + " integer"
            + ");";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
