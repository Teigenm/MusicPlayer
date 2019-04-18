package com.tgg.musicplayer.storage.database;

import android.content.Context;

import com.tgg.musicplayer.storage.database.dao.ListInMusicDao;
import com.tgg.musicplayer.storage.database.dao.MusicDao;
import com.tgg.musicplayer.storage.database.dao.RecentMusicDao;
import com.tgg.musicplayer.storage.database.dao.SongListDao;
import com.tgg.musicplayer.storage.database.table.ListInMusicEntity;
import com.tgg.musicplayer.storage.database.table.MusicEntity;
import com.tgg.musicplayer.storage.database.table.RecentMusicEntity;
import com.tgg.musicplayer.storage.database.table.SongListEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MusicEntity.class, SongListEntity.class,ListInMusicEntity.class, RecentMusicEntity.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;
    private static final String DB_NAME = "music_player.db";
    private static Object sLock = new Object();

    public static AppDatabase getInstance() {
        synchronized (sLock) {
            if(sInstance == null) {
                throw new NullPointerException("database is null");
            }
        }
        return sInstance;
    }

    public static void initialize(Context context) {
        if(sInstance == null) {
            synchronized (sLock) {
                if(sInstance == null) {
                    sInstance = Room.databaseBuilder(context,AppDatabase.class,DB_NAME)
                            .build();
                }
            }
        }
    }
    public abstract MusicDao getMusicDao();
    public abstract SongListDao getSongListDao();
    public abstract ListInMusicDao getListInMusicDao();
    public abstract RecentMusicDao getRecentMusicDao();
}
