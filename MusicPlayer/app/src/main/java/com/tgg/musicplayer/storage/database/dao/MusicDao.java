package com.tgg.musicplayer.storage.database.dao;

import android.util.Log;

import com.tgg.musicplayer.storage.database.table.ListInMusicEntity;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.storage.database.table.RecentMusicEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;

@Dao
public interface MusicDao {
    @Query("SELECT * FROM music_info LIMIT 80")
    List<MusicEntity>  getAll();

    @Query("SELECT * FROM music_info WHERE id IN (:musicIds)")
    List<MusicEntity> getMusicsByIds(List<Long> musicIds);


    @Query("SELECT * FROM music_info WHERE id = :musicId")
    MusicEntity getMusicById(Long musicId);

    @Query("SELECT a.id,a.album_name,a.duration,a.path,a.singer_name,a.size,a.song_name FROM music_info as a INNER JOIN recent_music_info as b ON b.music_id = a.id ORDER BY b.add_time DESC")
    List<MusicEntity> getMusicsRecentSort();

    @Query("SELECT a.id,a.album_name,a.duration,a.path,a.singer_name,a.size,a.song_name FROM music_info as a INNER JOIN list_in_music_info as b ON b.music_id = a.id WHERE b.song_list_id = :listId ORDER BY a.id ASC")
    List<MusicEntity> getMusicsByListId(long listId);

    @Query("SELECT Count(*) FROM music_info")
    int getAllMusicCount();

    @Insert
    void add(MusicEntity entity);

    @Update
    void update(MusicEntity entity);

    @Delete
    void delete(MusicEntity entity);

    @Insert
    void addAll(List<MusicEntity> list);
}
