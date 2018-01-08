package com.ech0s7r.android.log;


import com.ech0s7r.android.log.appender.LogcatAppender;
import com.ech0s7r.android.log.layout.LogcatLayout;
import com.ech0s7r.android.log.utils.LogTestUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


/**
 * @author ech0s7r
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class LoggerTest {

    private static final String TEST_V = "test verbose";
    private static final String TEST_D = "test debug";
    private static final String TEST_I = "test info";
    private static final String TEST_W = "test warning";
    private static final String TEST_E = "test error";
    private static final String TEST_A = "test assert";


    private static int expectedSize = 1;

    @Before
    public void setUp() {
        LoggerConfigurator.init(Logger.Level.VERBOSE, "TestLog", "1.0", "TestLog.log");
        LoggerConfigurator.addAppender(new LogcatAppender(new LogcatLayout()));
    }

    @Test
    public void testVerbose() {
        Logger.v(TEST_V);
        Assert.assertEquals(expectedSize++, LogTestUtils.logSize());
        Assert.assertTrue(LogTestUtils.hasMessage("1 LoggerTest.testVerbose (LoggerTest.java:41) " + TEST_V));
    }

    @Test
    public void testDebug() {
        Logger.v(TEST_D);
        Assert.assertEquals(expectedSize++, LogTestUtils.logSize());
        Assert.assertTrue(LogTestUtils.hasMessage("1 LoggerTest.testDebug (LoggerTest.java:48) " + TEST_D));
    }

    @Test
    public void testInfo() {
        Logger.v(TEST_I);
        Assert.assertEquals(expectedSize++, LogTestUtils.logSize());
        Assert.assertTrue(LogTestUtils.hasMessage("1 LoggerTest.testInfo (LoggerTest.java:55) " + TEST_I));
    }

    @Test
    public void testWarning() {
        Logger.v(TEST_W);
        Assert.assertEquals(expectedSize++, LogTestUtils.logSize());
        Assert.assertTrue(LogTestUtils.hasMessage("1 LoggerTest.testWarning (LoggerTest.java:62) " + TEST_W));
    }

    @Test
    public void testError() {
        Logger.v(TEST_E);
        Assert.assertEquals(expectedSize++, LogTestUtils.logSize());
        Assert.assertTrue(LogTestUtils.hasMessage("1 LoggerTest.testError (LoggerTest.java:69) " + TEST_E));
    }

    @Test
    public void testAssert() {
        Logger.v(TEST_A);
        Assert.assertEquals(expectedSize++, LogTestUtils.logSize());
        Assert.assertTrue(LogTestUtils.hasMessage("1 LoggerTest.testAssert (LoggerTest.java:76) " + TEST_A));
    }

    @Test
    public void testUncaughtExceptionhandler() {
        Logger.setUncaughtExceptionHandler();
        final String uncaughtExceptionString = "Expected!";
        Thread t = new Thread() {
            public void run() {
                throw new RuntimeException(uncaughtExceptionString);
            }
        };
        t.start();
        try {
            t.join();
        } catch (Exception e) {
        }
        LogTestUtils.waitLoggerLooper();
        Assert.assertTrue(LogTestUtils.getLastLogMsg().contains(uncaughtExceptionString));
    }

    @Test
    public void testLogcatLayout() {
        LogcatLayout layout = new LogcatLayout();
        String out = layout.format(new LogMsg(Logger.Level.WARN, TEST_V));
        Assert.assertTrue(out.startsWith("" + Thread.currentThread().getId()));
        Assert.assertTrue(out.contains(TEST_V));
        Assert.assertEquals(0, LogTestUtils.logSize());
        expectedSize++;
    }

}