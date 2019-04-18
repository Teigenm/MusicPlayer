package com.tgg.musicplayer.storage.database.dao;

import com.tgg.musicplayer.storage.database.table.SongListEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;

/**
 * @Author: tgg
 * @Date: 2019/4/14 9:13
 * @Description 0 表示
 *
 */
@Dao
public interface SongListDao {
    @Query("SELECT * FROM song_list_info")
    List<SongListEntity> getAll();

    @Query("SELECT * FROM song_list_info WHERE id = :id")
    SongListEntity getSongListById(long id);

    @Query("SELECT Count(*) FROM song_list_info ")
    int countSongList();

    @Query("SELECT * FROM song_list_info WHERE id != 1 AND id != 2")
    List<SongListEntity> getAllSongList();

    @Insert
    void add(SongListEntity entity);

    @Delete
    void delete(SongListEntity entity);

    @Update
    void update(SongListEntity entity);
}
