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
package com.ech0s7r.android.log.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.ech0s7r.android.log.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author ech0s7r
 */

public class Utils {

    public static void logDisplayInfo(Activity cx) {
        DisplayMetrics metrics = new DisplayMetrics();
        cx.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        String density;
        switch (metrics.densityDpi) {
            // case DisplayMetrics.DENSITY_400:
            // density = "DENSITY_400";
            // break;
            case DisplayMetrics.DENSITY_HIGH:
                density = "DENSITY_HIGH";
                break;
            case DisplayMetrics.DENSITY_LOW:
                density = "DENSITY_LOW";
                break;
            case DisplayMetrics.DENSITY_MEDIUM: // DEFAULT
                density = "DENSITY_MEDIUM (DEFAULT)";
                break;
            // case DisplayMetrics.DENSITY_TV:
            // density = "DENSITY_TV";
            // break;
            case DisplayMetrics.DENSITY_XHIGH:
                density = "DENSITY_XHIGH";
                break;
            // case DisplayMetrics.DENSITY_XXHIGH:
            // density = "DENSITY_XXHIGH";
            // break;
            // case DisplayMetrics.DENSITY_XXXHIGH:
            // density = "DENSITY_XXXHIGH";
            // break;
            default:
                density = "DENSITY_" + metrics.densityDpi;
                break;
        }
        String log = " [pysical dpi=" + metrics.densityDpi
                + " (dot4inch), height=" + metrics.heightPixels + "px, width="
                + metrics.widthPixels + "px, logical_density="
                + metrics.density + ", xdpiX=" + metrics.xdpi
                + "(px4inch), dpiY=" + metrics.ydpi + "(px4inch)] ";

        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;
        double diagonalInches = Math.sqrt((widthInches * widthInches)
                + (heightInches * heightInches));

        float densityDpi = cx.getResources().getDisplayMetrics().density;
        float dpHeight = metrics.heightPixels / densityDpi;
        float dpWidth = metrics.widthPixels / densityDpi;

        Logger.w("Screen spec: " + log + getScreenSize(cx) + "/" + density
                + " inches: " + diagonalInches + " dpWidth: " + dpWidth + " dpHeight: " + dpHeight);
    }

    private static String getScreenSize(Context cx) {
        Configuration config = cx.getResources().getConfiguration();
        int layout = config.screenLayout;
        switch (layout & Configuration.SCREENLAYOUT_SIZE_MASK) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return "SCREENLAYOUT_SIZE_SMALL";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return "SCREENLAYOUT_SIZE_NORMAL";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return "SCREENLAYOUT_SIZE_LARGE";
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return "SCREENLAYOUT_SIZE_XLARGE";
            case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
                return "SCREENLAYOUT_SIZE_UNDEFINED";
        }
        return "SCREENLAYOUT_SIZE_UNDEFINED";
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    @SuppressLint("PrintStackTraceDetector")
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new FastPrintWriter(sw, false, 256);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
