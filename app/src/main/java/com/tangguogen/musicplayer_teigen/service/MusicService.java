package com.tangguogen.musicplayer_teigen.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    private static final String TAG = MusicService.class.getSimpleName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
