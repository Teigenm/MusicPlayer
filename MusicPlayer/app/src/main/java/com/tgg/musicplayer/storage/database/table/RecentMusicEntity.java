package com.tgg.musicplayer.storage.database.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * @Author: tgg
 * @CreateDate: 2019/4/16 7:44
 * @Description: java类作用描述
 * @Version: 1.0
 */
@Entity(tableName = "recent_music_info")
public class RecentMusicEntity {

    @PrimaryKey
    @ColumnInfo(name = "music_id")
    private long musicId;

    @ColumnInfo(name = "add_time")
    private long addTime;

    @ColumnInfo(name = "play_times")
    private int playTimes;

    @Ignore
    private String songName; // 乐曲名
    @Ignore
    private String singerName; // 专辑名
    public RecentMusicEntity() {
    }

    @Ignore
    public RecentMusicEntity(long musicId, long addTime, int playTimes) {
        this.musicId = musicId;
        this.addTime = addTime;
        this.playTimes = playTimes;
    }

    @Ignore
    public RecentMusicEntity(int playTimes, String songName, String singerName) {
        this.playTimes = playTimes;
        this.songName = songName;
        this.singerName = singerName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public long getMusicId() {
        return musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public int getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }

    @Override
    public String toString() {
        return "RecentMusicEntity{" +
                "musicId=" + musicId +
                ", addTime=" + addTime +
                ", playTimes=" + playTimes +
                ", songName='" + songName + '\'' +
                ", singerName='" + singerName + '\'' +
                '}';
    }
}
