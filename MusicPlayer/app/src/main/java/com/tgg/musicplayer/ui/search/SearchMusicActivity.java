package com.tgg.musicplayer.ui.search;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.BaseActivity;
import com.tgg.musicplayer.storage.database.AppDatabase;
import com.tgg.musicplayer.storage.database.dao.ListInMusicDao;
import com.tgg.musicplayer.storage.database.dao.MusicDao;
import com.tgg.musicplayer.storage.database.dao.SongListDao;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.ui.play.PlayListAdapter;
import com.tgg.musicplayer.utils.log.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchMusicActivity extends BaseActivity implements View.OnClickListener{

    private SearchView mSearchView;
    private ImageView mBackImageView;
    private RecyclerView mRecyclerView;
    private PlayListAdapter mAdapter;

    private List<MusicEntity> mList;

    private CompositeDisposable mDisposable;
    private AppDatabase mAppDatabase;
    private ListInMusicDao mListInMusicDao;
    private SongListDao mSongListDao;
    private MusicDao mMusicDao;

    public static void go(Context context){
        Intent intent = new Intent();
        intent.setClass(context,SearchMusicActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_music_layout);

        initView();
        initDate();
    }
    private void initDate() {
        mDisposable = new CompositeDisposable();
        mAppDatabase = AppDatabase.getInstance();
        mListInMusicDao = mAppDatabase.getListInMusicDao();
        mSongListDao = mAppDatabase.getSongListDao();
        mMusicDao = mAppDatabase.getMusicDao();
    }
    private void initView() {
        mBackImageView = findViewById(R.id.search_music_back_image_view);
        mBackImageView.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.search_music_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchMusicActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(SearchMusicActivity.this,DividerItemDecoration.HORIZONTAL));

        mList = new ArrayList<>();
        mAdapter = new PlayListAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mSearchView = findViewById(R.id.search_music_search_view);
        if(mSearchView != null) {
            try {
                Class<?> argClass = mSearchView.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(mSearchView);
                //--设置背景
                mView.setBackgroundResource(R.drawable.shape_search_view_line);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        mSearchView.onActionViewExpanded();
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchView.setFocusable(false);
        mSearchView.setQueryHint(getResources().getString(R.string.text_search_music));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMusic();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    public void searchMusic() {
        mDisposable.add(Completable.fromAction(() -> {
            String text ;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    Logger.d(getResources().getString(R.string.error_load_date));
                }));
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.search_music_back_image_view:
                finish();
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
