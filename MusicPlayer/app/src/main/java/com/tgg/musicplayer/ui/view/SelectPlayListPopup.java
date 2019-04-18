package com.tgg.musicplayer.ui.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.RecyclerViewTouchListener;
import com.tgg.musicplayer.app.UserManager;
import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.ui.play.PlayListAdapter;
import com.tgg.musicplayer.ui.home.HomeActivity;
import com.tgg.musicplayer.ui.play.MusicPlayingActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectPlayListPopup implements View.OnClickListener,RecyclerViewTouchListener.OnItemClickListener, RecyclerViewTouchListener.OnItemLongClickListener {
    public PopupWindow mPopupWindow;
    private SelectPlayListPopupOnClickListener selectPlayListPopupOnClickListener;

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }
    private List<MusicEntity> mList;
    private Context mContext;

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("deprecation")
    public SelectPlayListPopup(Context context) {
        mContext = context;
        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setContentView(initViews());
        mPopupWindow.getContentView().setOnTouchListener((v, event) -> {
            mPopupWindow.setFocusable(false);
            mPopupWindow.dismiss();
            return true;
        });

    }

    public View initViews() {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_play_list_pop_window, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext) );
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext , DividerItemDecoration.VERTICAL) );
        if(UserManager.getInstance().getMyBinder() != null) {
            mList = UserManager.getInstance().getMyBinder().getMusicList();
        } else {
            mList = new ArrayList<>();
        }
        PlayListAdapter adapter = new PlayListAdapter(mList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(mContext,this,this));
        LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
        llm.scrollToPositionWithOffset(UserManager.getInstance().getMyBinder().getPos(), 0);
        llm.setStackFromEnd(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        MusicPlayingActivity.sInitTitleFlag = 0;
        HomeActivity.sInitTitleFlag = 0;
        MediaService.MyBinder myBinder = UserManager.getInstance().getMyBinder();
        myBinder.setPos(position);
        myBinder.initMediaPlayer();
        myBinder.playMusic();
        HomeActivity.getInstance().initDate(1);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    public interface SelectPlayListPopupOnClickListener {
        void obtainMessage(int flag, String ret);
    }

    public void SelectPlayListPopupOnClickListener(SelectPlayListPopupOnClickListener l) {
        this.selectPlayListPopupOnClickListener = l;
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void showPopup(View rootView) {
        // 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }
}
