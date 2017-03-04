package com.ikar.grossumtesttask.main;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ikar.grossumtesttask.App;
import com.ikar.grossumtesttask.algorithms.ICash;
import com.ikar.grossumtesttask.common.shared.Shared;
import com.ikar.grossumtesttask.components.DaggerICashComponent;
import com.ikar.grossumtesttask.data.CashDeskItem;
import com.ikar.grossumtesttask.db.UriMatcherHelper;
import com.ikar.grossumtesttask.db.scheme.TableCashDesk;
import com.ikar.grossumtesttask.utils.cache.ColumnIndexCache;
import com.ikar.grossumtesttask.views.AddItemFragmentDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by igorkarun on 3/2/17.
 */

public class MainFragmentPresenter {

    private final static String TAG = MainFragmentPresenter.class.getSimpleName();

    private IMainFragmentView view;


    @Inject
    ICash cash;

    public MainFragmentPresenter(IMainFragmentView view) {
       this.view = view;
    }

    private void calculateCashDesk(int amount) {
        List<CashDeskItem> cashDeskItems = new ArrayList<>();

        String orderBy =  TableCashDesk._DENOMINATION + " ASC";
        String[] args = new String[]{String.valueOf(amount)};
        Cursor cursor = App.instance().getContentResolver()
                .query(UriMatcherHelper.CONTENT_URI, null,
                        TableCashDesk._DENOMINATION +"<=?", args, orderBy);

        if(cursor != null) {
            ColumnIndexCache cache = new ColumnIndexCache();
            while (cursor.moveToNext())
                cashDeskItems.add(new CashDeskItem(cursor, cache));
            cursor.close();
        }

        cash = DaggerICashComponent.create().getCash();
        Map<Integer, Integer> result = cash.getAmount(cashDeskItems, cashDeskItems.size() - 1, amount);
        if(result != null && result.size() > 0) {
            //Succesful transaction
            for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
                String[] selectionArgs = new String[]{String.valueOf(entry.getKey())};
                ContentValues contentValues = new ContentValues();
                contentValues.put(TableCashDesk._INVENTORY, entry.getValue());
                App.instance().getContentResolver().update(UriMatcherHelper.CONTENT_URI, contentValues,
                        TableCashDesk._DENOMINATION +"=?", selectionArgs);
            }
            transactionDialog("Transaction succesful.", "Please get your "
                    + Integer.toString(amount) + "$");
        } else {
            transactionDialog("Transaction failed.", "Sorry, not enough cash.");
        }
    }

    public static int checkInputAmount(Context context, String amountText, String negativeMessage) {
        if(!TextUtils.isEmpty(amountText)) {
            Pattern p = Pattern.compile("^[1-9]\\d*$");
            Matcher m = p.matcher(amountText);
            if(m.matches()) {
                int value = Integer.parseInt(amountText);
                return value;
            }
        } else {
            Toast.makeText(context, negativeMessage, Toast.LENGTH_LONG).show();
        }

        return 0;
    }

    private void transactionDialog(String title, String message) {
        Log.e(TAG, "Show dialog");
        new AlertDialog.Builder(App.instance())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        }
                )
                .show();
    }

    public void defaultData() {
        if(!Shared.isFirstLaunch()) {
            addDefaultDataToDb();
            Shared.setFirstLaunch(true);
        }
    }

    public static final int DEFAULT_INVENTORY = 10;

    private void addDefaultDataToDb() {
        List<Integer> denominationList = new ArrayList<>(Arrays.asList(1, 5, 10, 20, 100));

        for (Integer item : denominationList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableCashDesk._DENOMINATION, item);
            contentValues.put(TableCashDesk._INVENTORY, DEFAULT_INVENTORY);
            App.instance().getContentResolver().insert(UriMatcherHelper.CONTENT_URI, contentValues);
        }
    }

    public void resetData() {
        App.instance().getContentResolver().delete(UriMatcherHelper.CONTENT_URI, null, null);
        addDefaultDataToDb();
    }

    public void addData() {
        AddItemFragmentDialog addItemFragmentDialog = new AddItemFragmentDialog();
       // addItemFragmentDialog.show(getFragmentManager(), "AddFragmentDialog");
    }




//    private void hideKeyboard() {
//        InputMethodManager inputManager = (InputMethodManager) getActivity().
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
//    }

    //getLoaderManager().initLoader(0, null, this);
}
