package com.tgg.musicplayer.ui.search;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.RecyclerAdapter;
import com.tgg.musicplayer.app.RecyclerViewHolder;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.utils.DateTimeUtils;

import java.util.List;

/**
 * @Author: tgg
 * @CreateDate: 2019/4/19 17:23
 * @Description: java类作用描述
 * @Version: 1.0
 */
public class SearchMusicAdapter extends RecyclerAdapter<MusicEntity> {

    /**
     * the constructor of this class.
     *
     * @param items the data source.
     */
    public SearchMusicAdapter(@Nullable List<MusicEntity> items) {
        super(items);
    }

    @Override
    protected int getLayoutByViewType(int viewType) {
        return R.layout.item_search_music_layout;
    }

    @Override
    protected void onBindViewHolder(RecyclerViewHolder holder, int position, MusicEntity item) {
        TextView number = holder.get(R.id.item_search_music_number_text_view);
        TextView songName = holder.get(R.id.item_search_music_song_name_text_view);
        TextView singerName = holder.get(R.id.item_search_music_singer_name_text_view);
        TextView albumName = holder.get(R.id.item_search_music_album_name_text_view);
        TextView durationTime = holder.get(R.id.item_search_music_duration_time_text_view);

        if(item != null) {
            number.setText(String.valueOf(position+1) );
            songName.setText(item.getSongName() );
            singerName.setText("艺术家:"+item.getSingerName() );
            albumName.setText("专辑:"+item.getAlbumName() );
            durationTime.setText(DateTimeUtils.getMinuteTimeInString(item.getDuration() ) );
        }
    }
}
