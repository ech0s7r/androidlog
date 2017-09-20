package com.ech0s7r.android.log.appender;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ech0s7r.android.log.IWriterService;
import com.ech0s7r.android.log.Logger;
import com.ech0s7r.android.log.layout.LogLayout;
import com.ech0s7r.android.log.service.WriterService;

import java.util.LinkedList;
import java.util.Queue;

import static com.ech0s7r.android.log.LoggerConfigurator.APP_NAME;

/**
 * Log File Appender
 *
 * @author marco.rocco
 */
@SuppressWarnings("unused")
public class FileAppender extends LogAppender {

	/**
	 * Message Queue used to store message before binding the Service
	 */
	private Queue<String> mQueueLogString;

	private static final Object mutex = new Object();
	private WriterServiceConnection mWriterServiceConnection;
	private IWriterService mLoggerService;
	private Context mContext;
	private boolean mBinding;


	private class WriterServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			synchronized (mutex) {
				mLoggerService = IWriterService.Stub.asInterface(service);
				if (mLoggerService != null) {
					wipeQueue();
				}
				mBinding = false;
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			synchronized (mutex) {
				mLoggerService = null;
				mBinding = false;
			}
		}
	}

	public FileAppender(Context appContext, LogLayout layout) {
		super(layout);
		mContext = appContext;
		mQueueLogString = new LinkedList<String>();
		mWriterServiceConnection = new WriterServiceConnection();
	}

	/**
	 * Wipe log queue if filled during service loading
	 */
	private void wipeQueue() {
		String msg;
		synchronized (mQueueLogString) {
			while ((msg = mQueueLogString.poll()) != null) {
				writeLog(msg);
			}
		}
	}

	private void writeLog(String msg) {
		writeLog(null, msg, null);
	}

	@Override
	public void writeLog(Logger.Level logLevel, String msg, Throwable tr) {
		if (logLevel == Logger.Level.ASSERT) {
			writeFatal(msg);
		} else {
			writeNotFatal(msg);
		}
	}

	@SuppressLint({"NoLoggedException", "AndroidLogDetector"})
	private void writeNotFatal(String msg) {
		synchronized (mutex) {
			if (mLoggerService == null) {
				synchronized (mQueueLogString) {
					mQueueLogString.add(msg);
				}
				if (!mBinding) {
					mBinding = true;
					Intent intent = new Intent(mContext, WriterService.class);
					intent.setAction(IWriterService.class.getName());
					mContext.bindService(intent, mWriterServiceConnection, Context.BIND_AUTO_CREATE);
				}
			} else {
				try {
					mLoggerService.addInQueue(msg);
				} catch (RemoteException e) {
					Log.e(APP_NAME, "", e);
					synchronized (mQueueLogString) {
						mQueueLogString.add(msg);
					}
				}
			}
		}
	}

	@SuppressLint({"NoLoggedException", "AndroidLogDetector"})
	private void writeFatal(String msg) {
		synchronized (mutex) {
			if (mLoggerService != null) {
				try {
					mLoggerService.stop();
				} catch (Exception e) {
					Log.e(APP_NAME, "", e);
				}
			}
			WriterService.writeImmediately(msg);
		}
	}

}
