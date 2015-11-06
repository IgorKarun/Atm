package com.ikar.grossumtesttask;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.ikar.grossumtesttask.adapters.RecyclerViewAdapter;
import com.ikar.grossumtesttask.data.CashDeskItem;
import com.ikar.grossumtesttask.db.DbHelper;
import com.ikar.grossumtesttask.db.UriMatcherHelper;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by iKar on 11/5/15.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;

    final int DEFAULT_INVENTORY = 10;

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
        return new CursorLoader(getActivity(), UriMatcherHelper.CONTENT_URI,
                null, null, null, null);
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
//                int amount = checkInputAmount();
//                if(amount > 0) {
//                    calculateCashDesk(amount);
//                }
                //TEST
                int amount = 16;
                List<CashDeskItem> cashDeskItems = new ArrayList<>();
                cashDeskItems.add(new CashDeskItem(1, 0));
                cashDeskItems.add(new CashDeskItem(3, 10));
                cashDeskItems.add(new CashDeskItem(5, 10));

                int cashDeskItemsCounter = cashDeskItems.size() - 1;
                final int defaultAmount = amount;
                //SORT 1st = MAX
                checkDenomination(cashDeskItems, cashDeskItemsCounter, amount);

                break;
        }
    }

    private void calculateCashDesk(int amount) {
        Map<Integer, Integer> totalCashBalance = new TreeMap<>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer lhs, Integer rhs) {
                        return rhs.compareTo(lhs);
                    }
                });

        Cursor cursor = getActivity().getContentResolver()
                .query(UriMatcherHelper.CONTENT_URI, null, null, null, null);

        if(cursor != null) {
            while (cursor.moveToNext()) {
                int denomination = cursor.getInt(cursor.getColumnIndex(DbHelper._DENOMINATION));
                int count = cursor.getInt(cursor.getColumnIndex(DbHelper._INVENTORY));
                totalCashBalance.put(denomination, count);
            }
            cursor.close();
        }

        Map<Integer, Integer> balance = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : totalCashBalance.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            if(amount >= entry.getKey()) {
                int result =(int) Math.floor(amount / entry.getKey());
                if(entry.getValue() >= result) {
                    int leftCount = entry.getValue() - result;
                    balance.put(entry.getKey(), leftCount);
                    amount = amount - result * entry.getKey();
                } else {
                    balance.put(entry.getKey(), 0);
                    amount = amount - entry.getValue() * entry.getKey();
                }
            }
        }

        List<CashDeskItem> cashDeskItems = new ArrayList<>();
        int cashDeskItemsCounter = cashDeskItems.size() - 1;
        final int defaultAmount = amount;
        //SORT 1st = MAX
        checkDenomination(cashDeskItems, cashDeskItemsCounter, amount);




        if(amount == 0) {
            //Succesful transaction
            for (Map.Entry<Integer, Integer> entry : balance.entrySet()) {
                String[] selectionArgs = new String[]{String.valueOf(entry.getKey())};
                ContentValues contentValues = new ContentValues();
                contentValues.put(DbHelper._INVENTORY, entry.getValue());
                getActivity().getContentResolver().update(UriMatcherHelper.CONTENT_URI, contentValues,
                        DbHelper._DENOMINATION +"=?", selectionArgs);
            }
        } else {
            //Error message
        }



    }

    private void checkDenomination(List<CashDeskItem> cashDeskItems,
            int noteItemNumber, int currentAmount) {
        if(noteItemNumber < 0)
            return;
        int cashItem = cashDeskItems.get(noteItemNumber).getDenomination();
        if(currentAmount > 0) {
            final int defaultAmount = currentAmount;
            int maxCashItemCounts =(int) Math.floor(currentAmount / cashItem);
            int maxAvailable = cashDeskItems.get(noteItemNumber).getInventory();
            int maxIterations = maxCashItemCounts <= maxAvailable ? maxCashItemCounts : maxAvailable;
            for(int i = maxIterations; i >= 0; i--) {
                currentAmount = defaultAmount;
                //balance.put(entry.getKey(), leftCount);
                currentAmount = currentAmount - i * cashItem;
                if(currentAmount == 0) {
                    Log.e("balance", "WIN");
                    return;
                } else {
                    checkDenomination(cashDeskItems, noteItemNumber - 1, currentAmount);
                }
            }
        }

    }

    private int checkInputAmount() {
        EditText editTextAMount
                = (EditText) getActivity().findViewById(R.id.fragment_main_et_amount);
        String amountText = editTextAMount.getText().toString();
        if(!TextUtils.isEmpty(amountText)) {
            Pattern p = Pattern.compile("^[1-9]\\d*$");
            Matcher m = p.matcher(amountText);
            if(m.matches()) {
                int value = Integer.parseInt(amountText);
                return value;
            }
        } else {
            Toast.makeText(getActivity(), "Please enter amount.", Toast.LENGTH_LONG).show();
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
}
