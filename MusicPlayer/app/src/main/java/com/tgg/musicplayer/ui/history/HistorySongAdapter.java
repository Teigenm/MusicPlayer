package com.tgg.musicplayer.ui.history;

import android.widget.TextView;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.RecyclerAdapter;
import com.tgg.musicplayer.app.RecyclerViewHolder;
import com.tgg.musicplayer.storage.database.table.RecentMusicEntity;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Author: tgg
 * @CreateDate: 2019/4/16 8:54
 * @Description: java类作用描述
 * @Version: 1.0
 */
public class HistorySongAdapter extends RecyclerAdapter<RecentMusicEntity> {

    /**
     * the constructor of this class.
     *
     * @param items the data source.
     */
    public HistorySongAdapter(@Nullable List items) {
        super(items);
    }

    @Override
    protected int getLayoutByViewType(int viewType) {
        return R.layout.item_history_song_layout;
    }
    @Override
    protected void onBindViewHolder(RecyclerViewHolder holder, int position, RecentMusicEntity item) {
        TextView songName = holder.get(R.id.item_history_song_name_text_view);
        TextView singerName = holder.get(R.id.item_history_singer_name_text_view);
        TextView historyTimes = holder.get(R.id.item_history_recent_release_times_text_view);

        if(item != null) {
            songName.setText(item.getSongName());
            singerName.setText(item.getSingerName());
            historyTimes.setText(item.getPlayTimes()+"次");
        }
    }

}
