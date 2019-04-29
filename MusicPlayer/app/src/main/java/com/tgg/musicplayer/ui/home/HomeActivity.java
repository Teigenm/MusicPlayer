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
import android.os.Message;
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
import com.tgg.musicplayer.app.RecyclerViewTouchListener;
import com.tgg.musicplayer.app.UserManager;
import com.tgg.musicplayer.storage.database.AppDatabase;
import com.tgg.musicplayer.storage.database.dao.ListInMusicDao;
import com.tgg.musicplayer.storage.database.dao.MusicDao;
import com.tgg.musicplayer.storage.database.dao.RecentMusicDao;
import com.tgg.musicplayer.storage.database.dao.SongListDao;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.storage.database.table.RecentMusicEntity;
import com.tgg.musicplayer.storage.database.table.SongListEntity;
import com.tgg.musicplayer.storage.sharedpreference.MusicSettingPreference;
import com.tgg.musicplayer.storage.sharedpreference.SharedPreferenceManager;
import com.tgg.musicplayer.ui.history.HistoryActivity;
import com.tgg.musicplayer.ui.search.SearchMusicActivity;
import com.tgg.musicplayer.ui.song.AllSongActivity;
import com.tgg.musicplayer.ui.play.MusicPlayingActivity;
import com.tgg.musicplayer.ui.song.MyFavoriteActivity;
import com.tgg.musicplayer.ui.song.RecentReleaseActivity;
import com.tgg.musicplayer.ui.song.SongListActivity;
import com.tgg.musicplayer.ui.view.SelectPlayListPopup;
import com.tgg.musicplayer.ui.view.SelectSongListOperationPopup;
import com.tgg.musicplayer.utils.Toaster;
import com.tgg.musicplayer.utils.loader.MusicLoader;
import com.tgg.musicplayer.utils.log.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, RecyclerViewTouchListener.OnItemClickListener, RecyclerViewTouchListener.OnItemLongClickListener  {

    private long mLastPressedTime = 0;
    private DrawerLayout mDrawerLayout;
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
    private TextView mAllSongNumberTextView;
    private TextView mRecentReleaseNumberTextView;
    private TextView mMyFavoriteNumberTextView;
    private TextView mHistoryMostPlayNumberTextView;
    private TextView mSeeMoreTextView;
    private RecyclerView mRecyclerView;
    private SongListAdapter mAdapter;
    private TextView mCreatedSongListTextView;
    private TextView mNewSongListTextView;
    private ImageView mRightArrowImageView;
    private TextView mHistorySongNameTextView;
    private SelectPlayListPopup mPlayListPopup;

    private CompositeDisposable mDisposable;
    private AppDatabase mAppDatabase;
    private ListInMusicDao mListInMusicDao;
    private SongListDao mSongListDao;
    private MusicDao mMusicDao;
    private RecentMusicDao mRecentMusicDao;
    private SharedPreferenceManager mManager = SharedPreferenceManager.getInstance();
    private MusicSettingPreference mSettingEntity = new MusicSettingPreference();

    private List<MusicEntity> mAllList;
    private List<MusicEntity> mMusicList;
    private List<SongListEntity> mSongListEntityList;
    private MediaService.MyBinder mMyBinder;
    private MyHandler mHandler;
    private int mCount;
    private int mRecentReleaseNum = 0;
    private int mMyFavoriteNum = 0;
    private RecentMusicEntity mHistoryMostRecentEntity;
    private int mAllSongNum = 0;
    private MusicEntity mHistoryMostMusicEntity;


    public static int sInitTitleFlag = 0;
    private static HomeActivity sHomeActivity;
    private static Object mLock = new Object();

    public static HomeActivity getInstance() {
        synchronized (mLock) {
            return sHomeActivity;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (MediaService.MyBinder) service;
            UserManager.getInstance().setMyBinder(mMyBinder);
            mMyBinder.setMusicList(mMusicList);
            boolean success = mManager.load(HomeActivity.this, mSettingEntity);
            Logger.d(mSettingEntity);
            if(success && mCount > 0) {
                mMyBinder.setPlayMode(mSettingEntity.getPlayMode());
                UserManager.getInstance().setIsPlayListId(mSettingEntity.getIsPlayListId());
                mMyBinder.setPos(mSettingEntity.getPosition());
            } else {
                UserManager.getInstance().setIsPlayListId(-2);
                mMyBinder.setPlayMode(0);
                mMyBinder.setPos(0);
            }
            if(mMyBinder.getPos() >= 0 && mMyBinder.getPos() < mMyBinder.getListSize()) {
                mMyBinder.initMediaPlayer();
            } else {
                mMyBinder.setPos(-1);
                initNoMusic();
            }
            mHandler.post(mRunnable);
            //Logger.d("Service与Activity已连接");
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
        sHomeActivity = this;

        initView();
        initDao();
        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            initDate();
        }
    }

    private void initDao() {
        mDisposable = new CompositeDisposable();
        mAppDatabase = AppDatabase.getInstance();
        mListInMusicDao = mAppDatabase.getListInMusicDao();
        mSongListDao = mAppDatabase.getSongListDao();
        mMusicDao = mAppDatabase.getMusicDao();
        mRecentMusicDao = mAppDatabase.getRecentMusicDao();

        sInitTitleFlag = 0;
    }
    private void initService() {
        if(mHandler == null) {
            mHandler = new MyHandler(HomeActivity.this);
        }

        Intent mediaServiceIntent = new Intent(HomeActivity.this,MediaService.class);
        bindService(mediaServiceIntent,mServiceConnection,this.BIND_AUTO_CREATE);

    }

    public void initDate() {

        mDisposable.add(Completable.fromAction(() -> {
            mCount = mSongListDao.countSongList();
            if(mCount == 0) {
                mAllList = MusicLoader.getMusicList(HomeActivity.this);
                SongListEntity songListEntity = new SongListEntity(getResources().getString(R.string.text_play_list),System.currentTimeMillis(),"");
                mSongListDao.add(songListEntity);
                songListEntity = new SongListEntity(getResources().getString(R.string.text_my_favorite),System.currentTimeMillis(),"");
                mSongListDao.add(songListEntity);
                songListEntity = new SongListEntity(getResources().getString(R.string.text_my_song_list),System.currentTimeMillis(),getResources().getString(R.string.text_favorite_song));
                mSongListDao.add(songListEntity);
                mMusicDao.addAll(mAllList);
                mMusicList = new ArrayList<>();
            } else {
                mAllList = mMusicDao.getAll();
                mMusicList = mMusicDao.getMusicsByListId(1);

            }
            mMyFavoriteNum = mListInMusicDao.getListCount(2);
            mRecentReleaseNum = mRecentMusicDao.getAllCount();
            mAllSongNum = mMusicDao.getAllMusicCount();
            mSongListEntityList.clear();
            if(mRecentMusicDao.getAllCount() > 0) {
                mHistoryMostRecentEntity = mRecentMusicDao.getMaxTimesRecentEntity();
                mHistoryMostMusicEntity = mMusicDao.getMusicById(mHistoryMostRecentEntity.getMusicId());
            } else {
                mHistoryMostMusicEntity = new MusicEntity();
                mHistoryMostMusicEntity.setSongName("");
            }
            mSongListEntityList.addAll( mSongListDao.getAllSongList() );
            for(int i = 0; i< mSongListEntityList.size(); i++) {
                SongListEntity entity = mSongListEntityList.get(i);
                entity.setSongNumber(mListInMusicDao.getListCount(entity.getId()) );
            }
            Logger.d(mMusicList.toString());
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if(UserManager.getInstance().getMyBinder() == null) {
                        initService();
                    }
                    mAllSongNumberTextView.setText(String.valueOf(mAllSongNum ));
                    mMyFavoriteNumberTextView.setText(String.valueOf(mMyFavoriteNum) );
                    mRecentReleaseNumberTextView.setText(String.valueOf(mRecentReleaseNum) );
                    mHistoryMostPlayNumberTextView.setText(String.valueOf(mHistoryMostRecentEntity == null ? 0:mHistoryMostRecentEntity.getPlayTimes()) );
                    mHistorySongNameTextView.setText(mHistoryMostMusicEntity == null? "":mHistoryMostMusicEntity.getSongName());

                    String str = getString(R.string.replace_create_song_list);
                    str = str.replace("*",String.valueOf(mSongListEntityList.size() ));
                    mCreatedSongListTextView.setText(str);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    Logger.d(getResources().getString(R.string.error_load_date));
                }));

    }

    public void initView() {
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
        mAllSongNumberTextView = findViewById(R.id.home_item_all_music_number_text_view);
        mMyFavoriteNumberTextView = findViewById(R.id.home_item_my_favorite_text_view);
        mRecentReleaseNumberTextView = findViewById(R.id.home_item_recent_release_text_view);
        mHistoryMostPlayNumberTextView = findViewById(R.id.home_history_most_play_number_text_view);
        mCreatedSongListTextView = findViewById(R.id.home_created_song_list_text_view);
        mHistorySongNameTextView = findViewById(R.id.home_history_song_name_text_view);

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

        mSeeMoreTextView = findViewById(R.id.home_see_more_text_view);
        mSeeMoreTextView.setOnClickListener(HomeActivity.this);

        mNewSongListTextView = findViewById(R.id.home_new_song_list_text_view);
        mNewSongListTextView.setOnClickListener(HomeActivity.this);

        mRightArrowImageView = findViewById(R.id.home_right_arrow_image_view);
        mRightArrowImageView.setOnClickListener(HomeActivity.this);

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

        mRecyclerView = findViewById(R.id.home_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(HomeActivity.this,DividerItemDecoration.VERTICAL));
        mSongListEntityList = new ArrayList<>();
        mAdapter = new SongListAdapter(mSongListEntityList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(HomeActivity.this,this,this));
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
                SearchMusicActivity.go(HomeActivity.this,mAllList,getResources().getString(R.string.text_all_song));
                break;
                default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Toaster.showToast("功能未实现");
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.home_see_more_text_view:
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
                mPlayListPopup = new SelectPlayListPopup(this);
                mPlayListPopup.showPopup(mDrawerLayout);
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
            case R.id.home_new_song_list_text_view:
                EditOrAddSongListActivity.go(HomeActivity.this,0,-1);
                break;
            case R.id.home_right_arrow_image_view:
                EditOrAddSongListActivity.go(HomeActivity.this,0,-1);
                break;
            default:break;
        }
    }

    public void saveSetting() {
        mSettingEntity.setIsPlayListId(UserManager.getInstance().getIsPlayListId());
        mSettingEntity.setPlayMode(mMyBinder.getPlayMode() );
        mSettingEntity.setPosition(mMyBinder.getPos() );
        mManager.save(HomeActivity.this, mSettingEntity);
    }
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        long currentTime = System.currentTimeMillis();//获取第一次按键时间
        if (currentTime - mLastPressedTime > 2000) {
            Toaster.showToast(getString(R.string.message_press_again_finish));
            mLastPressedTime = currentTime;
        } else {
            if(mHandler != null){
                mHandler.removeCallbacks(mRunnable);
                mHandler.removeCallbacksAndMessages(null);
                mHandler = null;
            }
            mDisposable.clear();
            mMyBinder.closeMusic();
            unbindService(mServiceConnection);
            finish();
        }
    }
    public void initMusic () {
        if(mMyBinder.getPos()< 0 || mMyBinder.getPos() >= mMyBinder.getListSize()) {
            return ;
        }
        if(sInitTitleFlag == 1) {
            return ;
        }
        MusicEntity musicEntity = mMyBinder.getMusicEntity();
        mSongNameTextView.setText(musicEntity.getSongName());
        mSingerNameTextView.setText(musicEntity.getSingerName());
        mSeekBar.setMax(musicEntity.getDuration());
        if(mMyBinder.isPlayingMusic()) {
            mPlayingControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_pause,null));
        } else {
            mPlayingControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_start,null));
        }
        initDate();
        saveSetting();
        sInitTitleFlag = 1;
    }
    public void controlMusic() {
        if(mMyBinder.getPos()< 0 || mMyBinder.getPos() >= mMyBinder.getListSize()) {
            return ;
        }
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
        sInitTitleFlag = 0;
        if(mPlayListPopup !=null && mPlayListPopup.getPopupWindow() != null && mPlayListPopup.getPopupWindow().isShowing()) {
            mPlayListPopup.changeListItem(mMyBinder.getPreciousPos());
            mPlayListPopup.changeListItem(mMyBinder.getPos());
        }
        initMusic();
    }

    public void preciousMusic() {
        mMyBinder.preciousMusic();
        sInitTitleFlag = 0;
        if(mPlayListPopup !=null && mPlayListPopup.getPopupWindow() != null && mPlayListPopup.getPopupWindow().isShowing()) {
            mPlayListPopup.changeListItem(mMyBinder.getPreciousPos());
            mPlayListPopup.changeListItem(mMyBinder.getPos());
        }
        initMusic();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permission,int[] grantResults) {
        switch (requestCode){
            case 1:if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                initDate();
            }else{
                Toaster.showToast(getString(R.string.error_get_permission_and_finish));
                finish();
            }
            break;
            default:break;
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mHandler != null){
            mHandler.removeCallbacks(mRunnable);
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mDisposable.clear();
        mMyBinder.closeMusic();
        unbindService(mServiceConnection);
    }
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //Logger.d(mMyBinder.getPos());
            if(mMyBinder.getPos() >= 0 && mMyBinder.getPos() < mMyBinder.getListSize()) {
                initMusic();
                if(mMyBinder.getPlayPosition() >= mMyBinder.getDuration() - 1000) {
                    nextMusic();
                }
                mSeekBar.setProgress(mMyBinder.getPlayPosition());
            } else if(sInitTitleFlag == 0){
                initDate();
                sInitTitleFlag = 1;
            }
            mHandler.postDelayed(mRunnable,1000);
        }
    };
    public void initNoMusic () {
        mSongNameTextView.setText(getResources().getString(R.string.label_music_player_welcome));
        mSingerNameTextView.setText("");
        mSeekBar.setProgress(0);
        mSeekBar.setMax(0);
        mPlayingControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_border_start,null));
    }

    @Override
    public void onItemClick(View view, int position) {
        SongListActivity.go(HomeActivity.this,mSongListEntityList.get(position).getId() );
    }

    @Override
    public void onItemLongClick(View view, int position) {
        SelectSongListOperationPopup songListOperationPopup = new SelectSongListOperationPopup(this,mSongListEntityList.get(position).getId() );
        songListOperationPopup.showPopup(mDrawerLayout);
    }

    static class MyHandler extends Handler {
        private final WeakReference mActivty;

        public MyHandler(HomeActivity activity){
            mActivty =new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
