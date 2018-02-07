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
package com.ech0s7r.android.log.layout;

import com.ech0s7r.android.log.LogMsg;
import com.ech0s7r.android.log.utils.Utils;

/**
 * @author ech0s7r
 */
public class LogcatLayout extends LogLayout {


    @Override
    public String format(LogMsg msg) {
        String exceptionStr = (msg.throwable != null) ? "\n" + Utils.getStackTraceString(msg.throwable) : "";
        if (exceptionStr == null || (exceptionStr.length() == 0 && msg.throwable != null)) {
            exceptionStr = msg.throwable.getMessage() + " [" + msg.throwable.getCause() + "]";
        }
        String str = String.format("%d %s %s %s",
                msg.getThreadId(), // thread id
                msg.getMethodInfo(), // class.method
                msg.msg, // message
                exceptionStr // throwable message
        );
        return str;
    }
}
