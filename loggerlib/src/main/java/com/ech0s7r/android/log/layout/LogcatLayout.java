package com.ech0s7r.android.log.layout;

import com.ech0s7r.android.log.LogMsg;
import com.ech0s7r.android.log.utils.Utils;

import java.util.Locale;

/**
 * @author ech0s7r
 */
public class LogcatLayout extends LogLayout {


    @Override
    public String format(LogMsg msg) {
        String exceptionStr = (msg.throwable != null) ? "\n" + Utils.getStackTraceString(msg.throwable) : "";
        if (exceptionStr == null || (exceptionStr.length() == 0 && msg.throwable != null)) {
            exceptionStr = msg.throwable.getMessage() + " [" + msg.throwable.getCause() + "]";
        }
        String str = String.format(Locale.US,
                "%d %s %s %s",
                msg.getThreadId(), // thread id
                msg.getMethodInfo(), // class.method
                msg.msg, // message
                exceptionStr // throwable message
        );
        return str;
    }
}
