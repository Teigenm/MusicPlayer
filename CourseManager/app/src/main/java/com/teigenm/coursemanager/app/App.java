package com.teigenm.coursemanager.app;

import android.app.Application;

import com.teigenm.coursemanager.database.AppDatabase;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.init(this);
    }
}
