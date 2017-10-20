package com.ech0s7r.android.log.layout;

import com.ech0s7r.android.log.LogMsg;
import com.ech0s7r.android.log.utils.Utils;

/**
 * @author marco.rocco
 */
public class LogcatLayout extends LogLayout {


    @Override
    public String format(LogMsg msg) {
        String exceptionStr = (msg.throwable != null) ? "\n" + Utils.getStackTraceString(msg.throwable) : "";
        if (exceptionStr == null || (exceptionStr.length() == 0 && msg.throwable != null)) {
            exceptionStr = msg.throwable.getMessage() + " [" + msg.throwable.getCause() + "]";
        }
        String str = String.format("%d %s %s %s",
                msg.getThreadId(), // thread id
                msg.getMethodInfo(), // class.method
                msg.msg, // message
                exceptionStr // throwable message
        );
        return str;
    }
}
