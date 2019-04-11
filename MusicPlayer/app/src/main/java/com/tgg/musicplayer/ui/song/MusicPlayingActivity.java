package com.tgg.musicplayer.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.andremion.music.MusicCoverView;
import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.BaseActivity;
import com.tgg.musicplayer.app.UserManager;
import com.tgg.musicplayer.model.MusicEntity;
import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.ui.view.SelectPlayListPopup;
import com.tgg.musicplayer.ui.view.SelectSongOperationPopup;

import java.text.SimpleDateFormat;


public class MusicPlayingActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = MusicPlayingActivity.class.getSimpleName();
    private ImageView mPlayingFinishImageView;
    private ImageView mPlayingSongOperationImageView;
    private LinearLayout mAllLayout;
    private ImageView mSongListImageView;
    private MusicCoverView mMusicCoverView;
    private ImageView mLastMusicImageView;
    private ImageView mNextMusicImageView;
    private ImageView mControlImageView;
    private TextView mNowSongTimeTextView;
    private TextView mTotalSongTimeTextView;
    private TextView mSongNameTextView;
    private TextView mSingerNameTextView;
    private SeekBar mSeekBar;

    private MediaService.MyBinder mMyBinder;
    private Handler mHandler = new Handler();
    private SimpleDateFormat timeFormat = new SimpleDateFormat("m:ss");

    private static MusicPlayingActivity sMusicPlayingActivity ;

    public static MusicPlayingActivity getInstance() {
        return sMusicPlayingActivity;
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
        sMusicPlayingActivity = MusicPlayingActivity.this;
        initView();

        init();
    }

    private void init() {
        mMyBinder = UserManager.getInstance().getMyBinder();
        if(mMyBinder.getMusicList().size()>0){
            initMusic();
            mHandler.post(mRunnable);
        }
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
    public void initMusic() {
        MusicEntity entity = mMyBinder.getMusicEntity();
        mSeekBar.setMax(entity.getDuration());
        mTotalSongTimeTextView.setText(timeFormat.format(mMyBinder.getDuration()));
        mSongNameTextView.setText(entity.getSongName());
        mSingerNameTextView.setText(entity.getSingerName());
        if(mMyBinder.isPlayingMusic()) {
            mControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_pause,null));
        }else {
            mControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_start,null));
        }

    }
    public void controlMusic() {
        if(mMyBinder.isPlayingMusic()) {
            mMyBinder.pauseMusic();
           // mMusicCoverView.stop();
            mControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_start,null));
        } else {
            mMyBinder.playMusic();
            //mMusicCoverView.start();
            mControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_pause,null));
        }
    }

    public void nextMusic() {
        mMyBinder.nextMusic();
        initMusic();
    }

    public void lastMusic() {
        mMyBinder.lastMusic();
        initMusic();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            initMusic();
            if(mMyBinder.getPlayPosition() >= mMyBinder.getDuration() - 1000) {
                nextMusic();
            }
            mNowSongTimeTextView.setText(timeFormat.format(mMyBinder.getPlayPosition()));
            mSeekBar.setProgress(mMyBinder.getPlayPosition());
            mHandler.postDelayed(mRunnable,1000);
        }
    };
}
