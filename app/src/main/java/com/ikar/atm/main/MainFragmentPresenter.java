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
import java.util.Map;

import javax.inject.Inject;

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
    }

    public void initLoading() {
        view.initLoading(this);
    }

    public void calculateCashDesk(int amount) {
        List<CashDeskItem> cashDeskItems = DbQuery.getCashDeskItems(amount);
        cash = DaggerICashComponent.create().getCash();
        Map<Integer, Integer> result = cash.getAmount(cashDeskItems, cashDeskItems.size() - 1, amount);
        boolean value = DbQuery.updateCashDeskItems(result);
        view.showAddNewItemDialog(value, amount);
    }

    public void defaultData() {
        if (!Shared.isFirstLaunch()) {
            DbQuery.addDefaultDataToDb();
            Shared.setFirstLaunch(true);
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
