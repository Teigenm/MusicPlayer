package com.tgg.musicplayer.storage.database.dao;

import com.tgg.musicplayer.storage.database.table.ListInMusicEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ListInMusicDao {

    @Query("SELECT * FROM list_in_music_info WHERE song_list_id = :songListId AND music_id = :musicId")
    ListInMusicEntity getListInMusic(long songListId, long musicId);

    @Query("SELECT music_id FROM list_in_music_info WHERE song_list_id = :songListId")
    List<Long> getMusicIdListByListId(long songListId);

    @Query("SELECT COUNT(*) FROM list_in_music_info WHERE song_list_id = :songListId")
    int getListCount(long songListId);

    @Update
    void update(ListInMusicEntity entity);

    @Insert
    void add(ListInMusicEntity entity);

    @Delete
    void delete(ListInMusicEntity entity);

    @Insert
    void addAll(List<ListInMusicEntity> list);

    @Query("DELETE FROM list_in_music_info WHERE song_list_id = :listId")
    void deleteByListId(long listId);

    @Query("DELETE FROM list_in_music_info WHERE song_list_id = :listId and music_id = :musicId")
    void deleteByIds(long listId,long musicId);

    @Query("DELETE FROM list_in_music_info WHERE song_list_id = :musicId")
    void deleteByMusicId(long musicId);
}
