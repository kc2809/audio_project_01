package com.framgia.mixrecorder.utils;

import android.app.Activity;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hoang on 1/13/2017.
 */
public class Counter {
    private long mMilliseconds;
    private Timer mTimer;
    private String mElapsedTime;
    private SimpleDateFormat mFormatter;
    private boolean mIsPlaying;
    private Activity mActivity;
    private OnMillisencondListener mOnMillisencondListener;

    public Counter(final Activity activity, String timeFormat,
                   final OnMillisencondListener onMillisencondListener) {
        mActivity = activity;
        mOnMillisencondListener = onMillisencondListener;
        mFormatter = new SimpleDateFormat(timeFormat);
        mFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        mTimer = new Timer();
    }

    public void start() {
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mIsPlaying) mMilliseconds += 1;
                mElapsedTime = mFormatter.format(mMilliseconds);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOnMillisencondListener.onMillisecond(mElapsedTime);
                    }
                });
            }
        }, 1, 1);
    }

    public void play() {
        mIsPlaying = true;
    }

    public void pause() {
        mIsPlaying = false;
    }

    public void reset() {
        mMilliseconds = 0;
    }

    public void cancel() {
        mTimer.cancel();
    }

    public interface OnMillisencondListener {
        void onMillisecond(String elapsedTime);
    }
}
