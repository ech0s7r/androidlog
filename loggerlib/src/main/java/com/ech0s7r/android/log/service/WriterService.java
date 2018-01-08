package com.ech0s7r.android.log.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ech0s7r.android.log.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.ech0s7r.android.log.LoggerConfigurator.APP_DIR_PATH;
import static com.ech0s7r.android.log.LoggerConfigurator.APP_NAME;
import static com.ech0s7r.android.log.LoggerConfigurator.LOG_FILE_NAME_PREFIX;


/**
 * @author marco.rocco
 */
public class WriterService extends Service {

    public static final int WRITE_TIMEOUT_MS = 100;

    private BlockingQueue<String> mQueueStringLog;
    private WriterThread mWriterThread;

    private WriterServiceImpl mLoggerServiceImpl = new WriterServiceImpl();

    private static File mCurrentFile;
    private static BufferedWriter mWriter;
    private static long line = 0;


    private class WriterThread extends Thread {
        boolean wrote = false;
        String txt = null;

        @Override
        @SuppressLint({"NoLoggedException", "AndroidLogDetector"})
        public void run() {
            try {
                mWriter = createWriter();
                while (!isInterrupted()) {
                    try {
                        txt = mQueueStringLog.take();
                        write(txt);
                        Thread.sleep(WRITE_TIMEOUT_MS);
                    } catch (Exception e) {
                        Log.e(APP_NAME, "", e);
                        try {
                            closeWriter(mWriter);
                            mWriter = createWriter();
                            if (!wrote && txt != null) {
                                write(txt);
                            }
                        } catch (Exception ee) {
                            Log.e(APP_NAME, "", ee);
                            break;
                        }
                        if (mWriter == null) {
                            Log.e(APP_NAME, "Writer is null!");
                            break;
                        }
                    }
                }
            } catch (Throwable e) {
                Log.e(APP_NAME, "", e);
                return;
            } finally {
                closeWriter(mWriter);
            }
        }

    }

    private static boolean checkFileAccess() {
        if (mCurrentFile != null && mCurrentFile.exists() && mCurrentFile.canWrite()) {
            return true;
        }
        return false;
    }

    @SuppressLint({"AndroidLogDetector"})
    private static void createLogDir() {
        String path = APP_DIR_PATH;
        if (!(new File(path).exists())) {
            boolean created = new File(path).mkdirs();
            if (!created) {
                Log.e(APP_NAME, "Impossible to create log file, path=" + path);
            }
        }
    }

    private static String nextLogFileName() {
        String fileName = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        String date = sdf.format(new Date());
        File dir = new File(APP_DIR_PATH);
        if (dir.canRead() && dir.isDirectory()) {
            for (String file : dir.list()) {
                if (file.contains(date)) {
                    // found, append to it
                    fileName = file;
                    break;
                }
            }
        }
        if (fileName == null) {
            // not found, create it
            if (LOG_FILE_NAME_PREFIX != null) {
                fileName = LOG_FILE_NAME_PREFIX + date;
            } else {
                fileName = APP_NAME + "_" + date + ".log";
            }
        }
        return fileName;
    }

    private static BufferedWriter createWriter()
            throws IOException {
        String logFilePath = APP_DIR_PATH + "/"
                + nextLogFileName();
        mCurrentFile = new File(logFilePath);
        synchronized (WriterService.class) {
            mWriter = new BufferedWriter(new FileWriter(new File(logFilePath),
                    true));
            return mWriter;
        }
    }

    private static void write(String txt) throws IOException {
        synchronized (WriterService.class) {
            if (!checkFileAccess()) {
                closeWriter(mWriter);
                mWriter = createWriter();
            }
            mWriter.write(txt);
            mWriter.newLine();
            mWriter.flush();
            line++;
            if (Logger.MAX_FILE_LINE != 0 && line > Logger.MAX_FILE_LINE) {
                line = 0;
                closeWriter(mWriter);
                mWriter = createWriter();
            }
        }
    }

    @SuppressLint({"NoLoggedException", "AndroidLogDetector"})
    private static void closeWriter(BufferedWriter writer) {
        if (writer != null) {
            try {
                synchronized (WriterService.class) {
                    writer.close();
                }
            } catch (Exception e) {
                Log.e(APP_NAME, "", e);
            }
        }
    }

    @SuppressLint({"NoLoggedException", "AndroidLogDetector"})
    public static void writeImmediately(String msg) {
        BufferedWriter writer = null;
        synchronized (WriterService.class) {
            try {
                WriterService.closeWriter(writer);
                writer = WriterService.createWriter();
                WriterService.write(msg);
            } catch (IOException e) {
                Log.e(APP_NAME, "", e);
            } finally {
                WriterService.closeWriter(writer);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.d(APP_NAME, "LogService onCreate()");

        createLogDir();

        mQueueStringLog = new LinkedBlockingQueue<String>();
        mWriterThread = new WriterThread();
        mWriterThread.setPriority(Thread.MIN_PRIORITY);
        mWriterThread.setDaemon(true);
        mWriterThread.start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        closeWriter(mWriter);
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // run until explicitly stopped.
    }


    private class WriterServiceImpl extends
            com.ech0s7r.android.log.IWriterService.Stub {

        @Override
        public void addInQueue(String msg) throws RemoteException {
            mQueueStringLog.add(msg);
        }

        @Override
        @SuppressLint({"NoLoggedException", "AndroidLogDetector"})
        public void stop() throws RemoteException {
            closeWriter(mWriter);
            try {
                stopSelf();
            } catch (Throwable t) {
                Log.e(APP_NAME, "stopSelf()", t);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mLoggerServiceImpl;
    }


}
