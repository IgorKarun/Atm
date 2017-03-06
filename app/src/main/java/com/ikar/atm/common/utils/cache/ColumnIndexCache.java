package com.ikar.atm.common.utils.cache;

import android.database.Cursor;
import android.support.v4.util.ArrayMap;

/**
 * Created by iKar on 2/14/16.
 */
public class ColumnIndexCache {

    private ArrayMap<String, Integer> mMap = new ArrayMap<>();

    public int getColumnIndex(Cursor cursor, String columnName) {
        if (!mMap.containsKey(columnName))
            mMap.put(columnName, cursor.getColumnIndex(columnName));
        return mMap.get(columnName);
    }

    public void clear() {
        mMap.clear();
    }
}
