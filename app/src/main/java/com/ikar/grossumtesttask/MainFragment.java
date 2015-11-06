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
import com.ikar.grossumtesttask.data.CashDeskItem;
import com.ikar.grossumtesttask.db.DbHelper;
import com.ikar.grossumtesttask.db.UriMatcherHelper;
import com.ikar.grossumtesttask.views.AddItemFragmentDialog;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        String orderBy =  DbHelper._DENOMINATION + " ASC";
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

    private Map<Integer, Integer> balance;

    private void calculateCashDesk(int amount) {
        List<CashDeskItem> cashDeskItems = new ArrayList<>();
        balance = new HashMap<>();

        String orderBy =  DbHelper._DENOMINATION + " ASC";
        String[] args = new String[]{String.valueOf(amount)};
        Cursor cursor = getActivity().getContentResolver()
                .query(UriMatcherHelper.CONTENT_URI, null,
                        DbHelper._DENOMINATION +"<=?", args, orderBy);

        if(cursor != null) {
            while (cursor.moveToNext()) {
                int denomination = cursor.getInt(cursor.getColumnIndex(DbHelper._DENOMINATION));
                int count = cursor.getInt(cursor.getColumnIndex(DbHelper._INVENTORY));
                cashDeskItems.add(new CashDeskItem(denomination, count));
            }
            cursor.close();
        }

        boolean result = checkBets(cashDeskItems, cashDeskItems.size() - 1, amount);

        if(result) {
            //Succesful transaction
            for (Map.Entry<Integer, Integer> entry : balance.entrySet()) {
                String[] selectionArgs = new String[]{String.valueOf(entry.getKey())};
                ContentValues contentValues = new ContentValues();
                contentValues.put(DbHelper._INVENTORY, entry.getValue());
                getActivity().getContentResolver().update(UriMatcherHelper.CONTENT_URI, contentValues,
                        DbHelper._DENOMINATION +"=?", selectionArgs);
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

    /*
    Main Bet Algorithm
     */
    private boolean checkBets(List<CashDeskItem> cashDeskItems,
                              int noteItemNumber, int currentAmount) {
        if(noteItemNumber < 0) return false;
        if(currentAmount > 0) {
            final int defaultAmount = currentAmount;
            int betItem = cashDeskItems.get(noteItemNumber).getDenomination();
            int maxBetsPerAmount = (int) Math.floor(currentAmount / betItem);
            int maxBetsCount = cashDeskItems.get(noteItemNumber).getInventory();
            int minIterations = maxBetsPerAmount <= maxBetsCount ? maxBetsPerAmount : maxBetsCount;
            for(int i = minIterations; i >= 0; i--) {
                currentAmount = defaultAmount;
                balance.put(betItem, maxBetsCount - i);
                currentAmount = currentAmount - i * betItem;
                if(currentAmount == 0) {
                    return true;
                } else {
                    boolean status = checkBets(cashDeskItems, noteItemNumber - 1, currentAmount);
                    if(status)
                        return true;
                }
            }
        }

        return false;
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
            contentValues.put(DbHelper._DENOMINATION, item);
            contentValues.put(DbHelper._INVENTORY, DEFAULT_INVENTORY);
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
