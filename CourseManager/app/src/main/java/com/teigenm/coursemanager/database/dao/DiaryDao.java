package com.teigenm.coursemanager.database.dao;

import com.teigenm.coursemanager.model.DiaryEntity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;

@Dao
public interface DiaryDao {

    @Query("select * from diary_info where id = :id")
    Flowable<DiaryEntity> getOne(long id);

    @Insert
    void add(DiaryEntity entitiey);

    @Delete
    void delete(DiaryEntity entity);

    @Update
    void update(DiaryEntity entity);
}
