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
package com.ech0s7r.android.log;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.ech0s7r.android.log.appender.LogAppender;
import com.ech0s7r.android.log.layout.LogLayout;


/**
 * @author ech0s7r
 */
@SuppressWarnings("unused")
public class Logger implements Cloneable {

    public enum Level {

        VERBOSE(2),

        DEBUG(3),

        INFO(4),

        WARN(5),

        ERROR(6),

        ASSERT(7);

        int priority;

        Level(int priority) {
            this.priority = priority;
        }

    }

    public static class ExceptionHandler implements
            Thread.UncaughtExceptionHandler {

        private Thread.UncaughtExceptionHandler defaultUEH;

        public ExceptionHandler() {
            defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Logger.e("uncaught exception !!!");
            Logger.e(ex);

            if (defaultUEH != null) {
                defaultUEH.uncaughtException(thread, ex);
            }
        }
    }

    private class HandlerCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            LogMsg logMsg = (LogMsg) msg.obj;
            writeLog(logMsg);
            return true;
        }
    }

    private static class LoggerInstance {
        private static Logger myInstance = new Logger();
    }

    /*package*/static Level logLevel = Level.INFO; // Default

    public static final long MAX_FILE_LINE = 0; // 0 to disable it

    private Handler mHandler;
    private HandlerThread mHandlerThread;

    private static ExceptionHandler mExceptionHandler = new ExceptionHandler();

    private static boolean sUseHandlerThread;


    @SuppressLint({"AndroidLogDetector", "NoLoggedException"})
    private Logger() {
        createThreadHandler();
    }

    @SuppressLint({"AndroidLogDetector", "NoLoggedException"})
    private void createThreadHandler() {
        HandlerCallback mHandlerCallback = new HandlerCallback();
        mHandlerThread = new HandlerThread(Logger.class.getSimpleName());
        mHandlerThread.start();
        try {
            sUseHandlerThread = true;
            mHandler = new Handler(mHandlerThread.getLooper(), mHandlerCallback);
        } catch (Exception e) {
            sUseHandlerThread = false;
        }
    }

    private static Logger getInstance() {
        return LoggerInstance.myInstance;
    }

    @SuppressLint({"AndroidLogDetector"})
    private void sendWrite(LogMsg msg) {
        Message handlerMsg;
        if (sUseHandlerThread && msg.level != Level.ASSERT) {
            if (!mHandlerThread.isAlive()) {
                createThreadHandler();
            }
            if (mHandler != null && (handlerMsg = mHandler.obtainMessage(0, msg)) != null) {
                handlerMsg.sendToTarget();
            } else {
                writeLog(msg);
            }
        } else {
            writeLog(msg);
        }
    }

    private void writeLog(LogMsg msg) {
        if (isLoggable(msg)) {
            for (LogAppender appender : LoggerConfigurator.getLogAppenderList()) {
                LogLayout layout = appender.getLogLayout();
                String str = layout.format(msg);
                appender.writeLog(msg.level, str, msg.throwable);
            }
        }
    }

    private boolean isLoggable(LogMsg msg) {
        return msg.level.priority >= logLevel.priority;
    }

    public static void setUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(mExceptionHandler);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param msg Message to write
     */
    public static void v(Object msg) {
        LogMsg m = new LogMsg(Level.VERBOSE, msg);
        getInstance().sendWrite(m);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param format Message string format
     * @param args   format args
     */
    public static void v(String format, Object... args) {
        LogMsg m = new LogMsg(Level.VERBOSE, format, args);
        getInstance().sendWrite(m);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param message Message to write
     * @param t       Throwable obj
     */
    public static void v(String message, Throwable t) {
        LogMsg m = new LogMsg(Level.VERBOSE, message, t);
        getInstance().sendWrite(m);
    }

    /**
     * Send a DEBUG log message.
     *
     * @param msg Message to write
     */
    public static void d(Object msg) {
        LogMsg m = new LogMsg(Level.DEBUG, msg);
        getInstance().sendWrite(m);
    }

    /**
     * Send a DEBUG log message.
     *
     * @param format Message string format
     * @param args   format args
     */
    public static void d(String format, Object... args) {
        LogMsg m = new LogMsg(Level.DEBUG, format, args);
        getInstance().sendWrite(m);
    }

    /**
     * Send a DEBUG log message.
     *
     * @param message Message to write
     * @param t       Throwable obj
     */
    public static void d(String message, Throwable t) {
        LogMsg m = new LogMsg(Level.DEBUG, message, t);
        getInstance().sendWrite(m);
    }

    /**
     * Send a INFO log message.
     *
     * @param msg Message to write
     */
    public static void i(Object msg) {
        LogMsg m = new LogMsg(Level.INFO, msg);
        getInstance().sendWrite(m);
    }

    /**
     * Send a INFO log message.
     *
     * @param format Message string format
     * @param args   format args
     */
    public static void i(String format, Object... args) {
        LogMsg m = new LogMsg(Level.INFO, format, args);
        getInstance().sendWrite(m);
    }

    /**
     * Send a INFO log message.
     *
     * @param message Message to write
     * @param t       Throwable obj
     */
    public static void i(String message, Throwable t) {
        LogMsg m = new LogMsg(Level.INFO, message, t);
        getInstance().sendWrite(m);
    }

    /**
     * Send a WARNING log message.
     *
     * @param msg Message to write
     */
    public static void w(Object msg) {
        LogMsg m = new LogMsg(Level.WARN, msg);
        getInstance().sendWrite(m);
    }

    /**
     * Send a WARNING log message.
     *
     * @param format Message string format
     * @param args   format args
     */
    public static void w(String format, Object... args) {
        LogMsg m = new LogMsg(Level.WARN, format, args);
        getInstance().sendWrite(m);
    }

    /**
     * Send a WARNING log message.
     *
     * @param message Message to write
     * @param t       Throwable obj
     */
    public static void w(String message, Throwable t) {
        LogMsg m = new LogMsg(Level.WARN, message, t);
        getInstance().sendWrite(m);
    }

    /**
     * Send a ERROR log message.
     *
     * @param msg Message to write
     */
    public static void e(Object msg) {
        LogMsg m = new LogMsg(Level.ERROR, msg);
        getInstance().sendWrite(m);
    }

    /**
     * Send a ERROR log message.
     *
     * @param msg Message to write
     * @param t   Throwable obj
     */
    public static void e(Object msg, Throwable t) {
        LogMsg m = new LogMsg(Level.ERROR, msg, t);
        getInstance().sendWrite(m);
    }

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * <p>
     * To use only to report app crash.
     *
     * @param msg Message to write
     * @param t   Throwable obj
     */
    public static void wtf(Object msg, Throwable t) {
        //Log.e("wtf", "WTF called");
        LogMsg m = new LogMsg(Level.ASSERT, msg, t);
        getInstance().sendWrite(m);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
