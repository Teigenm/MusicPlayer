package com.tgg.musicplayer.ui.song;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.RecyclerAdapter;
import com.tgg.musicplayer.app.RecyclerViewHolder;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.utils.DateTimeUtils;

import java.util.List;

import androidx.annotation.Nullable;

public class SingleSongListAdapter extends RecyclerAdapter<MusicEntity> {

    /**
     * the constructor of this class.
     *
     * @param items the data source.
     */
    private Context mContext;
    public static int mPos = 0;
    public SingleSongListAdapter(@Nullable List<MusicEntity> items) {
        super(items);
    }
    @Override
    protected int getLayoutByViewType(int viewType) {
        return R.layout.item_song_detail_layout;
    }

    @Override
    protected void onBindViewHolder(RecyclerViewHolder holder, int position, MusicEntity item) {
        TextView numberTextView = holder.get(R.id.item_song_detail_number_text_view);
        ImageView numberImageView = holder.get(R.id.item_song_detail_number_image_view);
        TextView songName = holder.get(R.id.item_song_detail_song_name_text_view);
        TextView singerName = holder.get(R.id.item_song_detail_singer_name_text_view);
        TextView durationTime = holder.get(R.id.item_song_detail_duration_time_text_view);
        if(item != null) {
            numberTextView.setText(String.valueOf(position+1));
            songName.setText(item.getSongName());
            singerName.setText(item.getSingerName());
            durationTime.setText(DateTimeUtils.getMinuteTimeInString(item.getDuration()));
            if(item.getIsPlaying() ) {
                numberTextView.setVisibility(View.INVISIBLE);
                numberImageView.setVisibility(View.VISIBLE);
            } else {
                numberTextView.setVisibility(View.VISIBLE);
                numberImageView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
