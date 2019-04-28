package com.tgg.musicplayer.storage.database.dao;

import com.tgg.musicplayer.storage.database.table.MusicEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MusicDao {
    @Query("SELECT * FROM music_info")
    List<MusicEntity>  getAll();

    @Query("SELECT * FROM music_info WHERE id IN (:musicIds)")
    List<MusicEntity> getMusicsByIds(List<Long> musicIds);


    @Query("SELECT * FROM music_info WHERE id = :musicId")
    MusicEntity getMusicById(Long musicId);

    @Query("SELECT a.id,a.song_name,a.singer_name,a.album_name,a.duration,a.size,a.path FROM music_info as a INNER JOIN recent_music_info as b ON b.music_id = a.id")
    List<MusicEntity> getMusicsRecentSort();

    @Query("SELECT a.id,a.song_name,a.singer_name,a.album_name,a.duration,a.size,a.path FROM music_info as a INNER JOIN list_in_music_info as b ON b.music_id = a.id WHERE b.song_list_id = :listId")
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

    @Query("DELETE FROM music_info WHERE id = :musicId")
    void deleteById(long musicId);
}
