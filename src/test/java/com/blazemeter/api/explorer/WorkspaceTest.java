/**
 * Copyright 2017 BlazeMeter Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blazemeter.api.explorer;

import com.blazemeter.api.explorer.test.MultiTest;
import com.blazemeter.api.explorer.test.SingleTest;
import com.blazemeter.api.logging.LoggerTest;
import com.blazemeter.api.logging.UserNotifier;
import com.blazemeter.api.logging.UserNotifierTest;
import com.blazemeter.api.utils.BlazeMeterUtilsEmul;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

import static com.blazemeter.api.utils.BlazeMeterUtilsEmul.BZM_ADDRESS;
import static com.blazemeter.api.utils.BlazeMeterUtilsEmul.BZM_DATA_ADDRESS;
import static org.junit.Assert.*;

public class WorkspaceTest {

    @org.junit.Test
    public void testFlow() throws Exception {
        LoggerTest logger = new LoggerTest();
        UserNotifier notifier = new UserNotifierTest();

        BlazeMeterUtilsEmul emul = new BlazeMeterUtilsEmul(BZM_ADDRESS, BZM_DATA_ADDRESS, notifier, logger);

        JSONObject result = new JSONObject();
        result.put("id", "999");
        result.put("name", "NEW_PROJECT");
        JSONObject response = new JSONObject();
        response.put("result", result);

        Workspace workspace = new Workspace(emul, "888", "workspace_name");
        emul.addEmul(response.toString());
        Project project = workspace.createProject("NEW_PROJECT");
        assertEquals("999", project.getId());
        assertEquals("NEW_PROJECT", project.getName());
        assertEquals("Request{method=POST, url=http://a.blazemeter.com/api/v4/projects, tag=null}", emul.getRequests().get(0));
        assertEquals("Create project with name=NEW_PROJECT\r\n" +
                        "Simulating request: Request{method=POST, url=http://a.blazemeter.com/api/v4/projects, tag=null}\r\n" +
                        "Response: {\"result\":{\"id\":\"999\",\"name\":\"NEW_PROJECT\"}}\r\n",
                logger.getLogs().toString());

        emul.clean();
        logger.reset();
        response.clear();
        JSONArray results = new JSONArray();
        results.add(result);
        results.add(result);
        response.put("result", results);
        emul.addEmul(response.toString());

        List<Project> projects = workspace.getProjects();
        assertEquals(2, projects.size());
        for (Project p :projects) {
            assertEquals("999", p.getId());
            assertEquals("NEW_PROJECT", p.getName());
        }
        assertEquals("Request{method=GET, url=http://a.blazemeter.com/api/v4/projects?workspaceId=888&limit=99999, tag=null}", emul.getRequests().get(0));
        assertEquals("Get list of projects for workspace id=888\r\n" +
                        "Simulating request: Request{method=GET, url=http://a.blazemeter.com/api/v4/projects?workspaceId=888&limit=99999, tag=null}\r\n" +
                        "Response: {\"result\":[{\"id\":\"999\",\"name\":\"NEW_PROJECT\"},{\"id\":\"999\",\"name\":\"NEW_PROJECT\"}]}\r\n",
                logger.getLogs().toString());

        emul.clean();
        logger.reset();
        emul.clean();
        logger.reset();
        response.clear();
        JSONArray res = new JSONArray();
        res.add(result);
        res.add(result);
        response.put("result", res);
        emul.addEmul(response.toString());
        List<SingleTest> tests = workspace.getSingleTests();
        assertEquals(2, tests.size());
        for (SingleTest t :tests) {
            assertEquals("999", t.getId());
            assertEquals("NEW_PROJECT", t.getName());
        }
        assertEquals("Request{method=GET, url=http://a.blazemeter.com/api/v4/tests?workspaceId=888, tag=null}", emul.getRequests().get(0));
        assertEquals("Get list of single tests for workspace id=888\r\n" +
                        "Simulating request: Request{method=GET, url=http://a.blazemeter.com/api/v4/tests?workspaceId=888, tag=null}\r\n" +
                        "Response: {\"result\":[{\"id\":\"999\",\"name\":\"NEW_PROJECT\"},{\"id\":\"999\",\"name\":\"NEW_PROJECT\"}]}\r\n",
                logger.getLogs().toString());

        emul.clean();
        logger.reset();
        emul.addEmul(response.toString());
        List<MultiTest> multiTests = workspace.getMultiTests();
        assertEquals(2, multiTests.size());
        for (MultiTest t :multiTests) {
            assertEquals("999", t.getId());
            assertEquals("NEW_PROJECT", t.getName());
        }
        assertEquals("Request{method=GET, url=http://a.blazemeter.com/api/v4/tests?workspaceId=888, tag=null}", emul.getRequests().get(0));
        assertEquals("Get list of multi tests for workspace id=888\r\n" +
                        "Simulating request: Request{method=GET, url=http://a.blazemeter.com/api/v4/tests?workspaceId=888, tag=null}\r\n" +
                        "Response: {\"result\":[{\"id\":\"999\",\"name\":\"NEW_PROJECT\"},{\"id\":\"999\",\"name\":\"NEW_PROJECT\"}]}\r\n",
                logger.getLogs().toString());
    }

    @org.junit.Test
    public void testFromJSON() throws Exception {
        LoggerTest logger = new LoggerTest();
        UserNotifier notifier = new UserNotifierTest();

        BlazeMeterUtilsEmul emul = new BlazeMeterUtilsEmul(BZM_ADDRESS, BZM_DATA_ADDRESS, notifier, logger);
        JSONObject object = new JSONObject();
        object.put("id", "workspaceId");
        object.put("name", "workspaceName");
        Workspace workspace = Workspace.fromJSON(emul, object);
        assertEquals("workspaceId", workspace.getId());
        assertEquals("workspaceName", workspace.getName());
    }

}