package com.tgg.musicplayer.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.ToolbarActivity;
import com.tgg.musicplayer.storage.database.AppDatabase;
import com.tgg.musicplayer.storage.database.dao.ListInMusicDao;
import com.tgg.musicplayer.storage.database.dao.MusicDao;
import com.tgg.musicplayer.storage.database.dao.SongListDao;
import com.tgg.musicplayer.storage.database.table.SongListEntity;
import com.tgg.musicplayer.utils.Toaster;
import com.tgg.musicplayer.utils.log.Logger;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: tgg
 * @CreateDate: 2019/4/17 22:17
 * @Description: sKind 1 编辑页面 0 增加页面
 * @Version: 1.0
 */
public class EditOrAddSongListActivity extends ToolbarActivity {

    private final int MAX_LIST_NAME_WORDS = 16;
    private final int MAX_DESCRIPTION_WORDS = 50;
    private TextInputEditText mListNameEditText;
    private TextInputEditText mDescriptionEditText;
    private FloatingActionButton mFab;
    private TextView mListNameNumWordsTextView;
    private TextView mDescriptionNumWordsTextView;

    private CompositeDisposable mDisposable;
    private AppDatabase mAppDatabase;
    private ListInMusicDao mListInMusicDao;
    private SongListDao mSongListDao;
    private MusicDao mMusicDao;

    private SongListEntity mSongListEntity;

    private static int sKind;
    private static long sListId;

    private boolean mListNameFlag;
    private boolean mDescriptionFlag;
    public static void go(Context context,int kind,long listId){
        Intent intent = new Intent();
        intent.setClass(context, EditOrAddSongListActivity.class);
        context.startActivity(intent);
        sKind = kind;
        sListId = listId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_add_song_list_layout);

        initView();
        initDate();
    }

    public void initDate() {
        mDisposable = new CompositeDisposable();
        mAppDatabase = AppDatabase.getInstance();
        mListInMusicDao = mAppDatabase.getListInMusicDao();
        mSongListDao = mAppDatabase.getSongListDao();
        mMusicDao = mAppDatabase.getMusicDao();

        mListNameFlag = true;
        mDescriptionFlag = true;

        if(sKind == 1) {
            mDisposable.add(Completable.fromAction(() -> {
                mSongListEntity = mSongListDao.getSongListById(sListId);
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        mDescriptionEditText.setText(mSongListEntity.getDescription());
                        mListNameEditText.setText(mSongListEntity.getTitle());
                    }, throwable -> {
                        Logger.d(getResources().getString(R.string.error_load_date));
                    }));
        }
    }

    public void edit() {
        mDisposable.add(Completable.fromAction(() -> {
            mSongListEntity.setTitle(mListNameEditText.getText().toString().trim());
            mSongListEntity.setDescription(mDescriptionEditText.getText().toString().trim());
            mSongListDao.update(mSongListEntity);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toaster.showToast("保存歌单信息成功");
                    finish();
                }, throwable -> {
                    Logger.d(getResources().getString(R.string.error_load_date));
                }));
    }
    public void add() {
        mDisposable.add(Completable.fromAction(() -> {
            SongListEntity songListEntity = new SongListEntity();
            songListEntity.setTitle(mListNameEditText.getText().toString().trim());
            songListEntity.setDescription(mDescriptionEditText.getText().toString().trim());
            songListEntity.setCreateTime(System.currentTimeMillis() );
            mSongListDao.add(songListEntity);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toaster.showToast("添加歌单成功");
                    finish();
                }, throwable -> {
                    Logger.d(getResources().getString(R.string.error_load_date));
                }));
    }
    public void initView () {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(sKind == 0) {
            getSupportActionBar().setTitle("新建歌单");
        } else {
            getSupportActionBar().setTitle("编辑歌单");
        }

        mListNameEditText = findViewById(R.id.edit_and_add_list_name_text_view);
        mDescriptionEditText = findViewById(R.id.edit_and_add_description_text_view);
        mFab = findViewById(R.id.edit_and_add_fab);
        mListNameNumWordsTextView = findViewById(R.id.edit_and_add_list_name_number_words_text_view);
        mDescriptionNumWordsTextView = findViewById(R.id.edit_and_add_description_number_words_text_view);


        mDescriptionNumWordsTextView.setText(0+"/"+MAX_DESCRIPTION_WORDS);
        mListNameNumWordsTextView.setText(0+"/"+ MAX_LIST_NAME_WORDS);
        mListNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = mListNameEditText.getText().toString().length();
                if(length <= MAX_LIST_NAME_WORDS) {
                    mListNameFlag = true;
                } else {
                    mListNameFlag = false;
                    mListNameEditText.setError("字数超出限制啦");
                }
                mListNameNumWordsTextView.setText(length+"/"+ MAX_LIST_NAME_WORDS);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = mDescriptionEditText.getText().toString().length();
                if(length <= MAX_DESCRIPTION_WORDS) {
                    mDescriptionFlag = true;
                } else {
                    mDescriptionFlag = false;
                    mDescriptionEditText.setError("字数超出限制啦");
                }
                mDescriptionNumWordsTextView.setText(length+"/"+MAX_DESCRIPTION_WORDS);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!mDescriptionFlag || !mListNameFlag ) {
                    return ;
                }
                String listName = mListNameEditText.getText().toString().trim();
                if(listName.equals("") || listName == null ) {
                    mListNameEditText.setFocusable(true);
                    mListNameEditText.setError("歌单名不能为空");
                    showSoftInputFromWindow(EditOrAddSongListActivity.this, mListNameEditText);
                    return ;
                }
                if(sKind == 1) {
                    edit();
                } else {
                    add();
                }
                HomeActivity.sInitTitleFlag = 0;
            }
        });
    }
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
