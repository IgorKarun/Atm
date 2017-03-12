package com.ikar.atm.common.views;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.ikar.atm.R;

/**
 * Created by igorkarun on 3/4/17.
 */

public class Dialogs {

    private final static String TAG = Dialogs.class.getSimpleName();

    public static void transactionDialog(final Context context, String title, String message) {
        new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok,(dialog, whichButton) -> dialog.cancel())
                .show();
    }

}
