package com.teigenm.coursemanager.database;

import android.app.Application;

import com.teigenm.coursemanager.database.dao.CourseDao;
import com.teigenm.coursemanager.database.dao.DiaryDao;
import com.teigenm.coursemanager.database.dao.StudentDao;
import com.teigenm.coursemanager.model.CourseEntity;
import com.teigenm.coursemanager.model.DiaryEntity;
import com.teigenm.coursemanager.model.StudentEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CourseEntity.class,StudentEntity.class,DiaryEntity.class}, version = 2,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * database name
     */
    private static final String DB_NAME = "fx";

    /**
     * 单例
     */
    private static AppDatabase sInstance;

    /**
     * 在Application中做初始化操作
     *
     * @param application a
     */
    public static void init(Application application) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(application, AppDatabase.class, DB_NAME).build();
                }
            }
        }
    }

    /**
     * 获取实例
     *
     * @return 没有初始化抛出异常
     */
    public static AppDatabase getInstance() {
        synchronized (AppDatabase.class) {
            if (sInstance == null) {
                throw new NullPointerException("database == null");
            }
        }
        return sInstance;
    }

    /**
     * 获取SubjectDao的实例
     *
     * @return SubjectDao实例
     */
    public abstract CourseDao getCourseDao();
    public abstract StudentDao getStudentDao();
    public abstract DiaryDao getDiaryDao();
}
