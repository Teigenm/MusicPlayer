package com.teigenm.coursemanager.database.dao;

import com.teigenm.coursemanager.model.CourseEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;

@Dao
public interface CourseDao {

    @Query("select MAX(id) from course_info")
    Flowable<Integer> getCount();

    @Query("select * from course_info where id = :id")
    Flowable<CourseEntity> getOne(long id);

    @Query("select * from course_info where sid = :sid")
    Flowable<List<CourseEntity>> getAll(String sid);

    @Query("select * from course_info where sid = :sid and cweek_day = :weekDay order by csection_start,csection_end")
    Flowable<List<CourseEntity>> getWeekDay(String sid,int weekDay);

    @Insert
    void add(CourseEntity entitiey);

    @Delete
    void delete(CourseEntity entity);

    @Update
    void update(CourseEntity entity);
}
