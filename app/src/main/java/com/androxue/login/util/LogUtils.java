package com.androxue.login.util;

import com.androxue.login.BuildConfig;

/**
 * Created by JimCharles on 2017/10/10.
 */

public class LogUtils {

    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void i(String tag, String msg) {
        if (DEBUG)
            android.util.Log.i(tag, msg);
    }

    public static void e(String msg) {
        if (DEBUG)
            android.util.Log.e("info", msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            android.util.Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG)
            android.util.Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG)
            android.util.Log.w(tag, msg);
    }
}
