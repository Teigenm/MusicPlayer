package com.tgg.musicplayer.storage.database.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.tgg.musicplayer.utils.Validator;
import com.tgg.musicplayer.utils.loader.AudioEntity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "music_info")
public class MusicEntity implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id; // 编号

    @ColumnInfo(name = "song_name")
    private String songName; // 乐曲名

    @ColumnInfo(name = "album_name")
    private String albumName; // 专辑名

    @ColumnInfo(name = "duration")
    private int duration; // 播放时长

    @ColumnInfo(name = "size")
    private long size; // 文件大小

    @ColumnInfo(name = "singer_name")
    private String singerName; // 演唱者

    @ColumnInfo(name = "path")
    private String path; // 文件路径

    public MusicEntity() {}

    @Ignore
    public MusicEntity(AudioEntity entity) {
        this.id = entity.getId();
        this.songName = entity.getTitle();
        this.singerName = entity.getArtist();
        this.albumName = entity.getAlbum();
        this.duration = entity.getDuration();
        this.size = entity.getSize();
        this.path = entity.getPath();
        if(!Validator.isLetterDigitOrChinese(this.songName) || !Validator.isLetterDigitOrChinese(this.singerName)) {
            this.songName = entity.getDisplayName().split("\\.")[0];
            this.singerName = "";
        }
    }

    @Ignore
    public MusicEntity(long id, String songName, String albumName, int duration, long size, String singerName, String path) {
        this.id = id;
        this.songName = songName;
        this.albumName = albumName;
        this.duration = duration;
        this.size = size;
        this.singerName = singerName;
        this.path = path;
    }

    @Ignore
    protected MusicEntity(Parcel in) {
        id = in.readLong();
        songName = in.readString();
        albumName = in.readString();
        duration = in.readInt();
        size = in.readLong();
        singerName = in.readString();
        path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(songName);
        dest.writeString(albumName);
        dest.writeInt(duration);
        dest.writeLong(size);
        dest.writeString(singerName);
        dest.writeString(path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MusicEntity> CREATOR = new Creator<MusicEntity>() {
        @Override
        public MusicEntity createFromParcel(Parcel in) {
            return new MusicEntity(in);
        }

        @Override
        public MusicEntity[] newArray(int size) {
            return new MusicEntity[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getSongName() {
        return songName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public int getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    public String getSingerName() {
        return singerName;
    }

    public String getPath() {
        return path;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "MusicEntity{" +
                "id=" + id +
                ", songName='" + songName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", singerName='" + singerName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}