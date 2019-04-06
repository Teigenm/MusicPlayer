package com.tgg.musicplayer.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;



public class MusicEntity implements Parcelable {
    private String mTitle;
    private String mTitleKey;
    private String mArtist;
    private String mArtistKey;
    private String mAlbum;
    private String mAlbumKey;
    private String mAlbumArtist;
    private String mComposer;
    private String mPath;
    private String mDisplayName;
    private String mMimeType;

    private int mId;
    private int mSize;
    private int mDateAdd;
    private int mDuration;
    private int mArtistId;
    private int mAlbumId;
    private int mTrack;
    private int mYear;

    private boolean mIsDrm;
    private boolean mIsRingtone;
    private boolean mIsMusic;
    private boolean mIsAlarm;
    private boolean mIsNotification;
    private boolean mIsPodcast;

    public MusicEntity(Bundle bundle) {
        mTitle = bundle.getString(MediaStore.Audio.AudioColumns.TITLE);
        mTitleKey = bundle.getString(MediaStore.Audio.AudioColumns.TITLE_KEY);
        mArtist = bundle.getString(MediaStore.Audio.AudioColumns.ARTIST);
        mArtistKey = bundle.getString(MediaStore.Audio.AudioColumns.ARTIST_KEY);
        mAlbum = bundle.getString(MediaStore.Audio.AudioColumns.ALBUM);
        mAlbumKey = bundle.getString(MediaStore.Audio.AudioColumns.ALBUM_KEY);
        //mAlbumArtist = bundle.getString(MediaStore.MusicEntity.AudioColumns.ALBUM_ARTIST);
        mAlbumArtist = null;
        mComposer = bundle.getString(MediaStore.Audio.AudioColumns.COMPOSER);
        mPath = bundle.getString(MediaStore.Audio.AudioColumns.DATA);
        mDisplayName = bundle.getString(MediaStore.Audio.AudioColumns.DISPLAY_NAME);
        mMimeType = bundle.getString(MediaStore.Audio.AudioColumns.MIME_TYPE);

        mId = bundle.getInt(MediaStore.Audio.AudioColumns._ID);
        mSize = bundle.getInt(MediaStore.Audio.AudioColumns.SIZE);
        mDateAdd = bundle.getInt(MediaStore.Audio.AudioColumns.DATE_ADDED);
        mDuration = bundle.getInt(MediaStore.Audio.AudioColumns.DURATION);
        mArtistId = bundle.getInt(MediaStore.Audio.AudioColumns.ARTIST_ID);
        mAlbumId = bundle.getInt(MediaStore.Audio.AudioColumns.ALBUM_ID);
        mTrack = bundle.getInt(MediaStore.Audio.AudioColumns.TRACK);
        mYear = bundle.getInt(MediaStore.Audio.AudioColumns.YEAR);

        mIsDrm = bundle.getInt(MediaStore.Audio.AudioColumns.IS_ALARM) == 1;
        mIsRingtone = bundle.getInt(MediaStore.Audio.AudioColumns.IS_RINGTONE) == 1;
        mIsMusic = bundle.getInt(MediaStore.Audio.AudioColumns.IS_MUSIC) == 1;
        mIsAlarm = bundle.getInt(MediaStore.Audio.AudioColumns.IS_ALARM) == 1;
        mIsNotification = bundle.getInt(MediaStore.Audio.AudioColumns.IS_NOTIFICATION) == 1;
        mIsPodcast = bundle.getInt(MediaStore.Audio.AudioColumns.IS_PODCAST) == 1;
    }

    protected MusicEntity(Parcel in) {
        mTitle = in.readString();
        mTitleKey = in.readString();
        mArtist = in.readString();
        mArtistKey = in.readString();
        mAlbum = in.readString();
        mAlbumKey = in.readString();
        mAlbumArtist = in.readString();
        mComposer = in.readString();
        mPath = in.readString();
        mDisplayName = in.readString();
        mMimeType = in.readString();
        mId = in.readInt();
        mSize = in.readInt();
        mDateAdd = in.readInt();
        mDuration = in.readInt();
        mArtistId = in.readInt();
        mAlbumId = in.readInt();
        mTrack = in.readInt();
        mYear = in.readInt();
        mIsDrm = in.readByte() != 0;
        mIsRingtone = in.readByte() != 0;
        mIsMusic = in.readByte() != 0;
        mIsAlarm = in.readByte() != 0;
        mIsNotification = in.readByte() != 0;
        mIsPodcast = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mTitleKey);
        dest.writeString(mArtist);
        dest.writeString(mArtistKey);
        dest.writeString(mAlbum);
        dest.writeString(mAlbumKey);
        dest.writeString(mAlbumArtist);
        dest.writeString(mComposer);
        dest.writeString(mPath);
        dest.writeString(mDisplayName);
        dest.writeString(mMimeType);
        dest.writeInt(mId);
        dest.writeInt(mSize);
        dest.writeInt(mDateAdd);
        dest.writeInt(mDuration);
        dest.writeInt(mArtistId);
        dest.writeInt(mAlbumId);
        dest.writeInt(mTrack);
        dest.writeInt(mYear);
        dest.writeByte((byte) (mIsDrm ? 1 : 0));
        dest.writeByte((byte) (mIsRingtone ? 1 : 0));
        dest.writeByte((byte) (mIsMusic ? 1 : 0));
        dest.writeByte((byte) (mIsAlarm ? 1 : 0));
        dest.writeByte((byte) (mIsNotification ? 1 : 0));
        dest.writeByte((byte) (mIsPodcast ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MusicEntity> CREATOR = new Creator<MusicEntity>() {
        @Override
        public MusicEntity createFromParcel(Parcel in) {
            return new MusicEntity(in);
        }

        @Override
        public MusicEntity[] newArray(int size) {
            return new MusicEntity[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getTitleKey() {
        return mTitleKey;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getArtistKey() {
        return mArtistKey;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public String getAlbumKey() {
        return mAlbumKey;
    }

    public String getAlbumArtist() {
        return mAlbumArtist;
    }

    public String getComposer() {
        return mComposer;
    }

    public String getPath() {
        return mPath;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public int getId() {
        return mId;
    }

    public int getSize() {
        return mSize;
    }

    public int getDateAdd() {
        return mDateAdd;
    }

    public int getDuration() {
        return mDuration;
    }

    public int getArtistId() {
        return mArtistId;
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public int getTrack() {
        return mTrack;
    }

    public int getYear() {
        return mYear;
    }

    public boolean isDrm() {
        return mIsDrm;
    }

    public boolean isRingtone() {
        return mIsRingtone;
    }

    public boolean isMusic() {
        return mIsMusic;
    }

    public boolean isAlarm() {
        return mIsAlarm;
    }

    public boolean isNotification() {
        return mIsNotification;
    }

    public boolean isPodcast() {
        return mIsPodcast;
    }
}
