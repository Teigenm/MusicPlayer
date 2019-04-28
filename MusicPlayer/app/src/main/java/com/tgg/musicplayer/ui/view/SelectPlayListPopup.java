package com.tgg.musicplayer.ui.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.RecyclerViewTouchListener;
import com.tgg.musicplayer.app.UserManager;
import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.ui.play.PlayListAdapter;
import com.tgg.musicplayer.ui.home.HomeActivity;
import com.tgg.musicplayer.ui.play.MusicPlayingActivity;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectPlayListPopup implements View.OnClickListener,RecyclerViewTouchListener.OnItemClickListener, RecyclerViewTouchListener.OnItemLongClickListener {
    public PopupWindow mPopupWindow;
    private SelectPlayListPopupOnClickListener selectPlayListPopupOnClickListener;

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }
    private Context mContext;
    private MediaService.MyBinder mBinder;
    private RecyclerView mRecyclerView;
    private PlayListAdapter mAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("deprecation")
    public SelectPlayListPopup(Context context) {
        mContext = context;
        mBinder = UserManager.getInstance().getMyBinder();

        View contentView = initViews();
        mPopupWindow = new PopupWindow(contentView,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);

        contentView.setOnTouchListener((v, event) -> {
            dismiss();
            mPopupWindow.setFocusable(false);
            return true;
        });
    }

    public View initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_play_list_pop_window, null);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext) );
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext , DividerItemDecoration.VERTICAL) );

        mAdapter = new PlayListAdapter(mBinder.getMusicList());
        mRecyclerView.setAdapter(mAdapter);
        focus();
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(mContext,this,this));
        changeListItem(mBinder.getPos());

        ImageView focusImageView = view.findViewById(R.id.play_list_focus_image_view);
        focusImageView.setOnClickListener(this);
        return view;
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
        switch (v.getId()) {
            case R.id.play_list_focus_image_view:
                focus();
            default:
                break;
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        MusicPlayingActivity.sInitTitleFlag = 0;
        HomeActivity.sInitTitleFlag = 0;
        mBinder.setPos(position);
        mBinder.initMediaPlayer();
        mBinder.playMusic();

        changeListItem(mBinder.getPos());
        changeListItem(mBinder.getPreciousPos());
    }
    public void changeListItem(int pos) {
        if(check(pos) ) {
            mAdapter.notifyItemChanged(pos);
        }
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
            HomeActivity.sInitTitleFlag = 0;
        }
    }
    public boolean check(int pos){
        if(pos >= mBinder.getListSize() || pos < 0) {
            return false;
        }
        return true;
    }
    public void showPopup(View rootView) {
        // 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

}
