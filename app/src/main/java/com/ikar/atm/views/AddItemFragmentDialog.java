package com.ikar.atm.views;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ikar.atm.db.DbQuery;
import com.ikar.atm.R;
import com.ikar.atm.db.UriMatcherHelper;
import com.ikar.atm.db.scheme.TableCashDesk;
import com.ikar.atm.main.MainFragmentPresenter;

/**
 * Created by iKar on 11/6/15.
 */
public class AddItemFragmentDialog extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_item, container, false);
        final EditText itemBet = (EditText) view.findViewById(R.id.dialog_add_item_bet);
        final EditText itemAmount = (EditText) view.findViewById(R.id.dialog_add_item_amount);

        // Watch for button clicks.
        Button button = (Button) view.findViewById(R.id.dialog_add_item_btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String strBet = itemBet.getText().toString();
                String strAmount = itemAmount.getText().toString();

                int bet = MainFragmentPresenter.checkInputAmount(getActivity(), strBet, "Please enter a bet");
                if (bet == 0) return;
                int amount = MainFragmentPresenter.checkInputAmount(getActivity(), strAmount, "Please enter amount");
                if (amount == 0) return;

                String[] selectionArgs = new String[]{String.valueOf(bet)};
                Cursor cursor = getActivity().getContentResolver().query(UriMatcherHelper.CONTENT_URI, null,
                        TableCashDesk._DENOMINATION + "=?", selectionArgs, null);
                if (cursor == null || cursor.getCount() > 0) {
                    Toast.makeText(getActivity(), "Sorry this BET is present in DB",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    DbQuery.addNewCashDeskItem(amount, bet);
                    AddItemFragmentDialog.this.dismiss();
                }
            }
        });

        return view;
    }
}
