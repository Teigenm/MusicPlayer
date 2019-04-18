package com.tgg.musicplayer.storage.database.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * @Author: tgg
 * @Date: 2019/4/11 18:31
  * @param null
 * @Description
 *
 */
@Entity(tableName = "song_list_info")
public class SongListEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "create_time")
    private long createTime;

    @ColumnInfo(name = "description")
    private String description;

    @Ignore
    private int songNumber;

    @Ignore
    private int isPlaying;

    public SongListEntity() {

    }

    @Ignore
    public SongListEntity(String title, long createTime) {
        this.title = title;
        this.createTime = createTime;
    }
    @Ignore
    public SongListEntity(long id, String title, long createTime) {
        this.id = id;
        this.title = title;
        this.createTime = createTime;
    }

    @Ignore
    public SongListEntity( String title, long createTime, String description) {
        this.id = id;
        this.title = title;
        this.createTime = createTime;
        this.description = description;
    }
    @Ignore
    public SongListEntity(long id, String title, long createTime, String description, int songNumber) {
        this.id = id;
        this.title = title;
        this.createTime = createTime;
        this.description = description;
        this.songNumber = songNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(int songNumber) {
        this.songNumber = songNumber;
    }

    public int getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(int isPlaying) {
        this.isPlaying = isPlaying;
    }

    @Override
    public String toString() {
        return "SongListEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", description='" + description + '\'' +
                ", songNumber=" + songNumber +
                '}';
    }
}
