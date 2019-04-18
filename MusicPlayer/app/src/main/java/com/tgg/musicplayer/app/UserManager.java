package com.tgg.musicplayer.app;



import com.tgg.musicplayer.service.MediaService;
import com.tgg.musicplayer.storage.database.table.MusicEntity;

import java.util.List;

public class UserManager {
    private MediaService.MyBinder mMyBinder;
    private List<MusicEntity> mAllList;
    private static UserManager sUserManager;

    public static UserManager getInstance() {
        if(sUserManager == null) {
            synchronized (UserManager.class) {
                if(sUserManager == null) {
                    sUserManager = new UserManager();
                }
            }
        }
        return sUserManager;
    }

    public UserManager() {
    }

    public UserManager(MediaService.MyBinder myBinder) {
        mMyBinder = myBinder;
    }

    public UserManager(MediaService.MyBinder myBinder, List<MusicEntity> list) {
        mMyBinder = myBinder;
        mAllList = list;
    }

    public List<MusicEntity> getAllList() {
        return mAllList;
    }

    public void setAllList(List<MusicEntity> allList) {
        mAllList = allList;
    }

    public void setMyBinder(MediaService.MyBinder myBinder) {
        mMyBinder = myBinder;
    }

    public MediaService.MyBinder getMyBinder() {
        return mMyBinder;
    }

}
