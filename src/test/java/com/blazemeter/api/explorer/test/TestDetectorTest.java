package com.blazemeter.api.explorer.test;

import com.blazemeter.api.logging.LoggerTest;
import com.blazemeter.api.logging.UserNotifier;
import com.blazemeter.api.logging.UserNotifierTest;
import com.blazemeter.api.utils.BlazeMeterUtilsEmul;
import net.sf.json.JSONObject;
import org.junit.Test;

import static com.blazemeter.api.utils.BlazeMeterUtilsEmul.BZM_ADDRESS;
import static com.blazemeter.api.utils.BlazeMeterUtilsEmul.BZM_DATA_ADDRESS;
import static org.junit.Assert.*;

public class TestDetectorTest {

    @Test
    public void testDetectTestReturnSingle() throws Exception {
        TestDetector detector = new TestDetector();
        assertNotNull(detector);
        LoggerTest logger = new LoggerTest();
        UserNotifier notifier = new UserNotifierTest();
        BlazeMeterUtilsEmul emul = new BlazeMeterUtilsEmul(BZM_ADDRESS, BZM_DATA_ADDRESS, notifier, logger);

        emul.addEmul(SingleTestTest.generateResponseGetSingleTest());

        AbstractTest abstractTest = TestDetector.detectTest(emul, "xxxx");

        assertTrue(abstractTest instanceof SingleTest);
        assertEquals("testId", abstractTest.getId());
        assertEquals("Single_testName", abstractTest.getName());
        assertEquals("http", abstractTest.getTestType());
        assertEquals(1, emul.getRequests().size());
        String logs = logger.getLogs().toString();
        assertEquals(logs, 267, logs.length());
        assertTrue(logs, logs.contains("Attempt to detect Single test type with id=xxx"));
    }

    @Test
    public void testDetectTestReturnMulti() throws Exception {
        LoggerTest logger = new LoggerTest();
        UserNotifier notifier = new UserNotifierTest();
        BlazeMeterUtilsEmul emul = new BlazeMeterUtilsEmul(BZM_ADDRESS, BZM_DATA_ADDRESS, notifier, logger);

        emul.addEmul(generateResponseTestNotFound());
        emul.addEmul(MultiTestTest.generateResponseGetMultiTest());

        AbstractTest abstractTest = TestDetector.detectTest(emul, "xxxx");

        assertTrue(abstractTest instanceof MultiTest);
        assertEquals("testId", abstractTest.getId());
        assertEquals("Multi_testName", abstractTest.getName());
        assertEquals("multi", abstractTest.getTestType());
        assertEquals(2, emul.getRequests().size());
        String logs = logger.getLogs().toString();
        assertEquals(logs, 621, logs.length());
        assertTrue(logs, logs.contains("Single test with id=xxxx not found"));
        assertTrue(logs, logs.contains("Attempt to detect Multi test type with id=xxx"));
    }

    @Test
    public void testDetectTestNotFound() throws Exception {
        LoggerTest logger = new LoggerTest();
        UserNotifier notifier = new UserNotifierTest();
        BlazeMeterUtilsEmul emul = new BlazeMeterUtilsEmul(BZM_ADDRESS, BZM_DATA_ADDRESS, notifier, logger);

        emul.addEmul(generateResponseTestNotFound());
        emul.addEmul(generateResponseTestNotFound());

        AbstractTest abstractTest = TestDetector.detectTest(emul, "xxxx");
        assertNull(abstractTest);
        assertEquals(2, emul.getRequests().size());
        String logs = logger.getLogs().toString();
        assertEquals(logs, 892, logs.length());
        assertTrue(logs, logs.contains("Fail for detect Multi test type id=xxxx. Reason is: Receive response with the following error message: Not Found: Test not found"));
    }

    @Test
    public void testDetectTestFailedGetSingleTest() throws Exception {
        LoggerTest logger = new LoggerTest();
        UserNotifier notifier = new UserNotifierTest();
        BlazeMeterUtilsEmul emul = new BlazeMeterUtilsEmul(BZM_ADDRESS, BZM_DATA_ADDRESS, notifier, logger);

        emul.addEmul(generateResponseUnauthorized());

        AbstractTest abstractTest = TestDetector.detectTest(emul, "xxxx");
        assertNull(abstractTest);
        assertEquals(1, emul.getRequests().size());
        String logs = logger.getLogs().toString();
        assertEquals(logs, 479, logs.length());
        assertTrue(logs, logs.contains("Fail for detect Single test type id=xxxx. Reason is: Receive response with the following error message: Unauthorized"));
    }

    public static String generateResponseTestNotFound() {
        JSONObject error = new JSONObject();
        error.put("code", 404);
        error.put("message", "Not Found: Test not found");

        JSONObject response = new JSONObject();
        response.put("error", error);
        response.put("result", null);
        return response.toString();
    }

    public static String generateResponseUnauthorized() {
        JSONObject error = new JSONObject();
        error.put("code", 404);
        error.put("message", "Unauthorized");

        JSONObject response = new JSONObject();
        response.put("error", error);
        response.put("result", null);
        return response.toString();
    }
}