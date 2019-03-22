package com.tangguogen.musicplayer_teigen.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.tangguogen.musicplayer_teigen.app.BaseActivity;
<<<<<<< HEAD
import com.tangguogen.musicplayer_teigen.ui.HomeActivity;
=======
<<<<<<< HEAD
import com.tangguogen.musicplayer_teigen.ui.HomeActivity;
=======
import com.tangguogen.musicplayer_teigen.ui.home.HomeActivity;
>>>>>>> commit
>>>>>>> create

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see SplashActivity
 * @since 2019/3/12
 */
public class SplashActivity extends BaseActivity implements Runnable {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN //hide statusBar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; //hide navigationBar
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void run() {
        HomeActivity.go(this);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler != null) {
            mHandler.removeCallbacks(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(this, 2000);
    }
}
