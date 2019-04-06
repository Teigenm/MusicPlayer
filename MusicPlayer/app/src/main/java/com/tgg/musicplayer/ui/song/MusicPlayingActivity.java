package com.tgg.musicplayer.ui.song;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.andremion.music.MusicCoverView;
import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.BaseActivity;
import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.ui.view.SelectPlayListPopup;
import com.tgg.musicplayer.ui.view.SelectSongOperationPopup;
import com.tgg.musicplayer.utils.log.Logger;
import com.tgg.musicplayer.utils.media.MusicList;

import java.text.SimpleDateFormat;


public class MusicPlayingActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = MusicPlayingActivity.class.getSimpleName();
    private ImageView mPlayingFinishImageView;
    private ImageView mPlayingSongOperationImageView;
    private RelativeLayout mAllLayout;
    private ImageView mSongListImageView;
    private MusicCoverView mMusicCoverView;
    private ImageView mLastMusicImageView;
    private ImageView mNextMusicImageView;
    private TextView mNowSongTimeTextView;
    private TextView mTotalSongTimeTextView;
    private SeekBar mSeekBar;

    private Handler mHandler = new Handler();
    private Intent mMediaServiceIntent;
    private MediaService.MyBinder mMyBinder;

    private SimpleDateFormat timeFormat = new SimpleDateFormat("m:ss");

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (MediaService.MyBinder) service;
            mMyBinder.setMusicList(MusicList.getMusicList(MusicPlayingActivity.this));
            mSeekBar.setMax(mMyBinder.getProgress());

            mTotalSongTimeTextView.setText(timeFormat.format(mMyBinder.getProgress()));
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
            mHandler.post(mRunnable);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static void go(Context context){
        Intent intent = new Intent();
        intent.setClass(context,MusicPlayingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playing_layout);
        initView();

        mMediaServiceIntent = new Intent(MusicPlayingActivity.this,MediaService.class);
        bindService(mMediaServiceIntent,mServiceConnection,BIND_AUTO_CREATE);
    }
    private void initView(){

        mAllLayout = findViewById(R.id.music_playing_all_layout);
        mNowSongTimeTextView = findViewById(R.id.music_playing_now_song_time_text_view);
        mTotalSongTimeTextView = findViewById(R.id.music_playing_total_song_time_text_view);
        mSeekBar = findViewById(R.id.music_playing_seek_bar);

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
        mMusicCoverView.start();


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.music_playing_finish_image_view:
                finish();
                break;
            case R.id.music_playing_song_operation_image_view:
                SelectSongOperationPopup songOperationPopup = new SelectSongOperationPopup(this);
                songOperationPopup.showPopup(mAllLayout);
                break;
            case R.id.music_playing_song_list_image_view:
                SelectPlayListPopup playListPopup = new SelectPlayListPopup(this);
                playListPopup.showPopup(mAllLayout);
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
                default:break;
        }
    }

    public void controlMusic() {
        mMyBinder.controlMusic();
    }

    public void nextMusic() {
        mMyBinder.nextMusic();
    }

    public void lastMusic() {
        mMyBinder.lastMusic();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        mMyBinder.closeMusic();
        unbindService(mServiceConnection);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mNowSongTimeTextView.setText(timeFormat.format(mMyBinder.getPlayPosition()));
            mSeekBar.setProgress(mMyBinder.getPlayPosition());
            mHandler.postDelayed(mRunnable,1000);
        }
    };
}
