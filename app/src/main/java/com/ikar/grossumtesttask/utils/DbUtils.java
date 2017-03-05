package com.ikar.grossumtesttask.utils;

import android.database.Cursor;

import com.ikar.grossumtesttask.utils.cache.ColumnIndexCache;

import java.util.Date;

/**
 * Created by Igor on 17.03.2016.
 */
public class DbUtils {

    public static String getStringValue(Cursor cursor, ColumnIndexCache cache, String field) {
        return getStringValue(cursor, cache.getColumnIndex(cursor, (field)));
    }

    public static String getStringValue(Cursor cursor, int index) {
        return !cursor.isNull(index) ? cursor.getString(index) : null;
    }

    public static Integer getIntegerValue(Cursor cursor, ColumnIndexCache cache, String field) {
        return getIntegerValue(cursor, cache.getColumnIndex(cursor, (field)));
    }

    public static Integer getIntegerValue(Cursor cursor, int index) {
        return !cursor.isNull(index) ? cursor.getInt(index) : null;
    }

    public static Long getLongValue(Cursor cursor, ColumnIndexCache cache, String field) {
        return getLongValue(cursor, cache.getColumnIndex(cursor, (field)));
    }

    public static Long getLongValue(Cursor cursor, int index) {
        return !cursor.isNull(index) ? cursor.getLong(index) : null;
    }

    public static Float getSimpleFloatValue(Cursor cursor, ColumnIndexCache cache, String field) {
        return getSimpleFloatValue(cursor, cache.getColumnIndex(cursor, (field)));
    }

    public static Float getSimpleFloatValue(Cursor cursor, int index) {
        return !cursor.isNull(index) ? cursor.getFloat(index) : 0.0f;
    }

    public static Float getFloatValue(Cursor cursor, ColumnIndexCache cache, String field) {
        return getFloatValue(cursor, cache.getColumnIndex(cursor, (field)));
    }

    public static Float getFloatValue(Cursor cursor, int index) {
        return !cursor.isNull(index) ? cursor.getFloat(index) : null;
    }

    public static Double getSimpleDoubleValue(Cursor cursor, ColumnIndexCache cache, String field) {
        return getSimpleDoubleValue(cursor, cache.getColumnIndex(cursor, (field)));
    }

    public static Double getSimpleDoubleValue(Cursor cursor, int index) {
        return !cursor.isNull(index) ? cursor.getDouble(index) : 0.0d;
    }

    public static Double getDoubleValue(Cursor cursor, ColumnIndexCache cache, String field) {
        return getDoubleValue(cursor, cache.getColumnIndex(cursor, (field)));
    }

    public static Double getDoubleValue(Cursor cursor, int index) {
        return !cursor.isNull(index) ? cursor.getDouble(index) : null;
    }

    public static Boolean getBooleanValue(Cursor cursor, ColumnIndexCache cache, String field) {
        return getBooleanValue(cursor, cache.getColumnIndex(cursor, (field)));
    }

    public static Boolean getBooleanValue(Cursor cursor, int index) {
        return !cursor.isNull(index) ? cursor.getInt(index) > 0 : null;
    }

    public static boolean getSimpleBooleanValue(Cursor cursor, ColumnIndexCache cache, String field) {
        return getSimpleBooleanValue(cursor, cache.getColumnIndex(cursor, (field)));
    }

    public static boolean getSimpleBooleanValue(Cursor cursor, int index) {
        return !cursor.isNull(index) && cursor.getInt(index) > 0;
    }

    public static Date getDateValue(Cursor cursor, ColumnIndexCache cache, String field) {
        return getDateValue(cursor, cache.getColumnIndex(cursor, (field)));
    }

    public static Date getDateValue(Cursor cursor, int index) {
        return !cursor.isNull(index) ? new Date(cursor.getLong(index)) : null;
    }

//    public static JsonObject getJsonObject(Cursor cursor, ColumnIndexCache cache, String field) {
//        return getJsonObject(cursor, cache.getColumnIndex(cursor, (field)));
//    }
//
//    public static JsonObject getJsonObject(Cursor cursor, int index) {
//        return !cursor.isNull(index) ? new JsonParser().parse(cursor.getString(index)).getAsJsonObject() : null;
//    }

    public static byte[] getBlob(Cursor cursor, ColumnIndexCache cache, String field) {
        return getBlob(cursor, cache.getColumnIndex(cursor, (field)));
    }

    public static byte[] getBlob(Cursor cursor, int index) {
        return !cursor.isNull(index) ? cursor.getBlob(index) : null;
    }

    public static <T> T getBlobValue(Cursor cursor, ColumnIndexCache cache, String field) {
        byte[] daysStatsByte = getBlob(cursor, cache, field);
        if (daysStatsByte != null && daysStatsByte.length > 0)
            return (T) Utils.deserializeObject(daysStatsByte);
        return null;
    }

}