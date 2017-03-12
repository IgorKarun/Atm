package com.ikar.atm.main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.ikar.atm.App;
import com.ikar.atm.common.algorithms.ICash;
import com.ikar.atm.common.shared.Shared;
import com.ikar.atm.common.components.DaggerICashComponent;
import com.ikar.atm.common.models.CashDeskItem;
import com.ikar.atm.common.db.DbQuery;
import com.ikar.atm.common.db.UriMatcherHelper;
import com.ikar.atm.common.db.scheme.TableCashDesk;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by igorkarun on 3/2/17.
 */

public class MainFragmentPresenter implements LoaderManager.LoaderCallbacks<Cursor>{

    private final static String TAG = MainFragmentPresenter.class.getSimpleName();

    private IMainFragmentView view;

    @Inject
    ICash cash;

    public MainFragmentPresenter(IMainFragmentView view) {
        this.view = view;
        this.view.initLoading(this);
    }

    public void calculateCashDesk(final int amount) {
        //TODO: Need to add some progressbar
        List<CashDeskItem> cashDeskItems = DbQuery.getCashDeskItems(amount);
        cash = DaggerICashComponent.create().getCash();
        cash.getAmount(cashDeskItems, amount)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    boolean value = DbQuery.updateCashDeskItems(result);
                    view.showAddNewItemDialog(value, amount);
                });
    }

    private void defaultData() {
        if (!Shared.isFirstLaunch()) {
            Shared.setFirstLaunch(true);
            DbQuery.addDefaultDataToDb();
        }
    }

    public void resetData() {
        DbQuery.deleteCashDeskItems();
        DbQuery.addDefaultDataToDb();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String orderBy =  TableCashDesk._DENOMINATION + " ASC";
        return new CursorLoader(App.instance(), UriMatcherHelper.CONTENT_URI,
                null, null, null, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        view.updateAdapter(data);
        defaultData();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
