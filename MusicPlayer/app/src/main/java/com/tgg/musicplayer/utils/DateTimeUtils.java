package com.tgg.musicplayer.utils;

import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see DateTimeUtils
 * @since 2019/3/20
 */
public class DateTimeUtils {

    private static final SimpleDateFormat DEFAULT_DATE_TIME_FORMAT;
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT;
    private static final SimpleDateFormat TimeFormat_MINUTE;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DEFAULT_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault(Locale.Category.FORMAT));
            DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault(Locale.Category.FORMAT));
        } else {
            DEFAULT_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
        TimeFormat_MINUTE = new SimpleDateFormat("m:ss");
    }

    private DateTimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_TIME_FORMAT}
     *
     * @param millis
     * @return
     */
    public static String getDateTime(long millis) {
        return getTime(millis, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_TIME_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getDateTime(System.currentTimeMillis());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(System.currentTimeMillis(), dateFormat);
    }
    public static String getMinuteTimeInString(long millis) {
        return TimeFormat_MINUTE.format(millis);
    }
}
