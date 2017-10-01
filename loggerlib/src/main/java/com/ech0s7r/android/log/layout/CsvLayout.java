package com.ech0s7r.android.log.layout;

import android.os.Build;
import android.util.Log;

import com.ech0s7r.android.log.LogMsg;

import java.util.Locale;
import java.util.regex.Matcher;

import static com.ech0s7r.android.log.LoggerConfigurator.APP_ID;
import static com.ech0s7r.android.log.LoggerConfigurator.DEVICE_ID;
import static com.ech0s7r.android.log.LoggerConfigurator.VERSION_NAME;

/**
 * @author marco.rocco
 */
@SuppressWarnings("unused")
public class CsvLayout extends LogLayout {

    private static final String QUOTE_REPLACEMENT = Matcher.quoteReplacement("\\\"");

    @Override
    public String format(LogMsg msg) {
        String str = String.format(Locale.ENGLISH, "%s,%s,%s,%d,%s,%s,%s,%d,%d,%d,%s,%s,%s,%d,\"%s\",\"%s\"",
                /* 1 date*/msg.getTimeStamp(),
				/* 2 log level*/msg.level,
				/* 3 Version*/VERSION_NAME,
				/* 4 Company id*/APP_ID,
				/* 5 Device code*/DEVICE_ID,
				/* 6 Device manufacturer*/Build.MANUFACTURER,
				/* 7 Device board*/Build.BOARD,
				/* 8 SDK Version*/Build.VERSION.SDK_INT,

				/* 9 Process id*/msg.getProcessId(),
				/*10 Thread id*/msg.getThreadId(),

				/*11 Class name*/msg.getMethodInfo().getClassName(),
				/*12 Method name*/msg.getMethodInfo().getMethodName(),
				/*13 File name*/msg.getMethodInfo().getFileName(),
				/*14 Line*/msg.getMethodInfo().getLine(),

				/*15 Description*/escapeQuote(normalizeMsg(msg.msg)),
				/*16 Stacktrace*/escapeQuote(normalizeMsg(msg.throwable))
        );
        return str;
    }

    private String escapeQuote(String text) {
        if (text == null) {
            return "";
        }
        String escaped = text.replace("\\", "\\\\");
        escaped = escaped.replace("\"", "\\\"");
        return escaped;
    }

    private String normalizeMsg(Object msg) {
        if (msg instanceof Throwable) {
            return stackOneLine((Throwable) msg);
        }
        return oneLine(msg != null ? msg.toString() : "", "");
    }


    private String stackOneLine(Throwable t) {
        return oneLine(Log.getStackTraceString(t), ";");
    }

    private String oneLine(String msg, String separator) {
        if (msg == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        String tkn[] = msg.split("\n");
        for (String s : tkn) {
            builder.append(s.trim());
            builder.append(separator);
        }
        return builder.toString();
    }

}
