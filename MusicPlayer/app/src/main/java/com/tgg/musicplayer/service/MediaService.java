package com.tgg.musicplayer.service;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.tgg.musicplayer.model.MusicEntity;
import com.tgg.musicplayer.utils.loader.AudioEntity;
import com.tgg.musicplayer.utils.log.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class MediaService extends Service {

    private MyBinder mBinder = new MyBinder();
    private MediaPlayer mMediaPlayer = null;
    private List<MusicEntity> mMusicList;
    private int mPos ;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mMusicList = new ArrayList<MusicEntity>();
        mPos = 0;
        Logger.d("创建");
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        Logger.d("启动");
        return super.onStartCommand(intent,flags,startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("绑定");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        Logger.d("销毁");
        super.onDestroy();
    }


    public class MyBinder extends Binder {

        public MediaService getInstance() {
            return MediaService.this;
        }


        public void initMediaPlayer(){
            if(mPos<0 || mPos >= mMusicList.size()) {
                return ;
            }
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(mMusicList.get(mPos).getPath());
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void playMusic() {
            mMediaPlayer.start();
        }

        public void pauseMusic() {
            mMediaPlayer.pause();
        }
        public boolean isPlayingMusic() {
            return mMediaPlayer.isPlaying();
        }
        public void nextMusic() {
            if (mMediaPlayer == null) {
                return ;
            }
            mPos = (mPos +1) % mMusicList.size();
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
            initMediaPlayer();
            playMusic();
        }
        public void resetMusic() {
            if (!mMediaPlayer.isPlaying()) {
                initMediaPlayer();
            }
        }

        public void closeMusic() {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
        }

        public int getDuration() {
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

        public MusicEntity getMusicEntity () {
            return mMusicList.get(mPos);
        }
    }

}
