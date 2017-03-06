package com.ikar.atm.views;

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
import com.ikar.atm.utils.Utils;

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

                Integer bet = Utils.parseAmountFromText(strBet);
                if (bet == null || bet == 0) {
                    Toast.makeText(getActivity(), R.string.please_enter_a_bet, Toast.LENGTH_LONG).show();
                    return;
                }
                Integer amount = Utils.parseAmountFromText(strAmount);
                if (amount == null || amount == 0) {
                    Toast.makeText(getActivity(), R.string.please_enter_amount, Toast.LENGTH_LONG).show();
                    return;
                }

                if (DbQuery.checkIfBetPresent(bet)) {
                    Toast.makeText(getActivity(), R.string.sorry_this_bet_is_present_in_db,
                            Toast.LENGTH_SHORT).show();
                } else {
                    DbQuery.addNewCashDeskItem(amount, bet);
                    AddItemFragmentDialog.this.dismiss();
                }
            }
        });

        return view;
    }
}
