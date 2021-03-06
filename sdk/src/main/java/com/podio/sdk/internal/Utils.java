
package com.podio.sdk.internal;

import android.annotation.SuppressLint;
import android.net.Uri;

import java.io.Closeable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static long currentTimeSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // Intentionally and silently consume the error.
            }
        }
    }

    public static String formatDateUtc(Date date) {
        try {
            return getUtcSimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String formatDateTimeUtc(Date dateTime) {
        try {
            return getUtcSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTime);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String formatDateDefault(Date date) {
        try {
            return getDefaultSimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String formatDateTimeDefault(Date dateTime) {
        try {
            return getDefaultSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTime);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static boolean getNative(Boolean object, boolean fallback) {
        return object != null ? object.booleanValue() : fallback;
    }

    public static double getNative(Double object, double fallback) {
        return object != null ? object.doubleValue() : fallback;
    }

    public static int getNative(Integer object, int fallback) {
        return object != null ? object.intValue() : fallback;
    }

    public static long getNative(Long object, long fallback) {
        return object != null ? object.longValue() : fallback;
    }

    public static <T> T getObject(T target, T fallback) {
        return target != null ? target : fallback;
    }

    public static boolean isAnyEmpty(String... strings) {
        boolean isEmpty = isEmpty(strings);

        for (int i = 0; !isEmpty && i < strings.length; i++) {
            isEmpty = isEmpty(strings[i]);
        }

        return isEmpty;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isEmpty(Uri uri) {
        return uri == null || uri.equals(Uri.EMPTY);
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean notAnyEmpty(String... strings) {
        return !isAnyEmpty(strings);
    }

    public static boolean notEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean notEmpty(Uri uri) {
        return !isEmpty(uri);
    }

    public static boolean notEmpty(byte[] array) {
        return !isEmpty(array);
    }

    public static boolean notEmpty(int[] array) {
        return !isEmpty(array);
    }

    public static boolean notEmpty(long[] array) {
        return !isEmpty(array);
    }

    public static boolean notEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean notEmpty(Object[] array) {
        return !isEmpty(array);
    }

    public static boolean notEmpty(String string) {
        return !isEmpty(string);
    }

    public static String join(String[] array, String seperator) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; array != null && i < array.length; i++) {
            result.append(array[i]);
            if (i < array.length - 1) {
                result.append(seperator);
            }
        }
        return result.toString();
    }

    /**
     * Takes a date and a time in utc.
     *
     * @param dateTime
     * @return
     */
    public static Date parseDateTimeUtc(String dateTime) {
        try {
            return getUtcSimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Takes a date in utc.
     *
     * @param date
     * @return
     */
    public static Date parseDateUtc(String date) {
        try {
            return getUtcSimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Takes time in utc.
     *
     * @param time
     * @return
     */
    public static Date parseTimeUtc(String time) {
        try {
            return getUtcSimpleDateFormat("HH:mm:ss").parse(time);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Takes date and time in the device default timezone.
     *
     * @param dateTime
     * @return
     */
    public static Date parseDateTimeDefault(String dateTime) {
        try {
            return getDefaultSimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Takes date in the device default timezone.
     *
     * @param date
     * @return
     */
    public static Date parseDateDefault(String date) {
        try {
            return getDefaultSimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Takes time in the device default timezone.
     *
     * @param time
     * @return
     */
    public static Date parseTimeDefault(String time) {
        try {
            return getDefaultSimpleDateFormat("HH:mm:ss").parse(time);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat getUtcSimpleDateFormat(String pattern) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat;
    }

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat getDefaultSimpleDateFormat(String pattern) {
        TimeZone timeZone = TimeZone.getDefault();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat;
    }
}
