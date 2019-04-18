package com.tgg.musicplayer.ui.home;

import android.widget.TextView;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.RecyclerAdapter;
import com.tgg.musicplayer.app.RecyclerViewHolder;
import com.tgg.musicplayer.storage.database.table.SongListEntity;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Author: tgg
 * @CreateDate: 2019/4/16 8:37
 * @Description: java类作用描述
 * @Version: 1.0
 */
public class SongListAdapter extends RecyclerAdapter<SongListEntity> {
    /**
     * the constructor of this class.
     *
     * @param items the data source.
     */
    public SongListAdapter(@Nullable List<SongListEntity> items) {
        super(items);
    }

    @Override
    protected int getLayoutByViewType(int viewType) {
        return R.layout.item_song_list_layout;
    }

    @Override
    protected void onBindViewHolder(RecyclerViewHolder holder, int position, SongListEntity item) {
        TextView title = holder.get(R.id.item_song_list_title_text_view);
        TextView description = holder.get(R.id.item_song_list_description_text_view);
        TextView songNumber = holder.get(R.id.item_song_list_song_number_text_view);

        if(item != null) {
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            songNumber.setText(item.getSongNumber()+"首");
        }
    }
}
