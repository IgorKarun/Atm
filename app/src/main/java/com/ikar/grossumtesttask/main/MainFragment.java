package com.ikar.grossumtesttask.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;

import com.ikar.grossumtesttask.R;
import com.ikar.grossumtesttask.adapters.RecyclerViewAdapter;
import com.ikar.grossumtesttask.views.AddItemFragmentDialog;
import com.ikar.grossumtesttask.views.Dialogs;

/**
 * Created by iKar on 11/5/15.
 */
public class MainFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, IMainFragmentView {

    private final static String TAG = MainFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private MainFragmentPresenter presenter;
    private RecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView.ItemAnimator itemAnimator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Toolbar action_bar = (Toolbar) rootView.findViewById(R.id.action_bar);
      //  action_bar.setTitle(R.string.events);
      //  action_bar.setNavigationIcon(R.drawable.ic_drawer);
        ((AppCompatActivity) getActivity()).setSupportActionBar(action_bar);
        //setSupportActionBar(myToolbar);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);

        presenter = new MainFragmentPresenter(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Faked Data
        presenter.defaultData();
        presenter.initLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                /* In a real project we must apply some asynchronous mechanism */
                EditText editTextAMount = (EditText) getActivity().findViewById(R.id.fragment_main_et_amount);
                String amountText = editTextAMount.getText().toString();
                int amount = presenter.checkInputAmount(getActivity(), amountText);
                if(amount > 0)
                    presenter.calculateCashDesk(amount);

                editTextAMount.setText("");
                hideKeyboard();

                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addItem();
                break;
            case R.id.action_reset:
                resetItems();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addItem() {
        AddItemFragmentDialog addItemFragmentDialog = new AddItemFragmentDialog();
        addItemFragmentDialog.show(getFragmentManager(), "AddFragmentDialog");
    }

    @Override
    public void resetItems() {
        presenter.resetData();
    }

    @Override
    public void giveMoney() {

    }

    @Override
    public void updateAdapter(Cursor cursor) {
        if (adapter == null) {
            adapter = new RecyclerViewAdapter(getActivity(), cursor);
            layoutManager = new LinearLayoutManager(getActivity());
            itemAnimator = new DefaultItemAnimator();

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(itemAnimator);
        } else {
            adapter.swapCursor(cursor);
        }
    }

    @Override
    public void initLoading(LoaderManager.LoaderCallbacks<Cursor> loader) {
        getLoaderManager().initLoader(0, null, loader);
    }

    @Override
    public void showAddNewItemDialog(boolean value, int amount) {
        if (value) {
            Dialogs.transactionDialog(getActivity(), "Transaction succesful.", "Please get your "
                    + Integer.toString(amount) + "$");
        } else {
            Dialogs.transactionDialog(getActivity(), "Transaction failed.", "Sorry, not enough cash.");
        }
    }
}
