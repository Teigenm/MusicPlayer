package com.tgg.musicplayer.ui.view;

import android.widget.TextView;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.RecyclerAdapter;
import com.tgg.musicplayer.app.RecyclerViewHolder;
import com.tgg.musicplayer.model.MusicEntity;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class PlayListAdapter extends RecyclerAdapter<MusicEntity> {
    /**
     * the constructor of this class.
     *
     * @param items the data source.
     */
    public PlayListAdapter(@Nullable List<MusicEntity> items) {
        super(items);
    }

    @Override
    protected int getLayoutByViewType(int viewType) {
        return R.layout.item_song_simple_detail_layout;
    }

    @Override
    protected void onBindViewHolder(RecyclerViewHolder holder, int position, MusicEntity item) {
        TextView id = holder.get(R.id.item_song_simple_detail_number_text_view);
        TextView songName = holder.get(R.id.item_song_simple_detail_song_name_text_view);
        TextView singerName = holder.get(R.id.item_song_simple_detail_singer_name_text_view);

        if(item != null) {
            id.setText((position+1)+"");
            songName.setText(item.getSongName());
            singerName.setText(item.getSingerName());
        }
    }
}
