package com.tangguogen.musicplayer_teigen.app.service;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    private static final String TAG = MusicService.class.getSimpleName();
    private static final int MUSIC_CONTROL_PLAY = 1;
    private static final int MUSIC_CONTROL_PAUSE = 0;
    private MediaPlayer mediaPlayer;
    private String mSongPath;

    @Override
    public void onCreate(){
        super.onCreate();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return super.onStartCommand(intent,flags,startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initMediaPlayer(){

    }

}
