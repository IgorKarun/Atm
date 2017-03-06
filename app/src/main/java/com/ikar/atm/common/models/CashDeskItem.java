package com.ikar.atm.common.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.ikar.atm.common.db.scheme.TableCashDesk;
import com.ikar.atm.common.utils.DbUtils;
import com.ikar.atm.common.utils.cache.ColumnIndexCache;

/**
 * Created by iKar on 11/5/15.
 */
public class CashDeskItem {

    private int denomination;
    private int inventory;

    public CashDeskItem() {

    }

    public CashDeskItem(int denomination, int inventory) {
        this.denomination = denomination;
        this.inventory = inventory;
    }

    public CashDeskItem(Cursor cursor, ColumnIndexCache cache) {
        denomination = DbUtils.getIntegerValue(cursor, cache, TableCashDesk._DENOMINATION);
        inventory = DbUtils.getIntegerValue(cursor, cache, TableCashDesk._INVENTORY);
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableCashDesk._DENOMINATION, denomination);
        contentValues.put(TableCashDesk._INVENTORY, inventory);
        return contentValues;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
