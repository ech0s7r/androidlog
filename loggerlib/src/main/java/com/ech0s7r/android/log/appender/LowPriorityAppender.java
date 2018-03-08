package com.ech0s7r.android.log.appender;

import android.annotation.SuppressLint;

import com.ech0s7r.android.log.Logger;
import com.ech0s7r.android.log.layout.LogLayout;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ech0s7r
 */
public abstract class LowPriorityAppender extends LogAppender {

    private BlockingQueue<String> logQueue;

    private Thread writerThread = new Thread() {
        @SuppressLint("NoLoggedException")
        @Override
        public void run() {
            String line;
            while (!isInterrupted()) {
                try {
                    line = logQueue.take();
                    writeLog(line);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    };

    protected LowPriorityAppender(LogLayout layout) {
        super(layout);
        logQueue = new LinkedBlockingQueue<String>();
        writerThread.setDaemon(true);
        writerThread.setPriority(Thread.MIN_PRIORITY);
        writerThread.start();
    }

    @Override
    public final void writeLog(Logger.Level logLevel, String msg, Throwable tr) {
        logQueue.add(msg);
    }

    public abstract void writeLog(String msg);

}
