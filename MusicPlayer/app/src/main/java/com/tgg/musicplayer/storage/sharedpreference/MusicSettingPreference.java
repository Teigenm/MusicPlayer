package com.tgg.musicplayer.storage.sharedpreference;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see MusicSettingPreference
 * @since 2019/3/11
 */

@Preference(name = "music_setting")
public class MusicSettingPreference {

    private int position;

    private int playMode;

    private int isPlayListId;

    public MusicSettingPreference() {
    }

    public MusicSettingPreference(int position, int playMode, int isPlayListId) {
        this.position = position;
        this.playMode = playMode;
        this.isPlayListId = isPlayListId;
    }

    public MusicSettingPreference(int position, int playMode) {
        this.position = position;
        this.playMode = playMode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPlayMode() {
        return playMode;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    public int getIsPlayListId() {
        return isPlayListId;
    }

    public void setIsPlayListId(int isPlayListId) {
        this.isPlayListId = isPlayListId;
    }

    @Override
    public String toString() {
        return "MusicSettingPreference{" +
                "position=" + position +
                ", playMode=" + playMode +
                ", isPlayListId=" + isPlayListId +
                '}';
    }
}
