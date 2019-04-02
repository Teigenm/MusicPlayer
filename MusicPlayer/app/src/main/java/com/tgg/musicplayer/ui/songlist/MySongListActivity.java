package com.tgg.musicplayer.ui.songlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.BaseActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class MySongListActivity extends BaseActivity {

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    public static void go(Context context){
        Intent intent = new Intent();
        intent.setClass(context,MySongListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_song_list_layout);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.my_song_list_app_bar);
        setSupportActionBar(toolbar);

        Drawable drawable = toolbar.getNavigationIcon();
        if (drawable != null) {
            DrawableCompat.setTint(drawable, ResourcesCompat.getColor(getResources(), android.R.color.white, null));
        }

        mCollapsingToolbarLayout = findViewById(R.id.my_song_list_collapsing_layout);
        mCollapsingToolbarLayout.setExpandedTitleColor(ResourcesCompat.getColor(getResources(), android.R.color.white, null));


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_song_list_menu, menu);//指定Toolbar上的视图文件
        return true;
    }
}
