package com.ikar.grossumtesttask.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.ikar.grossumtesttask.db.scheme.TableCashDesk;

/**
 * Created by iKar on 11/5/15.
 */
public class CashDeskContentProvider extends ContentProvider {

    private final static String TAG = CashDeskContentProvider.class.getName();

    private DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (UriMatcherHelper.uriMatcher.match(uri)) {
            case UriMatcherHelper.URI_CASHDESK:
                cursor = db.query(TableCashDesk.TABLE, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
        }


        if(cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(),
                    UriMatcherHelper.CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = 0;

        switch (UriMatcherHelper.uriMatcher.match(uri)) {
            case UriMatcherHelper.URI_CASHDESK:
                rowId = db.insert(TableCashDesk.TABLE, null, values);
                break;
        }


        Uri newUri = null;
        if(uri != null) {
            newUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(newUri, null);
        }
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ret = 0;

        switch (UriMatcherHelper.uriMatcher.match(uri)) {
            case UriMatcherHelper.URI_CASHDESK:
                ret = db.delete(TableCashDesk.TABLE, selection, selectionArgs);
                break;
        }

        if(uri != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return ret;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        switch (UriMatcherHelper.uriMatcher.match(uri)) {
            case UriMatcherHelper.URI_CASHDESK:
                count = db.update(TableCashDesk.TABLE, values, selection, selectionArgs);
                break;
        }

        if(uri != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
