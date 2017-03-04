package com.ikar.grossumtesttask.main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ikar.grossumtesttask.App;
import com.ikar.grossumtesttask.adapters.RecyclerViewAdapter;
import com.ikar.grossumtesttask.db.UriMatcherHelper;
import com.ikar.grossumtesttask.db.scheme.TableCashDesk;

/**
 * Created by igorkarun on 3/2/17.
 */

public class MainFragmentModel implements IMainFragmentModel, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerViewAdapter adapter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String orderBy =  TableCashDesk._DENOMINATION + " ASC";
        return new CursorLoader(App.instance(), UriMatcherHelper.CONTENT_URI,
                null, null, null, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (adapter == null) {
//            adapter = new RecyclerViewAdapter(App.instance(), data);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(App.instance());
//            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

//            recyclerView.setAdapter(adapter);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setItemAnimator(itemAnimator);
        } else {
            adapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
