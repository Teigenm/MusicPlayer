package com.tgg.musicplayer.ui.home;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.BaseActivity;
import com.tgg.musicplayer.app.UserManager;
import com.tgg.musicplayer.model.MusicEntity;
import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.ui.song.AllSongActivity;
import com.tgg.musicplayer.ui.song.MusicPlayingActivity;
import com.tgg.musicplayer.ui.song.MyFavoriteActivity;
import com.tgg.musicplayer.ui.song.RecentReleaseActivity;
import com.tgg.musicplayer.ui.view.SelectPlayListPopup;
import com.tgg.musicplayer.utils.Toaster;
import com.tgg.musicplayer.utils.loader.MusicLoader;
import com.tgg.musicplayer.utils.log.Logger;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    private long mLastPressedTime = 0;
    private DrawerLayout mDrawerLayout;
    private ConstraintLayout mHistoryLayout;
    private LinearLayout mItemAllSongLayout;
    private LinearLayout mItemMyFavoriteLayout;
    private LinearLayout mItemRecentReleaseLayout;
    private ImageView mHistoryImageView;
    private ImageView mPlayingSongListImageView;
    private ImageView mPlayingControlImageView;
    private LinearLayout mPlayingMusicImageLayout;
    private LinearLayout mPlayingMusicTextLayout;
    private TextView mSongNameTextView;
    private TextView mSingerNameTextView;
    private SeekBar mSeekBar;

    private MediaService.MyBinder mMyBinder;
    private Handler mHandler = new Handler();
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (MediaService.MyBinder) service;
            UserManager.getInstance().setMyBinder(mMyBinder);
            mMyBinder.setMusicList(MusicLoader.getMusicList(HomeActivity.this));
            mMyBinder.setPos(0);
            mMyBinder.initMediaPlayer();
            initMusic();
            mHandler.post(mRunnable);
            Logger.d("Service与Activity已连接");
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };

    public static void go(Context context){
        Intent intent = new Intent();
        intent.setClass(context,HomeActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);

        initView();

        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            initService();
        }
    }

    private void initService() {

        Intent mediaServiceIntent = new Intent(HomeActivity.this,MediaService.class);
        UserManager.getInstance().setServiceConnection(mServiceConnection);
        bindService(mediaServiceIntent,mServiceConnection,this.BIND_AUTO_CREATE);

    }
    private void initView() {
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.home_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.text_navigation_drawer_open, R.string.text_navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.home_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSingerNameTextView = findViewById(R.id.home_playing_singer_name_text_view);
        mSongNameTextView = findViewById(R.id.home_playing_song_name_text_view);

        mHistoryLayout = findViewById(R.id.home_history_layout);
        mHistoryLayout.setOnClickListener(HomeActivity.this);

        mHistoryImageView = findViewById(R.id.home_history_image_view);
        mHistoryImageView.setOnClickListener(HomeActivity.this);

        mItemAllSongLayout = findViewById(R.id.home_item_all_song_layout);
        mItemAllSongLayout.setOnClickListener(HomeActivity.this);

        mItemRecentReleaseLayout = findViewById(R.id.home_item_recent_release_layout);
        mItemRecentReleaseLayout.setOnClickListener(HomeActivity.this);

        mItemMyFavoriteLayout = findViewById(R.id.home_item_my_favorite_layout);
        mItemMyFavoriteLayout.setOnClickListener(HomeActivity.this);

        mPlayingSongListImageView = findViewById(R.id.home_playing_song_list_image_view);
        mPlayingSongListImageView.setOnClickListener(HomeActivity.this);

        mPlayingControlImageView = findViewById(R.id.home_playing_control_image_view);
        mPlayingControlImageView.setOnClickListener(HomeActivity.this);

        mPlayingMusicImageLayout = findViewById(R.id.home_playing_music_image_layout);
        mPlayingMusicImageLayout.setOnClickListener(HomeActivity.this);

        mPlayingMusicTextLayout = findViewById(R.id.home_playing_music_text_layout);
        mPlayingMusicTextLayout.setOnClickListener(HomeActivity.this);

        mSeekBar = findViewById(R.id.home_seek_bar);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);//指定Toolbar上的视图文件
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.menu_search:
                SearchMusicActivity.go(HomeActivity.this);
                break;
                default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_night_mode){
            Toaster.showToast("11111");
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.home_history_layout:
                HistoryActivity.go(HomeActivity.this);
                break;
            case R.id.home_history_image_view:
                HistoryActivity.go(HomeActivity.this);
                break;
            case R.id.home_item_all_song_layout:
                AllSongActivity.go(HomeActivity.this);
                break;
            case R.id.home_item_recent_release_layout:
                RecentReleaseActivity.go(HomeActivity.this);
                break;
            case R.id.home_item_my_favorite_layout:
                MyFavoriteActivity.go(HomeActivity.this);
                break;
            case R.id.home_playing_song_list_image_view:
                SelectPlayListPopup playListPopup = new SelectPlayListPopup(this);
                playListPopup.showPopup(mDrawerLayout);
                break;
            case R.id.home_playing_music_image_layout:
                MusicPlayingActivity.go(HomeActivity.this);
                break;
            case R.id.home_playing_music_text_layout:
                MusicPlayingActivity.go(HomeActivity.this);
                break;
            case R.id.home_playing_control_image_view:
                controlMusic();
                break;
            default:break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        long currentTime = System.currentTimeMillis();//获取第一次按键时间
        if (currentTime - mLastPressedTime > 2000) {
            Toaster.showToast("再按一次退出程序");
            mLastPressedTime = currentTime;
        } else {
            finish();
        }
    }
    public void initMusic () {
        MusicEntity entity = mMyBinder.getMusicEntity();
        mSongNameTextView.setText(entity.getSongName());
        mSingerNameTextView.setText(entity.getSingerName());
        mSeekBar.setMax(entity.getDuration());
        if(mMyBinder.isPlayingMusic()) {
            mPlayingControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_pause,null));
        } else {
            mPlayingControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_start,null));
        }
    }
    public void controlMusic() {
        if(mMyBinder.isPlayingMusic()) {
            mMyBinder.pauseMusic();
            mPlayingControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_start,null));
        } else {
            mMyBinder.playMusic();
            mPlayingControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_pause,null));
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
    public void onRequestPermissionsResult(int requestCode,String[] permission,int[] grantResults) {
        switch (requestCode){
            case 1:if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                initService();
            }else{
                Toaster.showToast("无法获得权限,程序将退出！");
                finish();
            }
            break;
            default:break;
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
//        mMyBinder.closeMusic();
//        unbindService(mServiceConnection);
    }
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            initMusic();
            if(mMyBinder.getPlayPosition() >= mMyBinder.getDuration() - 1000) {
                nextMusic();
            }
            mSeekBar.setProgress(mMyBinder.getPlayPosition());
            mHandler.postDelayed(mRunnable,1000);
        }
    };
}
