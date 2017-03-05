package com.ikar.grossumtesttask.main;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;

/**
 * Created by igorkarun on 3/2/17.
 */

public interface IMainFragmentView {

    void addItem();
    void resetItems();
    void giveMoney();
    void updateAdapter(Cursor cursor);
    void initLoading(LoaderManager.LoaderCallbacks<Cursor> loader);
    void showAddNewItemDialog(boolean value, int amount);
}
