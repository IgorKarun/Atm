package com.ikar.grossumtesttask.views;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ikar.grossumtesttask.MainFragment;
import com.ikar.grossumtesttask.R;
import com.ikar.grossumtesttask.db.UriMatcherHelper;
import com.ikar.grossumtesttask.db.scheme.TableCashDesk;

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

                int bet = MainFragment.checkInputAmount(getActivity(), strBet, "Please enter a bet");
                if(bet == 0) return;
                int amount = MainFragment.checkInputAmount(getActivity(), strAmount, "Please enter amount");
                if(amount == 0) return;

                String[] selectionArgs=new String[]{String.valueOf(bet)};
                Cursor cursor = getActivity().getContentResolver().query(UriMatcherHelper.CONTENT_URI, null,
                        TableCashDesk._DENOMINATION + "=?", selectionArgs ,null);
                if(cursor == null || cursor.getCount() > 0) {
                    Toast.makeText(getActivity(), "Sorry this BET is present in DB",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(TableCashDesk._DENOMINATION, bet);
                    contentValues.put(TableCashDesk._INVENTORY, amount > 0 ? amount : MainFragment.DEFAULT_INVENTORY);
                    getActivity().getContentResolver().insert(UriMatcherHelper.CONTENT_URI, contentValues);
                    AddItemFragmentDialog.this.dismiss();
                }
            }
        });

        return view;
    }
}
