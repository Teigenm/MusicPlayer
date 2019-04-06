package com.tgg.musicplayer.utils.media;

import android.content.Context;

import com.tgg.musicplayer.model.MusicEntity;
import com.tgg.musicplayer.utils.log.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: tgg
 * @Date: 2019/4/6 17:17
  * @param null
 * @Description
 *
 */
public class MusicList {
    private static Object sLock = new Object();
    private static List<MusicEntity> sMusicList = null;

    public static List<MusicEntity> getMusicList(Context context) {
        if(sMusicList == null) {
            synchronized(sLock) {
                if(sMusicList == null){
                    sMusicList = new ArrayList<MusicEntity>();
                    for(MusicEntity entity : MediaUtils.getAudioList(context) ) {
                        if(entity.isMusic() && entity.getDuration() > 40*1000){
                            Logger.e(entity.toString());
                            sMusicList.add(entity);
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(sMusicList);
    }
}
