package com.ech0s7r.android.example;

import android.app.Activity;
import android.os.Bundle;

import com.ech0s7r.android.log.Logger;
import com.ech0s7r.android.log.LoggerConfigurator;
import com.ech0s7r.android.log.appender.FileAppender;
import com.ech0s7r.android.log.appender.LogcatAppender;
import com.ech0s7r.android.log.layout.CsvLayout;
import com.ech0s7r.android.log.layout.LogcatLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 5; i++)
            Logger.w("Test before init");

        initLog();

        for (int i = 0; i < 5; i++)
            Logger.w("Test log");

        for (int i = 0; i < 5; i++)
            Logger.i("Test log");

        //System.out.println("ciao");

        try {
            throw new Exception("TestException");
        } catch (Exception e) {
            //e.printStackTrace();
            Logger.i("");
        } catch (Throwable t) {
            test1();
            test2();
            Logger.i("ok");
        }
        //Log.w("fail", "fail lint");
    }

    private void initLog() {
        LoggerConfigurator.init(
                Logger.Level.WARN,        /* Minimum level to log */
                123,                      /* Application ID */
                "AndroidLogApp",          /* Application name */
                BuildConfig.VERSION_NAME, /* Application version */
                "123456789",              /* Device ID */
                "AndroidLogApp.log");     /* File name prefix */
        LoggerConfigurator.addAppender(new LogcatAppender(new LogcatLayout()));
        LoggerConfigurator.addAppender(new FileAppender(getApplicationContext(), new CsvLayout()));
    }

    private void test1() {
    }

    private void test2() {
    }

}
