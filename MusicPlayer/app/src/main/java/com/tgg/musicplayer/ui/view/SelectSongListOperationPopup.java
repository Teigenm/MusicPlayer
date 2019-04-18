package com.tgg.musicplayer.ui.view;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.ui.home.EditOrAddSongListActivity;

public class SelectSongListOperationPopup implements View.OnClickListener {
    public PopupWindow mPopupWindow;
    private SelectSongListOperationPopupOnClickListener selectSongListOperationPopupOnClickListener;
    private LinearLayout mEditSongListInfoLayout;
    private LinearLayout mDeleteSongListLayout;

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }

    private Context mContext;
    private long mListId;

    @SuppressWarnings("deprecation")
    public SelectSongListOperationPopup(Context context,long listId) {
        mContext = context;
        mListId = listId;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_song_list_operation_pop_window, null);
        mEditSongListInfoLayout = view.findViewById(R.id.song_list_operation_edit_song_list_info_layout);
        mEditSongListInfoLayout.setOnClickListener(this);

        mDeleteSongListLayout = view.findViewById(R.id.song_list_operation_delete_song_list_layout);
        mDeleteSongListLayout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.song_list_operation_edit_song_list_info_layout:
                EditOrAddSongListActivity.go(mContext,1,mListId);
                break;
            default:
                break;
        }

    }

    public interface SelectSongListOperationPopupOnClickListener {
        void obtainMessage(int flag, String ret);
    }

    public void SelectSongListOperationPopupOnClickListener(SelectSongListOperationPopupOnClickListener l) {
        this.selectSongListOperationPopupOnClickListener = l;
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
