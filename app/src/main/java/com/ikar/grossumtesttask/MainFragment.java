package com.ikar.grossumtesttask;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.ikar.grossumtesttask.adapters.RecyclerViewAdapter;
import com.ikar.grossumtesttask.algorithms.ICash;
import com.ikar.grossumtesttask.components.DaggerICashComponent;
import com.ikar.grossumtesttask.data.CashDeskItem;
import com.ikar.grossumtesttask.db.DbHelper;
import com.ikar.grossumtesttask.db.UriMatcherHelper;
import com.ikar.grossumtesttask.db.scheme.TableCashDesk;
import com.ikar.grossumtesttask.utils.cache.ColumnIndexCache;
import com.ikar.grossumtesttask.views.AddItemFragmentDialog;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by iKar on 11/5/15.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final static String TAG = MainFragment.class.getName();

    private RecyclerView recyclerView;

    public static final int DEFAULT_INVENTORY = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);

        getLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Faked Data
        defaultData();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String orderBy =  TableCashDesk._DENOMINATION + " ASC";
        return new CursorLoader(getActivity(), UriMatcherHelper.CONTENT_URI,
                null, null, null, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                /* In a real project we must apply some asynchronous mechanism */
                EditText editTextAMount
                        = (EditText) getActivity().findViewById(R.id.fragment_main_et_amount);
                String amountText = editTextAMount.getText().toString();
                int amount = checkInputAmount(getActivity(), amountText, "Please enter amount");
                if(amount > 0)
                    calculateCashDesk(amount);

                editTextAMount.setText("");
                hideKeyboard();

                break;
        }
    }

    private void calculateCashDesk(int amount) {
        List<CashDeskItem> cashDeskItems = new ArrayList<>();

        String orderBy =  TableCashDesk._DENOMINATION + " ASC";
        String[] args = new String[]{String.valueOf(amount)};
        Cursor cursor = getActivity().getContentResolver()
                .query(UriMatcherHelper.CONTENT_URI, null,
                        TableCashDesk._DENOMINATION +"<=?", args, orderBy);

        if(cursor != null) {
            ColumnIndexCache cache = new ColumnIndexCache();
            while (cursor.moveToNext())
                cashDeskItems.add(new CashDeskItem(cursor, cache));
            cursor.close();
        }

        ICash cash = DaggerICashComponent.create().getCash();
        Map<Integer, Integer> result = cash.getAmount(cashDeskItems, cashDeskItems.size() - 1, amount);
        if(result != null && result.size() > 0) {
            //Succesful transaction
            for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
                String[] selectionArgs = new String[]{String.valueOf(entry.getKey())};
                ContentValues contentValues = new ContentValues();
                contentValues.put(TableCashDesk._INVENTORY, entry.getValue());
                getActivity().getContentResolver().update(UriMatcherHelper.CONTENT_URI, contentValues,
                        TableCashDesk._DENOMINATION +"=?", selectionArgs);
            }
            transactionDialog("Transaction succesful.", "Please get your "
                    + Integer.toString(amount) + "$");
        } else {
            transactionDialog("Transaction failed.", "Sorry, not enough cash.");
        }
    }

    private void transactionDialog(String title, String message) {
        Log.e(TAG, "Show dialog");
        new AlertDialog.Builder(getActivity())
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private final String SHARED_PREFS_NAME = "Prefs";
    private final String FIRST_LAUNCH_PARAM = "FirstLaunch";

    private boolean isFirstLaunch() {
        SharedPreferences mSettings
                = getActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return mSettings.getBoolean(FIRST_LAUNCH_PARAM, false);
    }

    private void setFirstLaunch(boolean value) {
        SharedPreferences mSettings
                = getActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(FIRST_LAUNCH_PARAM, value);
        editor.apply();
    }

    private void defaultData() {
        if(!isFirstLaunch()) {
            addDefaultDataToDb();
            setFirstLaunch(true);
        }
    }

    public void addData() {
        AddItemFragmentDialog addItemFragmentDialog = new AddItemFragmentDialog();
        addItemFragmentDialog.show(getFragmentManager(), "AddFragmentDialog");
    }

    public void resetData() {
        getActivity().getContentResolver().delete(UriMatcherHelper.CONTENT_URI, null, null);
        addDefaultDataToDb();
    }

    private void addDefaultDataToDb() {
        List<Integer> denominationList = new ArrayList<>(Arrays.asList(1, 5, 10, 20, 100));

        for (Integer item : denominationList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TableCashDesk._DENOMINATION, item);
            contentValues.put(TableCashDesk._INVENTORY, DEFAULT_INVENTORY);
            getActivity().getContentResolver().insert(UriMatcherHelper.CONTENT_URI, contentValues);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
