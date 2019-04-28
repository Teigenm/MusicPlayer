package com.tgg.musicplayer.service;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.tgg.musicplayer.app.UserManager;
import com.tgg.musicplayer.storage.database.AppDatabase;
import com.tgg.musicplayer.storage.database.dao.RecentMusicDao;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.storage.database.table.RecentMusicEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
/**
  *
  * @Author:         tgg
  * @CreateDate:     2019/4/20 21:27
  * @Description:     mPlayMod 0 顺序播放 1 随机播放
  * @Version:        1.0
 */

public class MediaService extends Service {

    private MyBinder mBinder = new MyBinder();
    private MediaPlayer mMediaPlayer = null;
    private List<MusicEntity> mMusicList;
    private int mPos ;
    private int mPlayMode = 0;
    private int mPreciousPos = -1;

    private CompositeDisposable mDisposable;
    private RecentMusicDao mRecentMusicDao;
    private int mPlayFlag ;
    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mDisposable = new CompositeDisposable();
        mRecentMusicDao = AppDatabase.getInstance().getRecentMusicDao();
        mPlayFlag = 0;
        mPlayMode = 0;
        mPreciousPos = -1;
        mMusicList = new ArrayList<>();
        //Logger.d("创建");
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        //Logger.d("启动");
        return super.onStartCommand(intent,flags,startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Logger.d("绑定");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        //Logger.d("销毁");
        super.onDestroy();
        mDisposable.clear();
    }


    public class MyBinder extends Binder {

        public MediaService getInstance() {
            return MediaService.this;
        }


        public void initMediaPlayer(){
            if(mPos<0 || mPos >= mMusicList.size()) {
                return ;
            }
            changeListPlayState(mPreciousPos,false);
            changeListPlayState(mPos,true);
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(mMusicList.get(mPos).getPath());
                mMediaPlayer.prepare();
                mPlayFlag = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        public void playMusic() {
            mMediaPlayer.start();
            if(mPlayFlag == 1 ) {
                return ;
            }
            mPlayFlag = 1;
            mDisposable.add(Completable.fromAction(() -> {
                MusicEntity musicEntity = mMusicList.get(mPos);
                RecentMusicEntity recentMusicEntity = mRecentMusicDao.getRecentMusicById(musicEntity.getId());
                if(recentMusicEntity == null) {
                    recentMusicEntity = new RecentMusicEntity();
                    recentMusicEntity.setMusicId(musicEntity.getId());
                    recentMusicEntity.setPlayTimes(1);
                    recentMusicEntity.setAddTime(System.currentTimeMillis());
                    mRecentMusicDao.add(recentMusicEntity);
                } else {
                    recentMusicEntity.setPlayTimes(recentMusicEntity.getPlayTimes()+1);
                    recentMusicEntity.setAddTime(System.currentTimeMillis());
                    mRecentMusicDao.update(recentMusicEntity);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, throwable -> {
                    }));
        }

        public void pauseMusic() {
            mMediaPlayer.pause();
        }
        public boolean isPlayingMusic() {
            return mMediaPlayer.isPlaying();
        }
        public void nextMusic() {
            mPreciousPos = mPos;
            if(mPlayMode == 0){
                mPos = (mPos +1) % mMusicList.size();
            } else {
                mPos = randomMusic();
            }
            if(mPos < 0) {
                mPos = 0;
            }
            if (mMediaPlayer == null || mPos < 0 || mPos >= mMusicList.size()) {
                return ;
            }
            initMediaPlayer();
            playMusic();
        }
        public void changeListPlayState(int pos,boolean state) {
            if(pos < 0 || pos >= mMusicList.size() ) {
                return ;
            }
            MusicEntity entity = mMusicList.get(pos);
            entity.setIsPlaying(state);
        }
        public void preciousMusic() {
            mPreciousPos = mPos;
            if(mPlayMode == 0){
                mPos--;
                if (mPos < 0) {
                    mPos = mMusicList.size()-1;
                }
            } else {
                mPos = randomMusic();
            }
            if(mPos >= mMusicList.size()) {
                mPos = mMusicList.size()-1;
            }
            if (mMediaPlayer == null || mPos < 0 || mPos >= mMusicList.size()) {
                return ;
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
            mPreciousPos = -1;
            mMusicList = list;
            for(int i = 0;i<mMusicList.size();i++) {
                mMusicList.get(i).setIsPlaying(false);
            }
        }

        public List<MusicEntity> getMusicList() {
            return mMusicList;
        }

        public void setPos(int pos) {
            mPreciousPos = mPos;
            mPos = pos;
        }

        public int getPos() {
            return mPos;
        }

        public MusicEntity getMusicEntity () {
            return mMusicList.get(mPos);
        }

        public int getListSize() {
            return mMusicList.size();
        }

        public void setPlayMode(int playMode) {
            mPlayMode = playMode;
        }

        public int getPlayMode() {
            return mPlayMode;
        }

        public void setPreciousPos(int preciousPos) {
            mPreciousPos = preciousPos;
        }

        public int getPreciousPos() {
            return mPreciousPos;
        }
        public int randomMusic() {
            if(mMusicList.size() == 1) {
                return 0 ;
            }
            int num = (int) (Math.random()*mMusicList.size());
            if(num == mPos) {
                num = randomMusic();
            }
            return num ;
        }
    }

}
