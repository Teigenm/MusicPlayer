package com.tgg.musicplayer.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;
import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.BaseActivity;
import com.tgg.musicplayer.ui.songlist.AllSongActivity;
import com.tgg.musicplayer.ui.songlist.MyFavoriteActivity;
import com.tgg.musicplayer.ui.songlist.RecentReleaseActivity;
import com.tgg.musicplayer.ui.view.SelectPlayListPopup;
import com.tgg.musicplayer.ui.view.SelectSongOperationPopup;
import com.tgg.musicplayer.utils.Toaster;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{

    private long mLastPressedTime = 0;
    private DrawerLayout mDrawerLayout;
    private ConstraintLayout mHomeHistoryLayout;
    private LinearLayout mHomeItemAllSongLayout;
    private LinearLayout mHomeItemMyFavoriteLayout;
    private LinearLayout mHomeItemRecentReleaseLayout;
    private ImageView iv_homeHistory;
    private ImageView iv_homePlayingSongList;
    private ImageView iv_homePlayingControl;

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

        iv_homeHistory = findViewById(R.id.home_history_image_view);
        iv_homeHistory.setOnClickListener(HomeActivity.this);

        mHomeItemAllSongLayout = findViewById(R.id.home_item_all_song_layout);
        mHomeItemAllSongLayout.setOnClickListener(HomeActivity.this);

        mHomeItemRecentReleaseLayout = findViewById(R.id.home_item_recent_release_layout);
        mHomeItemRecentReleaseLayout.setOnClickListener(HomeActivity.this);

        mHomeItemMyFavoriteLayout = findViewById(R.id.home_item_my_favorite_layout);
        mHomeItemMyFavoriteLayout.setOnClickListener(HomeActivity.this);

        iv_homePlayingSongList = findViewById(R.id.home_playing_song_list_image_view);
        iv_homePlayingSongList.setOnClickListener(HomeActivity.this);

        iv_homePlayingControl = findViewById(R.id.home_playing_control_image_view);
        iv_homePlayingControl.setOnClickListener(HomeActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);//指定Toolbar上的视图文件
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.menu_search:
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
            case R.id.home_item_recent_release_layout:
                RecentReleaseActivity.go(HomeActivity.this);
                break;
            case R.id.home_item_my_favorite_layout:
                MyFavoriteActivity.go(HomeActivity.this);
                break;
            case R.id.home_playing_song_list_image_view:
                SelectPlayListPopup playListPopup = new SelectPlayListPopup(this);
                playListPopup.showPopup(mDrawerLayout);
                break;
            case R.id.home_playing_control_image_view:
                SelectSongOperationPopup songOperationPopup = new SelectSongOperationPopup(this);
                songOperationPopup.showPopup(mDrawerLayout);
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
