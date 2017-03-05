package com.ikar.grossumtesttask.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.ikar.grossumtesttask.App;
import com.ikar.grossumtesttask.model.CashDeskItem;
import com.ikar.grossumtesttask.db.scheme.TableCashDesk;
import com.ikar.grossumtesttask.utils.cache.ColumnIndexCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by igorkarun on 3/4/17.
 */

public class DbQuery {

    public static List<CashDeskItem> getCashDeskItems(int amount) {
        List<CashDeskItem> cashDeskItems = new ArrayList<>();

        String orderBy = TableCashDesk._DENOMINATION + " ASC";
        String[] args = new String[]{String.valueOf(amount)};
        Cursor cursor = App.instance().getContentResolver()
                .query(UriMatcherHelper.CONTENT_URI, null,
                        TableCashDesk._DENOMINATION + "<=?", args, orderBy);
        if (cursor != null) {
            ColumnIndexCache cache = new ColumnIndexCache();
            while (cursor.moveToNext())
                cashDeskItems.add(new CashDeskItem(cursor, cache));
            cursor.close();
        }
        return cashDeskItems;
    }

    public static boolean updateCashDeskItems(Map<Integer, Integer> result) {
        if(result == null || result.size() == 0)
            return false;
        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            String[] selectionArgs = new String[]{String.valueOf(entry.getKey())};
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableCashDesk._INVENTORY, entry.getValue());
            App.instance().getContentResolver().update(UriMatcherHelper.CONTENT_URI, contentValues,
                    TableCashDesk._DENOMINATION + "=?", selectionArgs);
        }

        return true;
    }

    private static final int DEFAULT_INVENTORY = 10;

    public static void addDefaultDataToDb() {
        List<Integer> denominationList = new ArrayList<>(Arrays.asList(1, 5, 10, 20, 100));
        for (Integer item : denominationList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableCashDesk._DENOMINATION, item);
            contentValues.put(TableCashDesk._INVENTORY, DEFAULT_INVENTORY);
            App.instance().getContentResolver().insert(UriMatcherHelper.CONTENT_URI, contentValues);
        }
    }

    public static void deleteCashDeskItems() {
        App.instance().getContentResolver().delete(UriMatcherHelper.CONTENT_URI, null, null);
    }

    public static void addNewCashDeskItem(int amount, int bet) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCashDesk._DENOMINATION, bet);
        contentValues.put(TableCashDesk._INVENTORY, amount > 0 ? amount : DEFAULT_INVENTORY);
        App.instance().getContentResolver().insert(UriMatcherHelper.CONTENT_URI, contentValues);
    }
}
