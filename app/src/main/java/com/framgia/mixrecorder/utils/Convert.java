package com.framgia.mixrecorder.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by GIAKHANH on 1/15/2017.
 */
public class Convert {
    public static String convertTime(int duration) {
        if (duration < 0) return "";
        return TimeUnit.MILLISECONDS.toHours(duration) > 0 ?
            String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(duration),
                TimeUnit.MILLISECONDS.toMinutes(duration) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))) :
            String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}
