package com.tgg.musicplayer.app;



import android.content.ServiceConnection;
import android.telecom.ConnectionService;

import com.tgg.musicplayer.model.MusicEntity;
import com.tgg.musicplayer.service.MediaService;

import java.util.List;

public class UserManager {
    private MediaService.MyBinder mMyBinder;
    private ServiceConnection mServiceConnection;
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

    public UserManager(MediaService.MyBinder myBinder, ServiceConnection serviceConnection) {
        mMyBinder = myBinder;
        mServiceConnection = serviceConnection;
    }

    public ServiceConnection getServiceConnection() {
        return mServiceConnection;
    }

    public void setServiceConnection(ServiceConnection serviceConnection) {
        mServiceConnection = serviceConnection;
    }


    public void setMyBinder(MediaService.MyBinder myBinder) {
        mMyBinder = myBinder;
    }

    public MediaService.MyBinder getMyBinder() {
        return mMyBinder;
    }

}
