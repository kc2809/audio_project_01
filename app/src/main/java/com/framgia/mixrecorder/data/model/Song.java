package com.framgia.mixrecorder.data.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GIAKHANH on 1/12/2017.
 */
public class Song {
    private final String DATE_FORMAT = "hh:mm:ss a on yyyy-MM-dd";
    private String mName;
    private String mPath;
    private Date mLastModified;
    private long mDuration;

    public Song() {
    }

    public Song(String name, String path, long duration, Date lastModified) {
        mName = name;
        mPath = path;
        this.mDuration = duration;
        mLastModified = lastModified;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public Date getLastModified() {
        return mLastModified;
    }

    public void setLastModified(Date lastModified) {
        mLastModified = lastModified;
    }

    public String getStringLastModified() {
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        return (String) df.format(DATE_FORMAT, mLastModified);
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public static List<Song> getSongs(Context context) {
        List<Song> listSong = new ArrayList<>();
        Cursor cur = context.getContentResolver()
            .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cur != null && cur.moveToFirst()) {
            do {
                String title = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String path = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                long duration = cur.getLong(cur.getColumnIndex(MediaStore.Audio.Media.DURATION));
                Date date = new Date(cur.getLong(cur.getColumnIndex(MediaStore.Audio.Media
                    .DATE_MODIFIED)));
                listSong.add(new Song(title, path, duration, date));
            } while (cur.moveToNext());
        }
        return listSong;
    }

    @Override
    public String toString() {
        return mName;
    }
}
