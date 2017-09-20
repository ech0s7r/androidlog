package com.ech0s7r.android.log.layout;

import android.util.Log;

import com.ech0s7r.android.log.LogMsg;

/**
 * @author marco.rocco
 */
public class LogcatLayout extends LogLayout {


	@Override
	public String format(LogMsg msg) {
		String str = String.format("%d %s %s %s",
				msg.getThreadId(), // thread id
				msg.getMethodInfo(), // class.method
				msg.msg, // message
				(msg.throwable != null) ? "\n" + Log.getStackTraceString(msg.throwable) : ""
		);
		return str;
	}
}
