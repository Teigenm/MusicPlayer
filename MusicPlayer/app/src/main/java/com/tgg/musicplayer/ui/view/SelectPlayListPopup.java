package com.tgg.musicplayer.ui.view;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.RecyclerViewTouchListener;
import com.tgg.musicplayer.app.UserManager;
import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.ui.home.HomeActivity;
import com.tgg.musicplayer.utils.Toaster;
import com.tgg.musicplayer.utils.log.Logger;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectPlayListPopup implements View.OnClickListener {
    public PopupWindow mPopupWindow;
    private SelectPlayListPopupOnClickListener selectPlayListPopupOnClickListener;

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }

    private Context mContext;

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
        mPopupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPopupWindow.setFocusable(false);
                mPopupWindow.dismiss();
                return true;
            }
        });

    }

    public View initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_play_list_pop_window, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext) );
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext , DividerItemDecoration.VERTICAL) );
        PlayListAdapter adapter = new PlayListAdapter(UserManager.getInstance().getMyBinder().getMusicList());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(mContext,
                new RecyclerViewTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Logger.d(position+"");
                        MediaService.MyBinder myBinder = UserManager.getInstance().getMyBinder();
                        myBinder.setPos(position);
                        myBinder.initMediaPlayer();
                        myBinder.playMusic();
                    }
                 },new RecyclerViewTouchListener.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toaster.showToast(position+"");
                    }
            }));

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }

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
