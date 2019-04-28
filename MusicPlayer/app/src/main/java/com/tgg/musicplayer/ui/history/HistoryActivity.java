package com.tgg.musicplayer.ui.history;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.BaseActivity;
import com.tgg.musicplayer.storage.database.AppDatabase;
import com.tgg.musicplayer.storage.database.dao.MusicDao;
import com.tgg.musicplayer.storage.database.dao.RecentMusicDao;
import com.tgg.musicplayer.storage.database.dao.SongListDao;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.storage.database.table.RecentMusicEntity;
import com.tgg.musicplayer.utils.log.Logger;

import java.util.ArrayList;
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

public class HistoryActivity extends BaseActivity {

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView mNumberOneTimeTextView;
    private TextView mNumberOneSongNameTextView;
    private TextView mNumberOneSingerNameTextView;
    private TextView mNumberTwoTimeTextView;
    private TextView mNumberTwoSongNameTextView;
    private TextView mNumberTwoSingerNameTextView;
    private TextView mNumberThreeTimeTextView;
    private TextView mNumberThreeSongNameTextView;
    private TextView mNumberThreeSingerNameTextView;
    private RecyclerView mRecyclerView;
    private HistorySongAdapter mAdapter;

    private List<RecentMusicEntity> mList;

    private CompositeDisposable mDisposable;
    private AppDatabase mAppDatabase;
    private RecentMusicDao mRecentMusicDao;
    private SongListDao mSongListDao;
    private MusicDao mMusicDao;

    public static void go(Context context){
        Intent intent = new Intent();
        intent.setClass(context,HistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_layout);

        initView();
        initDate();
    }
    private void initDate() {
        mDisposable = new CompositeDisposable();
        mAppDatabase = AppDatabase.getInstance();
        mRecentMusicDao = mAppDatabase.getRecentMusicDao();
        mSongListDao = mAppDatabase.getSongListDao();
        mMusicDao = mAppDatabase.getMusicDao();

        mDisposable.add(Completable.fromAction(() -> {
            mList.addAll(mRecentMusicDao.getMostTimes());
            for(int i=0;i<mList.size();i++) {
                RecentMusicEntity recentMusicEntity = mList.get(i);
                MusicEntity musicEntity = mMusicDao.getMusicById(recentMusicEntity.getMusicId() );
                recentMusicEntity.setSongName(musicEntity.getSongName());
                recentMusicEntity.setSingerName(musicEntity.getSingerName());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    RecentMusicEntity one = new RecentMusicEntity(0,"","");
                    RecentMusicEntity two = new RecentMusicEntity(0,"","");
                    RecentMusicEntity three = new RecentMusicEntity(0,"","");
                    for(int i=0;i<mList.size() && i<=2;i++) {
                        switch (i) {
                            case 0:one = mList.get(i);break;
                            case 1:two = mList.get(i);break;
                            case 2:three = mList.get(i);break;
                        }
                    }
                    mNumberOneTimeTextView.setText(one.getPlayTimes()+"次");
                    mNumberOneSongNameTextView.setText(one.getSongName());
                    mNumberOneSingerNameTextView.setText(one.getSingerName());
                    mNumberTwoTimeTextView.setText(two.getPlayTimes()+"次");
                    mNumberTwoSongNameTextView.setText(two.getSongName());
                    mNumberTwoSingerNameTextView.setText(two.getSingerName());
                    mNumberThreeTimeTextView.setText(three.getPlayTimes()+"次");
                    mNumberThreeSongNameTextView.setText(three.getSongName());
                    mNumberThreeSingerNameTextView.setText(three.getSingerName());
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    Logger.d(getResources().getString(R.string.error_load_date));
                }));
    }
    private void initView () {

        Toolbar toolbar = findViewById(R.id.history_toolbar);
        setSupportActionBar(toolbar);

        Drawable drawable = toolbar.getNavigationIcon();
        if (drawable != null) {
            DrawableCompat.setTint(drawable, ResourcesCompat.getColor(getResources(), android.R.color.white, null));
        }

        mCollapsingToolbarLayout = findViewById(R.id.history_collapsing_layout);
        mCollapsingToolbarLayout.setExpandedTitleColor(ResourcesCompat.getColor(getResources(), android.R.color.white, null));

        mNumberOneTimeTextView = findViewById(R.id.history_number_one_recent_release_times_text_view);
        mNumberOneSongNameTextView = findViewById(R.id.history_number_one_song_name_text_view);
        mNumberOneSingerNameTextView = findViewById(R.id.history_number_one_singer_name_text_view);

        mNumberTwoTimeTextView = findViewById(R.id.history_number_two_recent_release_times_text_view);
        mNumberTwoSongNameTextView = findViewById(R.id.history_number_two_song_name_text_view);
        mNumberTwoSingerNameTextView = findViewById(R.id.history_number_two_singer_name_text_view);

        mNumberThreeTimeTextView = findViewById(R.id.history_number_three_recent_release_times_text_view);
        mNumberThreeSongNameTextView = findViewById(R.id.history_number_three_song_name_text_view);
        mNumberThreeSingerNameTextView = findViewById(R.id.history_number_three_singer_name_text_view);

        mList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.history_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(HistoryActivity.this,DividerItemDecoration.VERTICAL));
        mAdapter = new HistorySongAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mDisposable.clear();
    }
}
