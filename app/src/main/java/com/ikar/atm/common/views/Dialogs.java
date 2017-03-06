package com.ikar.atm.common.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.ikar.atm.R;

/**
 * Created by igorkarun on 3/4/17.
 */

public class Dialogs {

    private final static String TAG = Dialogs.class.getSimpleName();

    public static void transactionDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
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

}
