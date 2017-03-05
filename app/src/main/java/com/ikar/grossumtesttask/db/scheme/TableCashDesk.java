package com.ikar.grossumtesttask.db.scheme;

/**
 * Created by igorkarun on 2/27/17.
 */

public class TableCashDesk {

    public static final String TABLE = "cashdesk";

    public static final String _ID = "_id";
    public static final String _DENOMINATION= "denomination";
    public static final String _INVENTORY= "inventory";

    public static final String DB_CREATE = "create table "
            + TABLE + "("
            + _ID + " integer primary key autoincrement, "
            + _DENOMINATION + " integer, "
            + _INVENTORY + " integer"
            + ");";
}
