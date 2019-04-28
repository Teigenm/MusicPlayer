package com.tgg.musicplayer.ui.song;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.AppBarStateChangeListener;
import com.tgg.musicplayer.app.BaseActivity;
import com.tgg.musicplayer.app.RecyclerViewTouchListener;
import com.tgg.musicplayer.app.UserManager;
import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.storage.database.AppDatabase;
import com.tgg.musicplayer.storage.database.dao.ListInMusicDao;
import com.tgg.musicplayer.storage.database.dao.MusicDao;
import com.tgg.musicplayer.storage.database.dao.SongListDao;
import com.tgg.musicplayer.storage.database.table.ListInMusicEntity;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.ui.home.HomeActivity;
import com.tgg.musicplayer.ui.search.SearchMusicActivity;
import com.tgg.musicplayer.ui.view.DialogFactory;
import com.tgg.musicplayer.ui.view.SelectSongOperationPopup;
import com.tgg.musicplayer.utils.Toaster;
import com.tgg.musicplayer.utils.log.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AllSongActivity extends BaseActivity implements View.OnClickListener,RecyclerViewTouchListener.OnItemClickListener, RecyclerViewTouchListener.OnItemLongClickListener{

    private static final int ALL_LIST_ID = 0;
    private CoordinatorLayout mCoordinatorLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private RecyclerView mRecyclerView;
    private List<MusicEntity> mList;
    private SingleSongListAdapter mAdapter;
    private FloatingActionButton mFab;
    private AppBarLayout mAppBarLayout;
    private TextView mToolbarTextView;
    private TextView mPlayNumberTextView;
    private ConstraintLayout mRandomPlayLayout;

    private CompositeDisposable mDisposable;
    private AppDatabase mAppDatabase;
    private ListInMusicDao mListInMusicDao;
    private SongListDao mSongListDao;
    private MusicDao mMusicDao;
    private AlertDialog mFavoriteAlertDialog;
    private MediaService.MyBinder mBinder;
    private MyHandler mHandler;
    private int mIsPlayListId;

    private static AllSongActivity sAllSongActivity;
    private static Object mLock = new Object();
    public static int sInitListFlag = 0;

    public static AllSongActivity getInstance() {
        synchronized (mLock) {
            return sAllSongActivity;
        }
    }

    public static void go(Context context){
        Intent intent = new Intent();
        intent.setClass(context,AllSongActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_song_layout);
        sAllSongActivity = this;

        initView();
        init();
    }
    private void initView() {
        Toolbar toolbar = findViewById(R.id.all_song_toolbar);
        setSupportActionBar(toolbar);

        Drawable drawable = toolbar.getNavigationIcon();
        if (drawable != null) {
            DrawableCompat.setTint(drawable, ResourcesCompat.getColor(getResources(), android.R.color.white, null));
        }

        mCoordinatorLayout = findViewById(R.id.all_song_coordinator_layout);
        mCollapsingToolbarLayout = findViewById(R.id.all_song_collapsing_layout);
        mCollapsingToolbarLayout.setExpandedTitleColor(ResourcesCompat.getColor(getResources(), android.R.color.white, null));

        mRandomPlayLayout = findViewById(R.id.single_song_list_random_play_layout);
        mRandomPlayLayout.setOnClickListener(AllSongActivity.this);

        mAppBarLayout = findViewById(R.id.all_song_app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                if( state == State.EXPANDED ) {
                    mToolbarTextView.setVisibility(View.INVISIBLE);
                }else if(state == State.COLLAPSED){
                    mToolbarTextView.setVisibility(View.VISIBLE);
                }else {
                    mToolbarTextView.setVisibility(View.INVISIBLE);
                }
            }
        });

        mFab = findViewById(R.id.all_song_fab);
        mFab.setOnClickListener(AllSongActivity.this);
        mPlayNumberTextView = findViewById(R.id.single_song_list_play_number_text_view);

        mToolbarTextView = findViewById(R.id.all_song_toolbar_text_view);

        mRecyclerView = findViewById(R.id.single_song_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AllSongActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(AllSongActivity.this,DividerItemDecoration.VERTICAL));
        mList = new ArrayList<>();
        mAdapter = new SingleSongListAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this,this,this));


        mFavoriteAlertDialog = DialogFactory.createAlertDialog(AllSongActivity.this,getResources().getString(R.string.message_confirm_favorite_song_list), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                favoriteMusic();
            }
        });
    }


    public void init() {
        if(mHandler == null) {
            mHandler = new MyHandler(AllSongActivity.this);
            mHandler.post(mRunnable);
        }
        mDisposable = new CompositeDisposable();
        mAppDatabase = AppDatabase.getInstance();
        mListInMusicDao = mAppDatabase.getListInMusicDao();
        mSongListDao = mAppDatabase.getSongListDao();
        mMusicDao = mAppDatabase.getMusicDao();
        mBinder = UserManager.getInstance().getMyBinder();
        mIsPlayListId = UserManager.getInstance().getIsPlayListId();

        initDate();
    }
    public void initDate() {

        mDisposable.add(Completable.fromAction(() -> {
            mList.clear();
            mList.addAll(mMusicDao.getAll() );
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if(mIsPlayListId == ALL_LIST_ID) {
                        mBinder.setMusicList(mList);
                        if(check(mBinder.getPos())){
                            mList.get(mBinder.getPos()).setIsPlaying(true);
                        } else {
                            mBinder.pauseMusic();
                            HomeActivity.getInstance().initNoMusic();
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    String str = getResources().getString(R.string.replace_random_play_some_music);
                    str = str.replace("*",String.valueOf(mList.size()) );
                    mPlayNumberTextView.setText(str);
                    focus();
                }, throwable -> {
                    Logger.d(getResources().getString(R.string.error_load_date));
                }));
    }
    @Override
    public void onItemClick(View view, int position) {
        if(UserManager.getInstance().getIsPlayListId() != ALL_LIST_ID) {
            mBinder.setPos(position);
            addToPlayList();
            startMusic();
            sInitListFlag = 0;
        } else {
            mBinder.setPos(position);
            startMusic();
            HomeActivity.sInitTitleFlag = 0;
            changeListItem(mBinder.getPreciousPos());
            changeListItem(mBinder.getPos());
        }
    }
    public void startMusic() {
        if(mBinder.getPos() < 0 || mBinder.getPos() >= mBinder.getListSize()) {
            return ;
        }
        mBinder.initMediaPlayer();
        mBinder.playMusic();
    }
    public void addToPlayList() {
        if(UserManager.getInstance().getIsPlayListId() != ALL_LIST_ID) {
            mIsPlayListId = ALL_LIST_ID;
            UserManager.getInstance().setIsPlayListId(mIsPlayListId);
        }
        mBinder.setMusicList(mList);
        mDisposable.add(Completable.fromAction(() -> {
            ListInMusicEntity listInMusicEntity = new ListInMusicEntity();
            listInMusicEntity.setSongListId(1);
            mListInMusicDao.deleteByListId(1);
            for(int i=0;i<mList.size();i++) {
                MusicEntity musicEntity = mList.get(i);
                listInMusicEntity.setMusicId(musicEntity.getId());
                mListInMusicDao.add(listInMusicEntity);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, throwable -> {
                }));
    }
    @Override
    public void onItemLongClick(View view, int position) {
        SelectSongOperationPopup songOperationPopup = new SelectSongOperationPopup(this,0,mList.get(position) );
        songOperationPopup.showPopup(mCoordinatorLayout);
    }
    public void changeListItem(int pos) {
        if(check(pos) ) {
            mAdapter.notifyItemChanged(pos);
        }
    }
    public boolean check(int pos){
        if(pos >= mBinder.getListSize() || pos < 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_and_favorite:
                mFavoriteAlertDialog.show();
                break;
            case R.id.menu_and_search:
                SearchMusicActivity.go(AllSongActivity.this,mList,getResources().getString(R.string.text_all_song));
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_and_search_menu, menu);//指定Toolbar上的视图文件
        return true;
    }

    public void focus() {
        LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int pos =  mBinder.getPos();
        if(pos>=2) {
            pos -= 2;
        } else {
            pos = 0;
        }
        llm.scrollToPositionWithOffset(pos, 0);
        llm.setStackFromEnd(false);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.all_song_fab:
                if(mList.size() == 0) {
                    Toaster.showToast(getString(R.string.message_no_music_and_go_add_music));
                    return ;
                }
                mBinder.setPos(0);
                addToPlayList();
                startMusic();
                focus();
                sInitListFlag = 0;
                HomeActivity.sInitTitleFlag = 0;
                break;
            case R.id.single_song_list_random_play_layout:
                if(mList.size() == 0) {
                    Toaster.showToast(getString(R.string.message_no_music_and_go_add_music));
                    return ;
                }
                mBinder.setPlayMode(1);
                mBinder.setPos((int) (Math.random()*mList.size()));
                addToPlayList();
                startMusic();
                focus();
                HomeActivity.sInitTitleFlag = 0;
                sInitListFlag = 0;
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mDisposable.clear();
        HomeActivity.sInitTitleFlag = 0;
        HomeActivity.getInstance().initMusic();
        if(mHandler != null){
            mHandler.removeCallbacks(mRunnable);
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
    public void favoriteMusic() {
        mDisposable.add(Completable.fromAction(() -> {
            ListInMusicEntity listInMusicEntity = new ListInMusicEntity();
            listInMusicEntity.setSongListId(2);
            for(int i=0;i<mList.size();i++) {
                MusicEntity musicEntity = mList.get(i);
                listInMusicEntity.setMusicId(musicEntity.getId());
                if(mListInMusicDao.getListInMusic( listInMusicEntity.getSongListId() , listInMusicEntity.getMusicId() ) == null) {
                    mListInMusicDao.add(listInMusicEntity);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toaster.showToast(getResources().getString(R.string.message_favorite_music_success));
                    HomeActivity.sInitTitleFlag = 0;
                }, throwable -> {
                    Logger.d(getResources().getString(R.string.error_load_date));
                }));
    }
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //Logger.d(mIsPlayListId);
            if(mBinder.getDuration() - mBinder.getPlayPosition() <= 2000) {
                sInitListFlag = 0;
            }
            if( sInitListFlag == 0 && mBinder.getPlayPosition() <= 2000 &&  mIsPlayListId == ALL_LIST_ID) {
                mAdapter.notifyDataSetChanged();
                sInitListFlag = 1;
            }
            mHandler.postDelayed(mRunnable,1000);
        }
    };
    static class MyHandler extends Handler {
        private final WeakReference mActivty;

        public MyHandler(AllSongActivity activity){
            mActivty = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
