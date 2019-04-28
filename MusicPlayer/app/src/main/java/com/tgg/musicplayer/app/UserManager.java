package com.tgg.musicplayer.app;



import com.tgg.musicplayer.service.MediaService;

public class UserManager {
    private MediaService.MyBinder mMyBinder;
    private int isPlayListId;
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

    public UserManager(MediaService.MyBinder myBinder, int isPlayListId) {
        mMyBinder = myBinder;
        this.isPlayListId = isPlayListId;
    }

    public UserManager(MediaService.MyBinder myBinder) {
        mMyBinder = myBinder;
    }

    public int getIsPlayListId() {
        return isPlayListId;
    }

    public void setIsPlayListId(int isPlayListId) {
        this.isPlayListId = isPlayListId;
    }

    public void setMyBinder(MediaService.MyBinder myBinder) {
        mMyBinder = myBinder;
    }

    public MediaService.MyBinder getMyBinder() {
        return mMyBinder;
    }

}
