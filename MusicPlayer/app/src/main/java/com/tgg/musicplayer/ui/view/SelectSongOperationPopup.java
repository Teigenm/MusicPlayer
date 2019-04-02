package com.tgg.musicplayer.ui.view;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tgg.musicplayer.R;

public class SelectSongOperationPopup implements View.OnClickListener {
    public PopupWindow mPopupWindow;
    private SelectSongOperationPopupOnClickListener selectSongOperationPopupOnClickListener;

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }

    private Context mContext;

    @SuppressWarnings("deprecation")
    public SelectSongOperationPopup(Context context) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_song_operation_pop_window, null);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }

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
        }
    }

    public void showPopup(View rootView) {
        // 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }
}
