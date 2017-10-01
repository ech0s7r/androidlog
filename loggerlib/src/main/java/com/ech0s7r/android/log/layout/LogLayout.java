package com.ech0s7r.android.log.layout;

import com.ech0s7r.android.log.LogMsg;

/**
 * @author marco.rocco
 */
public abstract class LogLayout {

    public abstract String format(LogMsg logMsg);

}
