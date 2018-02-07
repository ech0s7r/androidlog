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

import com.ech0s7r.android.log.Logger;
import com.ech0s7r.android.log.layout.LogLayout;


/**
 * Abstract LogAppender
 *
 * @author ech0s7r
 */

public abstract class LogAppender {

    private LogLayout mLogLayout;

    protected LogAppender(LogLayout layout) {
        mLogLayout = layout;
    }

    public LogLayout getLogLayout() {
        return mLogLayout;
    }


    public abstract void writeLog(Logger.Level logLevel, String msg, Throwable tr);
}
