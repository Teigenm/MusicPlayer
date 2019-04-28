package com.tgg.musicplayer.storage.database.dao;

import com.tgg.musicplayer.storage.database.table.RecentMusicEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * @Author: tgg
 * @CreateDate: 2019/4/16 7:50
 * @Description: java类作用描述
 * @Version: 1.0
 */
@Dao
public interface RecentMusicDao {

    @Query("SELECT * FROM recent_music_info WHERE music_id = :musicId")
    RecentMusicEntity getRecentMusicById(long musicId);

    @Query("SELECT music_id FROM recent_music_info")
    List<Long> getAllMusicId();

    @Query("SELECT COUNT(*) FROM recent_music_info")
    int getAllCount();

    @Query("SELECT * FROM recent_music_info ORDER BY play_times DESC LIMIT 1")
    RecentMusicEntity getMaxTimesRecentEntity();

    @Query("SELECT * FROM recent_music_info")
    List<RecentMusicEntity> getAll();

    @Query("SELECT * FROM recent_music_info ORDER BY add_time DESC")
    List<RecentMusicEntity> getAllMusicIdSort();

    @Query("SELECT * FROM recent_music_info ORDER BY play_times DESC LIMIT 10")
    List<RecentMusicEntity> getMostTimes();

    @Update
    void update(RecentMusicEntity entity);

    @Insert
    void add(RecentMusicEntity entity);

    @Delete
    void delete(RecentMusicEntity entity);

    @Query("DELETE FROM recent_music_info WHERE music_id = :musicId")
    void deleteById(long musicId);
}
