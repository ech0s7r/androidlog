package com.ech0s7r.android.log.appender;

import com.ech0s7r.android.log.Logger;
import com.ech0s7r.android.log.layout.LogLayout;


/**
 * Abstract LogAppender
 *
 * @author marco.rocco
 */

public abstract class LogAppender {

    private LogLayout mLogLayout;

    protected LogAppender(LogLayout layout) {
        mLogLayout = layout;
    }

    public LogLayout getLogLayout() {
        return mLogLayout;
    }


    public abstract void writeLog(Logger.Level logLevel, String msg, Throwable tr);
}
