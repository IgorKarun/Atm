package com.ikar.grossumtesttask.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ikar.grossumtesttask.R;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by iKar on 11/5/15.
 */
public class MainFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, IMainFragmentView {

    private final static String TAG = MainFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private MainFragmentPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Toolbar action_bar = (Toolbar) rootView.findViewById(R.id.action_bar);
      //  action_bar.setTitle(R.string.events);
      //  action_bar.setNavigationIcon(R.drawable.ic_drawer);
        ((AppCompatActivity) getActivity()).setSupportActionBar(action_bar);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
//                /* In a real project we must apply some asynchronous mechanism */
//                EditText editTextAMount
//                        = (EditText) getActivity().findViewById(R.id.fragment_main_et_amount);
//                String amountText = editTextAMount.getText().toString();
//                int amount = checkInputAmount(getActivity(), amountText, "Please enter amount");
//                if(amount > 0)
//                    calculateCashDesk(amount);
//
//                editTextAMount.setText("");
//                hideKeyboard();

                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                return true;
            case R.id.action_reset:
                resetItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean addItem() {
        return false;
    }

    @Override
    public void resetItems() {

    }

    @Override
    public void giveMoney() {

    }
}
