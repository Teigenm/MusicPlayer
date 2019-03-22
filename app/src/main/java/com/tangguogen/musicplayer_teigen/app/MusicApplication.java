package com.tangguogen.musicplayer_teigen.app;

import android.app.Application;
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
import android.content.Context;
>>>>>>> commit
>>>>>>> create

import com.tangguogen.musicplayer_teigen.utils.Toaster;

public class MusicApplication extends Application {
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
    private static Context context;
>>>>>>> commit
>>>>>>> create
    @Override
    public void onCreate(){
        super.onCreate();
        Toaster.initialize(this);
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
        context = this;
    }
    public static Context getContext(){
        return context;
>>>>>>> commit
>>>>>>> create
    }
}
