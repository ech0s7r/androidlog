package com.ech0s7r.android.log;

import android.annotation.SuppressLint;
import android.os.SystemClock;

import java.util.Calendar;

/**
 * @author marco.rocco
 */
public class LogMsg {

    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 5;


    private long mCurrentTimestamp;
    private long mCurrentTimestampElapsed;

    private int mProcessId;
    private long mThreadId;
    private StackTraceElement mTrace[];
    private String mTimeStamp;

    public Object msg;
    public Logger.Level level;
    public Throwable throwable;

    public static class StackElement {

        private String mClassName;
        private String mMethodName;
        private String mFileName;
        private int mLine;

        StackElement(String className, String methodName, String fileName, int line) {
            this.mClassName = className;
            this.mMethodName = methodName;
            this.mFileName = fileName;
            this.mLine = line;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(mClassName)
                    .append(".")
                    .append(mMethodName)
                    .append(" (")
                    .append(mFileName)
                    .append(":")
                    .append(mLine)
                    .append(")");
            return builder.toString();
        }

        public String getClassName() {
            return mClassName;
        }

        public String getMethodName() {
            return mMethodName;
        }

        public String getFileName() {
            return mFileName;
        }

        public int getLine() {
            return mLine;
        }

    }


    private LogMsg() {
        mProcessId = android.os.Process.myPid();
        mThreadId = Thread.currentThread().getId();
        mTrace = Thread.currentThread().getStackTrace();
        mTimeStamp = calculateTimeStamp();
    }

    LogMsg(Logger.Level level, Object msg, Throwable throwable) {
        this();
        if (msg instanceof Throwable) {
            this.msg = "";
            this.throwable = (Throwable) msg;
        } else {
            this.msg = (msg != null) ? msg : "";
            this.throwable = throwable;
        }
        this.level = level;
    }

    @SuppressLint({"NoLoggedException"})
    LogMsg(Logger.Level level, String format, Object... args) {
        this(level, null);
        try {
            if (format == null) {
                setMsgFromArgs(args);
            } else if (!format.contains("%")) {
                this.msg += "" + format + " ";
                setMsgFromArgs(args);
            } else {
                this.msg = String.format(format, args);
            }
        } catch (Throwable tr) {
            setMsgFromArgs(args);
        }
    }

    LogMsg(Logger.Level level, Object msg) {
        this(level, msg, null);
    }

    private void setMsgFromArgs(Object... args) {
        // format not valid
        if (args != null) {
            for (Object obj : args) {
                if (obj != null) {
                    this.msg += obj.toString() + " ";
                }
            }
        }
    }

    private int getStackOffset(StackTraceElement[] trace) {
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(Logger.class.getName()) && !name.equals(LogMsg.class.getName())) {
                return i;
            }
        }
        return -1;
    }

    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private String calculateTimeStamp() {
        long time;
        final Calendar cal = Calendar.getInstance();
        if (mCurrentTimestamp != 0) {
            long elapsed = SystemClock.elapsedRealtime()
                    - mCurrentTimestampElapsed;
            time = elapsed + mCurrentTimestamp;
            cal.setTimeInMillis(time);
        } else {
            cal.setTimeInMillis(System.currentTimeMillis());
        }
        return String.format(
                "%04d-%02d-%02d %02d:%02d:%02d.%03d",
                cal.get(Calendar.YEAR),
                (cal.get(Calendar.MONTH) + 1),
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                cal.get(Calendar.MILLISECOND));
    }

    public int getProcessId() {
        return mProcessId;
    }

    public long getThreadId() {
        return mThreadId;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public StackElement getMethodInfo() {
        int index = getStackOffset(mTrace);
        return new StackElement(getSimpleClassName(mTrace[index].getClassName()),
                mTrace[index].getMethodName(),
                mTrace[index].getFileName(),
                mTrace[index].getLineNumber());
    }

}