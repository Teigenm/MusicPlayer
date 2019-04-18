package com.tgg.musicplayer.utils.loader;

import android.content.Context;

import com.tgg.musicplayer.storage.database.table.MusicEntity;
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
public class MusicLoader {
    private static Object sLock = new Object();
    private static List<MusicEntity> sMusicList = null;

    public static List<MusicEntity> getMusicList(Context context) {
        if(sMusicList == null) {
            synchronized(sLock) {
                if(sMusicList == null){
                    sMusicList = new ArrayList<MusicEntity>();
                    for(AudioEntity entity : MediaUtils.getAudioList(context) ) {
                        if(entity.isMusic() && entity.getDuration() > 40*1000){
                            MusicEntity musicEntity = new MusicEntity(entity);
                            //Logger.d(entity.toString());
                            sMusicList.add(musicEntity);
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(sMusicList);
    }
}
