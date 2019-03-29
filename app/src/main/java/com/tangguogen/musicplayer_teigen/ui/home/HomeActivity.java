package com.tangguogen.musicplayer_teigen.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;
import com.tangguogen.musicplayer_teigen.R;
import com.tangguogen.musicplayer_teigen.app.BaseActivity;
import com.tangguogen.musicplayer_teigen.utils.Toaster;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{

    private long mLastPressedTime = 0;
    private DrawerLayout mDrawerLayout;
    private ConstraintLayout mHomeHistoryLayout;
    private ImageView mHomeHistoryImageView;
    private LinearLayout mHomeItemAllSongLayout;

    public static void go(Context context){
        Intent intent = new Intent();
        intent.setClass(context,HomeActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.home_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.text_navigation_drawer_open, R.string.text_navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.home_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mHomeHistoryLayout = findViewById(R.id.home_history_layout);
        mHomeHistoryLayout.setOnClickListener(HomeActivity.this);

        mHomeHistoryImageView = findViewById(R.id.home_history_image_view);
        mHomeHistoryImageView.setOnClickListener(HomeActivity.this);

        mHomeItemAllSongLayout = findViewById(R.id.home_item_all_song_layout);
        mHomeItemAllSongLayout.setOnClickListener(HomeActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_title_menu, menu);//指定Toolbar上的视图文件
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.menu_home_search:
                SearchMusicActivity.go(HomeActivity.this);
                break;
                default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_night_mode){
            Toaster.showToast("11111");
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.home_history_layout:
                HistoryActivity.go(HomeActivity.this);
                break;
            case R.id.home_history_image_view:
                HistoryActivity.go(HomeActivity.this);
                break;
            case R.id.home_item_all_song_layout:
                AllSongActivity.go(HomeActivity.this);
                break;
            default:break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        long currentTime = System.currentTimeMillis();//获取第一次按键时间
        if (currentTime - mLastPressedTime > 2000) {
            Toaster.showToast("再按一次退出程序");
            mLastPressedTime = currentTime;
        } else {
            finish();
        }
    }
}
