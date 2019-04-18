package com.tgg.musicplayer.storage.database.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
  *
  * @Author:         tgg
  * @CreateDate:     2019/4/14 17:23
  * @Description:    id 1 默认歌单 id 2 我的收藏
  * @Version:        1.0
 */

@Entity(primaryKeys = {"music_id","song_list_id"},tableName = "list_in_music_info")
public class ListInMusicEntity {

    @ColumnInfo(name = "song_list_id")
    private long songListId;

    @ColumnInfo(name = "music_id")
    private long musicId;

    public ListInMusicEntity() {
    }

    @Ignore
    public ListInMusicEntity(long songListId , long musicId) {
        this.musicId = musicId;
        this.songListId = songListId;
    }

    public long getMusicId() {
        return musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }

    public long getSongListId() {
        return songListId;
    }

    public void setSongListId(long songListId) {
        this.songListId = songListId;
    }

}
