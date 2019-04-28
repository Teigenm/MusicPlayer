package com.tgg.musicplayer.storage.database.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

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

    @ColumnInfo(name = "add_time")
    private long addTime;

    public ListInMusicEntity() {
        this.addTime = System.currentTimeMillis();
    }

    @Ignore
    public ListInMusicEntity(long songListId , long musicId) {
        this.musicId = musicId;
        this.songListId = songListId;
        this.addTime = System.currentTimeMillis();
    }

    @Ignore
    public ListInMusicEntity(long songListId, long musicId, long addTime) {
        this.songListId = songListId;
        this.musicId = musicId;
        this.addTime = addTime;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
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


    @Override
    public String toString() {
        return "ListInMusicEntity{" +
                "songListId=" + songListId +
                ", musicId=" + musicId +
                '}';
    }
}
