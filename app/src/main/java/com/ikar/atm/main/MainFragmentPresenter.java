package com.ikar.atm.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ikar.atm.App;
import com.ikar.atm.algorithms.ICash;
import com.ikar.atm.common.shared.Shared;
import com.ikar.atm.components.DaggerICashComponent;
import com.ikar.atm.model.CashDeskItem;
import com.ikar.atm.db.DbQuery;
import com.ikar.atm.db.UriMatcherHelper;
import com.ikar.atm.db.scheme.TableCashDesk;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static int checkInputAmount(Context context, String amountText, String text) {
        if (!TextUtils.isEmpty(amountText)) {
            Pattern p = Pattern.compile("^[1-9]\\d*$");
            Matcher m = p.matcher(amountText);
            if (m.matches())
                return Integer.parseInt(amountText);
        } else
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();

        return 0;
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
        Log.e(TAG, "onCreateLoader");
        String orderBy =  TableCashDesk._DENOMINATION + " ASC";
        return new CursorLoader(App.instance(), UriMatcherHelper.CONTENT_URI,
                null, null, null, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(TAG, "onLoadFinished");
        view.updateAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(TAG, "onLoaderReset");
    }
}
