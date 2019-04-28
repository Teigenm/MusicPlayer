package com.tgg.musicplayer.ui.view;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.UserManager;
import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.storage.database.AppDatabase;
import com.tgg.musicplayer.storage.database.dao.ListInMusicDao;
import com.tgg.musicplayer.storage.database.dao.MusicDao;
import com.tgg.musicplayer.storage.database.dao.RecentMusicDao;
import com.tgg.musicplayer.storage.database.dao.SongListDao;
import com.tgg.musicplayer.storage.database.table.ListInMusicEntity;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.storage.database.table.SongListEntity;
import com.tgg.musicplayer.ui.home.HomeActivity;
import com.tgg.musicplayer.ui.play.MusicPlayingActivity;
import com.tgg.musicplayer.ui.song.AllSongActivity;
import com.tgg.musicplayer.ui.song.MyFavoriteActivity;
import com.tgg.musicplayer.ui.song.RecentReleaseActivity;
import com.tgg.musicplayer.ui.song.SongListActivity;
import com.tgg.musicplayer.ui.view.DialogFactory;
import com.tgg.musicplayer.utils.Toaster;
import com.tgg.musicplayer.utils.log.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
  *
  * @Author:         tgg
  * @CreateDate:     2019/4/20 20:57
  * @Description:
  * @Version:        1.0
 */

public class SelectSearchOperationPopup implements View.OnClickListener {
    public PopupWindow mPopupWindow;
    private SelectSongOperationPopupOnClickListener selectSongOperationPopupOnClickListener;
    private LinearLayout mAddPlayListLayout;
    private LinearLayout mSongDetailLayout;
    private LinearLayout mDeleteSongLayout;
    private TextView mSongNameTextView;
    private TextView mSingerNameTextView;

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    private MediaService.MyBinder mBinder;
    private Context mContext;
    private static MusicEntity sMusicEntity;

    private CompositeDisposable mDisposable;
    private AppDatabase mAppDatabase;
    private ListInMusicDao mListInMusicDao;
    private SongListDao mSongListDao;
    private MusicDao mMusicDao;

    private AlertDialog mListDialog;

    private List<MusicEntity> mMusicList;
    private List<SongListEntity> mSongListEntityList;
    private int mAddPlayListFlag = 0;
    private List<String> mTitleList;
    private int mIsCheckedPos;

    @SuppressWarnings("deprecation")
    public SelectSearchOperationPopup(Context context, MusicEntity musicEntity) {
        mContext = context;
        sMusicEntity = musicEntity;

        mDisposable = new CompositeDisposable();
        mAppDatabase = AppDatabase.getInstance();
        mListInMusicDao = mAppDatabase.getListInMusicDao();
        mSongListDao = mAppDatabase.getSongListDao();
        mMusicDao = mAppDatabase.getMusicDao();

        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setContentView(initViews());
        mPopupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPopupWindow.setFocusable(false);
                mPopupWindow.dismiss();
                mDisposable.clear();
                return true;
            }
        });
        getSongList();
    }

    public View initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_song_operation_pop_window, null);
        mAddPlayListLayout = view.findViewById(R.id.song_operation_add_play_list_layout);
        mAddPlayListLayout.setOnClickListener(this);

        mSongDetailLayout = view.findViewById(R.id.song_operation_song_detail_layout);
        mSongDetailLayout.setOnClickListener(this);

        mDeleteSongLayout = view.findViewById(R.id.song_operation_delete_song_layout);
        mDeleteSongLayout.setVisibility(View.GONE);

        mSongNameTextView = view.findViewById(R.id.song_operation_song_name_text_view);
        mSingerNameTextView = view.findViewById(R.id.song_operation_singer_name_text_view);
        mSingerNameTextView.setVisibility(View.GONE);

        mSongNameTextView.setText(sMusicEntity.getSongName());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.song_operation_add_play_list_layout:
                mListDialog = DialogFactory.createOptionMenuDialog(mContext, mTitleList, (parent, view, position, id) -> {
                    String item = (String) parent.getAdapter().getItem(position);
                    mIsCheckedPos = position+1;
                    mListDialog.dismiss();
                    addPlayList(mIsCheckedPos);
                });
                mListDialog.show();
                break;
            case R.id.song_operation_song_detail_layout:
                Toaster.showToast("该功能未开放");
                break;
            default:
                break;
        }

    }
    private void addPlayList (int isCheckedListId) {
        mAddPlayListFlag = 0;
        mDisposable.add(Completable.fromAction(() -> {
            if(mListInMusicDao.getListInMusic(isCheckedListId,sMusicEntity.getId()) == null) {
                mListInMusicDao.add(new ListInMusicEntity(isCheckedListId,sMusicEntity.getId() ) );
                mAddPlayListFlag = 1;
                mMusicList = mMusicDao.getMusicsByListId(1);
            } else {
                mAddPlayListFlag = 0;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if(mAddPlayListFlag == 1) {
                        Toaster.showToast(mContext.getResources().getString(R.string.message_add_list_success));
                        UserManager.getInstance().getMyBinder().setMusicList(mMusicList);
                    } else {
                        Toaster.showToast(mContext.getResources().getString(R.string.message_add_list_fail));
                    }
                    dismiss();
                }, throwable -> {
                    Toaster.showToast(mContext.getResources().getString(R.string.error_progress));
                }));
    }
    private void getSongList () {
        mDisposable.add(Completable.fromAction(() -> {
            mTitleList = new ArrayList<>();
            mSongListEntityList = mSongListDao.getAllListOrderById();
            for(int i=0;i<mSongListEntityList.size();i++) {
                mTitleList.add(mSongListEntityList.get(i).getTitle());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, throwable -> {
                    Toaster.showToast(mContext.getResources().getString(R.string.error_progress));
                }));
    }
    public interface SelectSongOperationPopupOnClickListener {
        void obtainMessage(int flag, String ret);
    }

    public void SelectSongOperationPopupOnClickListener(SelectSongOperationPopupOnClickListener l) {
        this.selectSongOperationPopupOnClickListener = l;
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mDisposable.clear();
            HomeActivity.sInitTitleFlag = 0;
        }
    }

    public void showPopup(View rootView) {
        // 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }
}
