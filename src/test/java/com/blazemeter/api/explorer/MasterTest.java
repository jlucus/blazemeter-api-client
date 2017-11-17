package com.blazemeter.api.explorer;

import com.blazemeter.api.logging.Logger;
import com.blazemeter.api.logging.LoggerTest;
import com.blazemeter.api.logging.UserNotifier;
import com.blazemeter.api.logging.UserNotifierTest;
import com.blazemeter.api.utils.BlazeMeterUtilsEmul;
import net.sf.json.JSONObject;

import static com.blazemeter.api.utils.BlazeMeterUtilsEmul.BZM_ADDRESS;
import static com.blazemeter.api.utils.BlazeMeterUtilsEmul.BZM_DATA_ADDRESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MasterTest {

    @org.junit.Test
    public void junitReport() throws Exception {
        Logger logger = new LoggerTest();
        UserNotifier notifier = new UserNotifierTest();

        BlazeMeterUtilsEmul emul = new BlazeMeterUtilsEmul(BZM_ADDRESS, BZM_DATA_ADDRESS, notifier, logger);
        String result = "junit";
        emul.addEmul(result);
        Master master = new Master(emul,"id","name");
        assertEquals("junit", master.junitReport());
        emul.clean();


    }

    @org.junit.Test
    public void cistatus() throws Exception {
        Logger logger = new LoggerTest();
        UserNotifier notifier = new UserNotifierTest();

        BlazeMeterUtilsEmul emul = new BlazeMeterUtilsEmul(BZM_ADDRESS, BZM_DATA_ADDRESS, notifier, logger);
        JSONObject result = new JSONObject();
        JSONObject status = new JSONObject();
        status.put("masterId","id");
        result.put("result",status);
        emul.addEmul(result.toString());
        Master master = new Master(emul,"id","name");
        JSONObject cs = master.cistatus();
        assertTrue(cs.has("masterId"));
        assertTrue(cs.getString("masterId").equals("id"));


    }
}
