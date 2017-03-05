package com.ikar.atm;

import android.app.Application;

/**
 * Created by igorkarun on 3/3/17.
 */

public class App extends Application {

    private static App _instance;

    public static App instance() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (_instance == null) {
            _instance = this;
        }

    }
}
