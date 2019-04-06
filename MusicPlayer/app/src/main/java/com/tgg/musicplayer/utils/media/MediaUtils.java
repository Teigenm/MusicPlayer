package com.tgg.musicplayer.utils.media;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import com.tgg.musicplayer.model.MusicEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: tgg
 * @Date: 2019/4/6 17:17
  * @param null
 * @Description
 *
 */
public class MediaUtils {

    public static List<MusicEntity> getAudioList(Context context) {
        List<MusicEntity> list = new ArrayList<MusicEntity>();

        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        for(cursor.moveToFirst(); !cursor.isAfterLast() ; cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            for(int i = 0; i < cursor.getColumnCount() ; i++) {
                String colName = cursor.getColumnName(i);
                switch (cursor.getType(i)) {
                    case Cursor.FIELD_TYPE_INTEGER :
                        bundle.putInt(colName,cursor.getInt(i));
                        break;
                    case Cursor.FIELD_TYPE_STRING :
                        bundle.putString(colName,cursor.getString(i));
                        break;
                }
            }
            list.add(new MusicEntity(bundle));
        }
        cursor.close();
        return list;
    }
}
