package com.ikar.atm.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ikar.atm.common.db.scheme.TableCashDesk;

/**
 * Created by iKar on 11/5/15.
 */
public class DbHelper extends SQLiteOpenHelper{

    static final String DB_NAME = "cashdesk_db";
    static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableCashDesk.DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
