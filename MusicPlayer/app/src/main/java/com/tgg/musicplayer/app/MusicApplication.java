package com.tgg.musicplayer.app;

import android.app.Application;
import android.content.Context;

import com.tgg.musicplayer.utils.Toaster;

public class MusicApplication extends Application {
    private static Context context;
    @Override
    public void onCreate(){
        super.onCreate();
        Toaster.initialize(this);
        context = this;
    }
    public static Context getContext(){
        return context;
    }
}
