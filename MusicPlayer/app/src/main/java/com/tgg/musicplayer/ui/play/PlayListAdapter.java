package com.tgg.musicplayer.ui.play;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tgg.musicplayer.R;
import com.tgg.musicplayer.app.RecyclerAdapter;
import com.tgg.musicplayer.app.RecyclerViewHolder;
import com.tgg.musicplayer.app.UserManager;
import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.storage.database.table.MusicEntity;

import java.util.List;

import androidx.annotation.Nullable;

public class PlayListAdapter extends RecyclerAdapter<MusicEntity> {
    /**
     * the constructor of this class.
     *
     * @param items the data source.
     */
    private MediaService.MyBinder mMyBinder;
    public PlayListAdapter(@Nullable List<MusicEntity> items) {
        super(items);
        mMyBinder = UserManager.getInstance().getMyBinder();
    }

    @Override
    protected int getLayoutByViewType(int viewType) {
        return R.layout.item_song_simple_detail_layout;
    }


    @Override
    protected void onBindViewHolder(RecyclerViewHolder holder, int position, MusicEntity item) {
        TextView numberTextView = holder.get(R.id.item_song_simple_detail_number_text_view);
        TextView songName = holder.get(R.id.item_song_simple_detail_song_name_text_view);
        TextView singerName = holder.get(R.id.item_song_simple_detail_singer_name_text_view);
        ImageView numberImageView = holder.get(R.id.item_song_simple_detail_number_image_view);
        if(item != null) {
            numberTextView.setText((position+1)+"");
            songName.setText(item.getSongName());
            singerName.setText(item.getSingerName());
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
