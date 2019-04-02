package com.teigenm.coursemanager.database.dao;

import com.teigenm.coursemanager.model.StudentEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;

@Dao
public interface StudentDao {

    @Query("select * from student_info")
    Flowable<List<StudentEntity>> getAll();

    @Query("select COUNT(*) from student_info where sid = :sid and spassword = :spassword")
    Flowable<Integer> login(String sid, String spassword);

    @Query("select COUNT(*) from student_info where sid = :sid and spassword = :spassword")
    Integer login_one(String sid, String spassword);

    @Query("select * from student_info where sid = :sid")
    Flowable<StudentEntity> getOne(String sid);

    @Insert
    void add(StudentEntity entities);

    @Delete
    void delete(StudentEntity entity);

    @Update
    void update(StudentEntity entity);
}
