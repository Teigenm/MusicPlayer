package com.tgg.musicplayer.ui.song;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TimeUtils;
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
import com.tgg.musicplayer.storage.database.AppDatabase;
import com.tgg.musicplayer.storage.database.dao.ListInMusicDao;
import com.tgg.musicplayer.storage.database.dao.MusicDao;
import com.tgg.musicplayer.storage.database.dao.SongListDao;
import com.tgg.musicplayer.storage.database.table.ListInMusicEntity;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.storage.database.table.SongListEntity;
import com.tgg.musicplayer.ui.home.EditOrAddSongListActivity;
import com.tgg.musicplayer.ui.home.HomeActivity;
import com.tgg.musicplayer.utils.DateTimeUtils;
import com.tgg.musicplayer.utils.Toaster;
import com.tgg.musicplayer.utils.log.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
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

public class SongListActivity extends BaseActivity implements View.OnClickListener,RecyclerViewTouchListener.OnItemClickListener, RecyclerViewTouchListener.OnItemLongClickListener{

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private RecyclerView mRecyclerView;
    private List<MusicEntity> mList;
    private SingleSongListAdapter mAdapter;
    private FloatingActionButton mFab;
    private AppBarLayout mAppBarLayout;
    private TextView mToolbarTextView;
    private TextView mPlayNumberTextView;
    private TextView mListTitleTextView;
    private TextView mDescriptionTextView;
    private TextView mCreateTimeTextView;

    private CompositeDisposable mDisposable;
    private AppDatabase mAppDatabase;
    private ListInMusicDao mListInMusicDao;
    private SongListDao mSongListDao;
    private MusicDao mMusicDao;

    private SongListEntity mSongListEntity;
    private static long sListId;
    public static void go(Context context,long listId){
        Intent intent = new Intent();
        intent.setClass(context,SongListActivity.class);
        context.startActivity(intent);
        sListId = listId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_layout);

        initView();
        initDate();
    }
    private void initView() {
        Toolbar toolbar = findViewById(R.id.song_list_toolbar);
        setSupportActionBar(toolbar);

        Drawable drawable = toolbar.getNavigationIcon();
        if (drawable != null) {
            DrawableCompat.setTint(drawable, ResourcesCompat.getColor(getResources(), android.R.color.white, null));
        }

        mCollapsingToolbarLayout = findViewById(R.id.song_list_collapsing_layout);
        mCollapsingToolbarLayout.setExpandedTitleColor(ResourcesCompat.getColor(getResources(), android.R.color.white, null));

        mCreateTimeTextView = findViewById(R.id.song_list_create_time_text_view);
        mDescriptionTextView = findViewById(R.id.song_list_description_text_view);
        mListTitleTextView = findViewById(R.id.song_list_title_text_view);
        mToolbarTextView = findViewById(R.id.song_list_toolbar_text_view);

        mAppBarLayout = findViewById(R.id.song_list_app_bar);
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

        mFab = findViewById(R.id.song_list_fab);
        mFab.setOnClickListener(SongListActivity.this);
        mPlayNumberTextView = findViewById(R.id.single_song_list_play_number_text_view);

        mRecyclerView = findViewById(R.id.single_song_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SongListActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(SongListActivity.this,DividerItemDecoration.VERTICAL));
        mList = new ArrayList<>();
        mAdapter = new SingleSongListAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this,this,this));

    }
    private void initDate() {
        mDisposable = new CompositeDisposable();
        mAppDatabase = AppDatabase.getInstance();
        mListInMusicDao = mAppDatabase.getListInMusicDao();
        mSongListDao = mAppDatabase.getSongListDao();
        mMusicDao = mAppDatabase.getMusicDao();

        mDisposable.add(Completable.fromAction(() -> {
            Logger.d(sListId);
            mList.addAll(mMusicDao.getMusicsByListId(sListId));
            mSongListEntity = mSongListDao.getSongListById(sListId);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mAdapter.notifyDataSetChanged();
                    mPlayNumberTextView.setText("随机播放"+mList.size()+"首歌曲");
                    mListTitleTextView.setText(mSongListEntity.getTitle() );
                    mDescriptionTextView.setText(mSongListEntity.getDescription() );
                    mCreateTimeTextView.setText(DateTimeUtils.getDate(mSongListEntity.getCreateTime() ) );
                    mToolbarTextView.setText(mSongListEntity.getTitle() );
                }, throwable -> {
                    Logger.d(getResources().getString(R.string.error_load_date));
                }));
    }
    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
    public void favoriteMusic() {
        mDisposable.add(Completable.fromAction(() -> {
            ListInMusicEntity listInMusicEntity = new ListInMusicEntity();
            listInMusicEntity.setSongListId(2);
            for(int i=0;i<mList.size();i++) {
                MusicEntity musicEntity = mList.get(i);
                listInMusicEntity.setMusicId(musicEntity.getId());
                mListInMusicDao.add(listInMusicEntity);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toaster.showToast(getResources().getString(R.string.message_favorite_music_success));
                }, throwable -> {

                }));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_song_list_favorite:
                favoriteMusic();
                break;
            case R.id.menu_song_list_description:
                EditOrAddSongListActivity.go(SongListActivity.this,1,sListId);
                initDate();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_list_menu, menu);//指定Toolbar上的视图文件
        return true;
    }

    public void addToPlayList() {
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
                    HomeActivity.getInstance().initDate(1);
                    Toaster.showToast(getResources().getString(R.string.message_play_music_success));
                }, throwable -> {
                    Toaster.showToast(getResources().getString(R.string.message_play_music_success));
                }));
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.song_list_fab:
                addToPlayList();
                break;
            default:
                break;
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mDisposable.clear();
    }
}
