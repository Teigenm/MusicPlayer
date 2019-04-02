package com.tgg.musicplayer.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.ToolbarActivity;

public class HistoryActivity extends ToolbarActivity {

    public static void go(Context context){
        Intent intent = new Intent();
        intent.setClass(context,HistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_layout);
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
}
