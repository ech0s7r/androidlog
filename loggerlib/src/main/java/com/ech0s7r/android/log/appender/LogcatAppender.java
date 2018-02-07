/*
 * Copyright 2011 ech0s7r
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ech0s7r.android.log.appender;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ech0s7r.android.log.Logger;
import com.ech0s7r.android.log.layout.LogLayout;

import static com.ech0s7r.android.log.LoggerConfigurator.APP_NAME;

/**
 * Logger Logcat appender
 *
 * @author ech0s7r
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
