package com.tangguogen.musicplayer_teigen.app;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.tangguogen.musicplayer_teigen.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Class description:
 *
 * @author tgg
 * @version 1.0
 * @see BaseActivity
 * @since 2019/3/6
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
