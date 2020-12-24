package com.example.note;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
