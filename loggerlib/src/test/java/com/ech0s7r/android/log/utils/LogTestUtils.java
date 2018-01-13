package com.ech0s7r.android.log.utils;

import android.os.Handler;
import android.os.Looper;

import com.ech0s7r.android.log.Logger;

import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.util.ReflectionHelpers;

/**
 * @author ech0s7r
 */
public class LogTestUtils {

    public static void waitLoggerLooper() {
        Logger logger = ReflectionHelpers.callStaticMethod(Logger.class, "getInstance");
        Handler handler = ReflectionHelpers.getField(logger, "mHandler");
        Looper looper = ShadowLooper.getLooperForThread(handler.getLooper().getThread());
        Shadows.shadowOf(looper).idleConstantly(true);
    }

    public static boolean hasMessage(String message) {
        waitLoggerLooper();
        for (ShadowLog.LogItem item : ShadowLog.getLogs()) {
            System.out.println("MARCO: " + item);
            if (item.msg.trim().equals(message)) {
                return true;
            }
        }
        return false;
    }

    public static int logSize() {
        waitLoggerLooper();
        return ShadowLog.getLogs().size();
    }

    public static String getLastLogMsg() {
        return ShadowLog.getLogs().get(ShadowLog.getLogs().size() - 1).msg;
    }
}
