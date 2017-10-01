package com.ech0s7r.android.log.appender;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ech0s7r.android.log.Logger;
import com.ech0s7r.android.log.layout.LogLayout;

import static com.ech0s7r.android.log.LoggerConfigurator.APP_NAME;

/**
 * Logger Logcat appender
 *
 * @author marco.rocco
 */
@SuppressWarnings("unused")
public class LogcatAppender extends LogAppender {

    public LogcatAppender(LogLayout layout) {
        super(layout);
    }

    @Override
    public void writeLog(Logger.Level logLevel, String msg, Throwable tr) {
        androidLog(logLevel, msg, tr);
    }

    @SuppressLint({"AndroidLogDetector"})
    private void androidLog(Logger.Level logLevel, String msg, Throwable tr) {
        switch (logLevel) {
            case DEBUG:
                Log.d(APP_NAME, msg);
                break;
            case ERROR:
            case ASSERT:
                //if (tr != null) {
                //Log.e(APP_NAME, msg, tr);
                //} else {
                Log.e(APP_NAME, msg);
                //}
                break;
            case INFO:
                Log.i(APP_NAME, msg);
                break;
            case VERBOSE:
                Log.v(APP_NAME, msg);
                break;
            case WARN:
                Log.w(APP_NAME, msg);
                break;
            default:
                break;
        }
    }

}
