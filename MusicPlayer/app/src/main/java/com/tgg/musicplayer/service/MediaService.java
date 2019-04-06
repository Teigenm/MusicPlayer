package com.tgg.musicplayer.service;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.tgg.musicplayer.model.MusicEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class MediaService extends Service {

    private static final String TAG = MediaService.class.getSimpleName();
    private MyBinder mBinder = new MyBinder();
    private MediaPlayer mMediaPlayer = null;
    private List<MusicEntity> mMusicList;
    private int mPos = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mMusicList = new ArrayList<MusicEntity>();
        mPos = 0;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        return super.onStartCommand(intent,flags,startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public class MyBinder extends Binder {

        public MediaService getInstance() {
            return MediaService.this;
        }

        public MediaPlayer getMediaPlayer() {
           return mMediaPlayer;
        }
        public void initMediaPlayer(){
            try {
                mMediaPlayer.setDataSource(mMusicList.get(mPos).getPath());
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void playMusic() {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        }

        public void pauseMusic() {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
        public void controlMusic() {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }
        public void nextMusic() {
            if (mMediaPlayer == null) {
                return ;
            }
            mPos = (mPos +1) % mMusicList.size();
            mMediaPlayer.reset();
            initMediaPlayer();
            playMusic();
        }
        public void lastMusic() {
            if (mMediaPlayer == null) {
                return ;
            }
            mPos--;
            if (mPos == -1) {
                mPos = mMusicList.size()-1;
            }
            mMediaPlayer.reset();
            initMediaPlayer();
            playMusic();
        }
        public void resetMusic() {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
                initMediaPlayer();
            }
        }

        public void closeMusic() {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
        }

        public int getProgress() {
            return mMediaPlayer.getDuration();
        }

        public int getPlayPosition() {
            return mMediaPlayer.getCurrentPosition();
        }

        public void seekToPosition(int purpose) {
            mMediaPlayer.seekTo(purpose);
        }

        public void setMusicList (List<MusicEntity> list) {
            mMusicList = list;
        }

        public List<MusicEntity> getMusicList() {
            return mMusicList;
        }

        public void setPos(int pos) {
            mPos = pos;
        }

        public int getPos() {
            return mPos;
        }
    }

}
