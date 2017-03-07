package com.ikar.atm.main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ikar.atm.R;
import com.ikar.atm.common.utils.Utils;
import com.ikar.atm.common.views.AddItemFragmentDialog;
import com.ikar.atm.common.views.Dialogs;

import java.util.Formatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by iKar on 11/5/15.
 */
public class MainFragment extends Fragment implements IMainFragmentView {

    private final static String TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.rv_list) RecyclerView recyclerView;
    @BindView(R.id.action_bar) Toolbar actionBar;
    @BindView(R.id.fragment_main_et_amount) EditText editTextAMount;
    private MainFragmentPresenter presenter;
    private RecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity) getActivity()).setSupportActionBar(actionBar);
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

    @OnClick(R.id.fab)
    @Override
    public void giveMoney() {
        String amountText = editTextAMount.getText().toString();
        Integer amount = Utils.parseAmountFromText(amountText);
        if(amount != null && amount > 0) {
            presenter.calculateCashDesk(amount);
            editTextAMount.getText().clear();
        } else
            Toast.makeText(getActivity(), R.string.please_enter_amount, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateAdapter(Cursor cursor) {
        if (adapter == null) {
            adapter = new RecyclerViewAdapter(getActivity(), cursor);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                    DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        } else
            adapter.swapCursor(cursor);
    }

    @Override
    public void initLoading(LoaderManager.LoaderCallbacks<Cursor> loader) {
        getLoaderManager().initLoader(0, null, loader);
    }

    @Override
    public void showAddNewItemDialog(boolean value, int amount) {
        if (value) {
            Dialogs.transactionDialog(getActivity(), getString(R.string.transaction_successful),
                    new Formatter().format(getString(R.string.please_get_your_money), amount).toString());
        } else {
            Dialogs.transactionDialog(getActivity(), getString(R.string.transaction_failed),
                    getString(R.string.sorry_not_enough_cash));
        }
    }
}
