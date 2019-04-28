package com.tgg.musicplayer.ui.play;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.music.MusicCoverView;
import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.BaseActivity;
import com.tgg.musicplayer.app.UserManager;
import com.tgg.musicplayer.storage.database.AppDatabase;
import com.tgg.musicplayer.storage.database.dao.ListInMusicDao;
import com.tgg.musicplayer.storage.database.table.ListInMusicEntity;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.storage.sharedpreference.MusicSettingPreference;
import com.tgg.musicplayer.storage.sharedpreference.SharedPreferenceManager;
import com.tgg.musicplayer.ui.home.HomeActivity;
import com.tgg.musicplayer.ui.view.SelectPlayListPopup;
import com.tgg.musicplayer.ui.view.SelectSongOperationPopup;
import com.tgg.musicplayer.utils.DateTimeUtils;
import com.tgg.musicplayer.utils.Toaster;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class MusicPlayingActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mPlayingFinishImageView;
    private ImageView mPlayingSongOperationImageView;
    private LinearLayout mAllLayout;
    private ImageView mSongListImageView;
    private MusicCoverView mMusicCoverView;
    private ImageView mLastMusicImageView;
    private ImageView mNextMusicImageView;
    private ImageView mFavoriteMusicImageView;
    private ImageView mControlImageView;
    private TextView mNowSongTimeTextView;
    private TextView mTotalSongTimeTextView;
    private TextView mSongNameTextView;
    private TextView mSingerNameTextView;
    private ImageView mPlayModeImageView;
    private SeekBar mSeekBar;
    private SelectPlayListPopup mPlayListPopup;


    public static int sInitTitleFlag = 0;
    private MediaService.MyBinder mMyBinder;
    private MyHandler mHandler ;
    private int mFavoriteFlag;

    private CompositeDisposable mDisposable;
    private ListInMusicDao mListInMusicDao;
    private SharedPreferenceManager mManager = SharedPreferenceManager.getInstance();
    private MusicSettingPreference mSettingEntity = new MusicSettingPreference();

    private static MusicPlayingActivity sMusicPlayingActivity;
    private static Object sObject = new Object();

    public static MusicPlayingActivity getInstance() {
        synchronized (sObject) {
            return sMusicPlayingActivity;
        }
    }
    public static void go(Context context){
        Intent intent = new Intent();
        intent.setClass(context,MusicPlayingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playing_layout);
        sMusicPlayingActivity = this;

        initView();
        init();
    }

    private void init() {
        if(mHandler == null) {
            mHandler = new MyHandler(MusicPlayingActivity.this);
        }
        mDisposable = new CompositeDisposable();
        mListInMusicDao = AppDatabase.getInstance().getListInMusicDao();
        mMyBinder = UserManager.getInstance().getMyBinder();

        if(mMyBinder.getPlayMode() == 1) {
            mPlayModeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_random_play));
        } else {
            mPlayModeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_cycle_play));
        }

        if(mMyBinder.getPos()<0 || mMyBinder.getPos() >= mMyBinder.getListSize()){
            initNoMusic();
        } else {
            initMusic();
            mHandler.post(mRunnable);
        }
    }
    private void initNoMusic () {
        mSeekBar.setMax(0);
        mTotalSongTimeTextView.setText(DateTimeUtils.getMinuteTimeInString(0));
        mNowSongTimeTextView.setText(DateTimeUtils.getMinuteTimeInString(0));
        mSongNameTextView.setText(getResources().getString(R.string.label_music_player_welcome));
        mSingerNameTextView.setText("");
        mControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_start,null));
    }
    private void initView() {

        mAllLayout = findViewById(R.id.music_playing_all_layout);
        mNowSongTimeTextView = findViewById(R.id.music_playing_now_song_time_text_view);
        mTotalSongTimeTextView = findViewById(R.id.music_playing_total_song_time_text_view);
        mSeekBar = findViewById(R.id.music_playing_seek_bar);
        mSongNameTextView = findViewById(R.id.music_playing_song_name_text_view);
        mSingerNameTextView = findViewById(R.id.music_playing_singer_name_text_view);

        mPlayingFinishImageView = findViewById(R.id.music_playing_finish_image_view);
        mPlayingFinishImageView.setOnClickListener(MusicPlayingActivity.this);

        mPlayingSongOperationImageView = findViewById(R.id.music_playing_song_operation_image_view);
        mPlayingSongOperationImageView.setOnClickListener(MusicPlayingActivity.this);

        mSongListImageView = findViewById(R.id.music_playing_song_list_image_view);
        mSongListImageView.setOnClickListener(MusicPlayingActivity.this);

        mLastMusicImageView = findViewById(R.id.music_playing_last_music_image_view);
        mLastMusicImageView.setOnClickListener(MusicPlayingActivity.this);

        mNextMusicImageView = findViewById(R.id.music_playing_next_music_image_view);
        mNextMusicImageView.setOnClickListener(MusicPlayingActivity.this);

        mControlImageView = findViewById(R.id.music_playing_control_music_image_view);
        mControlImageView.setOnClickListener(MusicPlayingActivity.this);

        mFavoriteMusicImageView = findViewById(R.id.music_playing_favorite_song_image_view);
        mFavoriteMusicImageView.setOnClickListener(MusicPlayingActivity.this);

        mPlayModeImageView = findViewById(R.id.music_playing_play_mode_image_view);
        mPlayModeImageView.setOnClickListener(MusicPlayingActivity.this);

        mMusicCoverView = findViewById(R.id.music_playing_cover_view);
        mMusicCoverView.setCallbacks(new MusicCoverView.Callbacks() {
            @Override
            public void onMorphEnd(MusicCoverView coverView) {
                // Nothing to do
            }
            @Override
            public void onRotateEnd(MusicCoverView coverView) {
                supportFinishAfterTransition();
            }
        });
        //mMusicCoverView.start();
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    mMyBinder.seekToPosition(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.music_playing_finish_image_view:
                finish();
                break;
            case R.id.music_playing_song_operation_image_view:
                if(mMyBinder.getPos()>=0 && mMyBinder.getPos() < mMyBinder.getListSize()){
                    SelectSongOperationPopup songOperationPopup = new SelectSongOperationPopup(this,1,mMyBinder.getMusicEntity() );
                    songOperationPopup.showPopup(mAllLayout);
                } else {
                    Toaster.showToast(getResources().getString(R.string.message_no_music_to_look) );
                }
                break;
            case R.id.music_playing_song_list_image_view:
                mPlayListPopup = new SelectPlayListPopup(this);
                mPlayListPopup.showPopup(mAllLayout);
                break;
            case R.id.music_playing_last_music_image_view:
                lastMusic();
                break;
            case R.id.music_playing_next_music_image_view:
                nextMusic();
                break;
            case R.id.music_playing_control_music_image_view:
                controlMusic();
                break;
            case R.id.music_playing_favorite_song_image_view:
                addMyFavorite();
                break;
            case R.id.music_playing_play_mode_image_view:
                changePlayMode();
                break;
                default:break;
        }
    }
    public void changePlayMode() {
        if(mMyBinder.getPlayMode() == 0) {
            mMyBinder.setPlayMode(1);
            mPlayModeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_random_play));
            Toast.makeText(getApplication(),getResources().getString(R.string.play_mode_random),Toast.LENGTH_SHORT).show();
        } else {
            mPlayModeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_cycle_play));
            mMyBinder.setPlayMode(0);
            Toast.makeText(getApplication(),getResources().getString(R.string.play_mode_cycle),Toast.LENGTH_SHORT).show();
        }
    }
    public void initMusic() {
        if(mMyBinder.getPos()<0 || mMyBinder.getPos() >= mMyBinder.getListSize()){
            return ;
        }
        if(sInitTitleFlag == 1) {
            return ;
        }
        MusicEntity musicEntity = mMyBinder.getMusicEntity();
        mSeekBar.setMax(musicEntity.getDuration());
        mTotalSongTimeTextView.setText(DateTimeUtils.getMinuteTimeInString(mMyBinder.getDuration()));
        mSongNameTextView.setText(musicEntity.getSongName());
        mSingerNameTextView.setText(musicEntity.getSingerName());
        if(mMyBinder.isPlayingMusic()) {
            mControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_pause,null));
        }else {
            mControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_start,null));
        }
        sInitTitleFlag = 1;

    }
    public void controlMusic() {
        if(mMyBinder.getPos()<0 || mMyBinder.getPos() >= mMyBinder.getListSize()){
            return ;
        }
        if(mMyBinder.isPlayingMusic()) {
            mMyBinder.pauseMusic();
            mControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_start,null));
        } else {
            mMyBinder.playMusic();
            mControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_pause,null));
        }
    }

    public void nextMusic() {
        mMyBinder.nextMusic();
        sInitTitleFlag = 0;
        if(mPlayListPopup !=null && mPlayListPopup.getPopupWindow() != null && mPlayListPopup.getPopupWindow().isShowing()) {
            mPlayListPopup.changeListItem(mMyBinder.getPreciousPos());
            mPlayListPopup.changeListItem(mMyBinder.getPos());
        }
        initMusic();
    }

    public void lastMusic() {
        mMyBinder.preciousMusic();
        sInitTitleFlag = 0;
        if(mPlayListPopup !=null && mPlayListPopup.getPopupWindow() != null && mPlayListPopup.getPopupWindow().isShowing()) {
            mPlayListPopup.changeListItem(mMyBinder.getPreciousPos());
            mPlayListPopup.changeListItem(mMyBinder.getPos());
        }
        initMusic();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            initMusic();
            if(mMyBinder.getPlayPosition() >= mMyBinder.getDuration() - 1000) {
                nextMusic();
            }
            mNowSongTimeTextView.setText(DateTimeUtils.getMinuteTimeInString(mMyBinder.getPlayPosition()));
            mSeekBar.setProgress(mMyBinder.getPlayPosition());
            mHandler.postDelayed(mRunnable,1000);
        }
    };
    static class MyHandler extends Handler {
        private final WeakReference mActivty;

        public MyHandler(MusicPlayingActivity activity){
            mActivty =new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
    public void addMyFavorite() {
        mFavoriteFlag = 0;
        mDisposable.add(Completable.fromAction(() -> {
            MusicEntity musicEntity = mMyBinder.getMusicEntity();
            ListInMusicEntity listInMusicEntity = new ListInMusicEntity(2,musicEntity.getId());
            if(mListInMusicDao.getListInMusic(listInMusicEntity.getSongListId() ,listInMusicEntity.getMusicId() ) != null) {
                mFavoriteFlag = 1;
            } else {
                mListInMusicDao.add(listInMusicEntity);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if(mFavoriteFlag == 1) {
                        Toaster.showToast(getResources().getString(R.string.message_favorite_music_fail));
                    } else {
                        Toaster.showToast(getResources().getString(R.string.message_favorite_music_success));
                    }
                }, throwable -> {

                }));
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        sInitTitleFlag = 0;
        if(mHandler != null){
            mHandler.removeCallbacks(mRunnable);
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mDisposable.clear();
        HomeActivity.sInitTitleFlag = 0;
        HomeActivity.getInstance().initMusic();
    }

}
