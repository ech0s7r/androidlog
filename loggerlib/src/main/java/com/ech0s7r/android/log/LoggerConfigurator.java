package com.ech0s7r.android.log;

import android.os.Environment;

import com.ech0s7r.android.log.appender.LogAppender;

import java.util.ArrayList;

/**
 * @author marco.rocco
 */

public class LoggerConfigurator {

    public static String APP_DIR_PATH;
    public static String APP_NAME;
    public static int APP_ID;
    public static String VERSION_NAME;
    public static String DEVICE_ID;
    public static String LOG_FILE_NAME_PREFIX;

    private static ArrayList<LogAppender> mLogAppenderList;

    static {
        mLogAppenderList = new ArrayList<LogAppender>();
    }

    private LoggerConfigurator() {

    }

    /**
     * Init the Logger library
     *
     * @param logLevel      log level
     * @param id            Application ID
     * @param appName       Application Name
     * @param versionName   Application Version
     * @param deviceId      Device ID
     * @param logNamePrefix Log name prefix used for the log file, if null appName will be used
     */
    public static void init(Logger.Level logLevel, int id, String appName, String versionName, String deviceId, String logNamePrefix) {
        APP_ID = id;
        APP_NAME = appName;
        DEVICE_ID = deviceId;
        VERSION_NAME = versionName;
        LOG_FILE_NAME_PREFIX = logNamePrefix;
        APP_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "logs" + "/" + APP_NAME;

        Logger.logLevel = logLevel;
    }

    /**
     * Add a custom appender to the Logger
     * {@link com.ech0s7r.android.log.appender.FileAppender}
     * {@link com.ech0s7r.android.log.appender.LogcatAppender}
     *
     * @param logAppender
     */
    public static void addAppender(LogAppender logAppender) {
        mLogAppenderList.add(logAppender);
    }

    static ArrayList<LogAppender> getLogAppenderList() {
        return mLogAppenderList;
    }

}
