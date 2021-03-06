package com.ikar.atm.common.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.ikar.atm.App;

/**
 * Created by igorkarun on 3/3/17.
 */

public class Shared {

    private static final String SHARED_PREFS_NAME = "Prefs";
    private static final String FIRST_LAUNCH_PARAM = "FirstLaunch";

    public static boolean isFirstLaunch() {
        return getSharedPrefs().getBoolean(FIRST_LAUNCH_PARAM, false);
    }

    public static void setFirstLaunch(boolean value) {
        SharedPreferences mSettings = getSharedPrefs();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(FIRST_LAUNCH_PARAM, value);
        editor.apply();
    }

    private static SharedPreferences getSharedPrefs() {
        return App.instance().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }
}
