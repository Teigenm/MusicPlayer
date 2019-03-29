package com.tangguogen.musicplayer_teigen.ui.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.tangguogen.musicplayer_teigen.R;
import com.tangguogen.musicplayer_teigen.app.BaseActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

public class SearchMusicActivity extends BaseActivity implements View.OnClickListener{

    private SearchView mSearchView;
    private ImageView mBackImageView;
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
    }
    private void initView() {
        mBackImageView = findViewById(R.id.search_music_back_image_view);
        mBackImageView.setOnClickListener(this);

        mSearchView = findViewById(R.id.search_music_search_view);
        mSearchView.onActionViewExpanded();
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchView.setFocusable(true);
        mSearchView.setQueryHint(getResources().getString(R.string.text_search_music));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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
}
