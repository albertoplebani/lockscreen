package com.ap.lockscreen;

import android.util.Log;

/**
 * Created by alberto on 08/04/16.
 */
public class L {

    final static boolean isDebug = true;

    final static String TAG = "LockScreen";

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void e(String msg, Exception ex) {
        if (isDebug)
            Log.e(TAG, msg, ex);
    }

    public static void time(long start, long end, String msg) {
        if (isDebug)
            Log.i(TAG, msg + " (" + (end - start) + " milliseconds)");
    }

    public static long mill() {
        return System.currentTimeMillis();
    }

}
