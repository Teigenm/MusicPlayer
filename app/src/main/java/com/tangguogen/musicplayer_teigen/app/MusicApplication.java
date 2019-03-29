package com.tangguogen.musicplayer_teigen.app;

import android.app.Application;
import android.content.Context;

import com.tangguogen.musicplayer_teigen.utils.Toaster;

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
