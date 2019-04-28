package com.tgg.musicplayer.ui.search;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.BaseActivity;
import com.tgg.musicplayer.app.RecyclerViewTouchListener;
import com.tgg.musicplayer.storage.database.AppDatabase;
import com.tgg.musicplayer.storage.database.dao.ListInMusicDao;
import com.tgg.musicplayer.storage.database.dao.MusicDao;
import com.tgg.musicplayer.storage.database.dao.SongListDao;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.ui.view.SelectSearchOperationPopup;
import com.tgg.musicplayer.ui.view.SelectSongOperationPopup;
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

public class SearchMusicActivity extends BaseActivity implements View.OnClickListener,RecyclerViewTouchListener.OnItemClickListener, RecyclerViewTouchListener.OnItemLongClickListener{

    private SearchView mSearchView;
    private ImageView mBackImageView;
    private RecyclerView mRecyclerView;
    private SearchMusicAdapter mAdapter;
    private TextView mNowListNameTextView;
    private TextView mResultTextView;
    private RelativeLayout mAllLayout;

    private CompositeDisposable mDisposable;
    private AppDatabase mAppDatabase;
    private ListInMusicDao mListInMusicDao;
    private SongListDao mSongListDao;
    private MusicDao mMusicDao;
    private static List<MusicEntity> mAllList;
    private List<MusicEntity> mMusicList;
    private String mSearchText = "";
    private static String mNowListName = "";

    public static void go(Context context,List<MusicEntity> allList,String nowListName) {
        Intent intent = new Intent();
        intent.setClass(context,SearchMusicActivity.class);
        context.startActivity(intent);
        mAllList = allList;
        mNowListName = nowListName;
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

        mNowListNameTextView.setText(mNowListName);
        String tempString = getResources().getString(R.string.replace_follow_some_music_search);
        tempString = tempString.replace("*", String.valueOf(mAllList.size() ) );
        mResultTextView.setText(tempString);
    }
    private void initView() {

        mAllLayout = findViewById(R.id.search_music_all_layout);
        mNowListNameTextView = findViewById(R.id.search_music_now_list_name_text_view);
        mResultTextView = findViewById(R.id.search_music_result_text_view);

        mBackImageView = findViewById(R.id.search_music_back_image_view);
        mBackImageView.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.search_music_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchMusicActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(SearchMusicActivity.this,DividerItemDecoration.HORIZONTAL));
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this,this,this));

        mMusicList = new ArrayList<>();
        mAdapter = new SearchMusicAdapter(mMusicList);
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
        mSearchView.setQueryHint(getResources().getString(R.string.text_search_music));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hintKeyBoard();
                searchMusic();
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    public void searchMusic() {
        mSearchText = mSearchView.getQuery().toString();
        if(mSearchText == null || "".equals(mSearchText) ) {
            return ;
        }
        mDisposable.add(Completable.fromAction(() -> {
            mMusicList.clear();
            for(int i=0;i < mAllList.size();i++) {
                MusicEntity entity = mAllList.get(i);
                if( entity.getSingerName().contains(mSearchText) || entity.getSongName().contains(mSearchText)
                        || entity.getAlbumName().contains(mSearchText) ) {
                    mMusicList.add(entity);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mAdapter.notifyDataSetChanged();
                    String tempString = getResources().getString(R.string.replace_search_music_result);
                    tempString = tempString.replace("*", String.valueOf(mMusicList.size() ) );
                    mResultTextView.setText(tempString);
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

    @Override
    public void onItemClick(View view, int position) {
        SelectSearchOperationPopup songOperationPopup = new SelectSearchOperationPopup(this,mMusicList.get(position) );
        songOperationPopup.showPopup(mAllLayout);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        SelectSearchOperationPopup songOperationPopup = new SelectSearchOperationPopup(this,mMusicList.get(position) );
        songOperationPopup.showPopup(mAllLayout);
    }
}
