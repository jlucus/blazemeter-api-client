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

import com.blazemeter.api.explorer.base.BZAObject;
import com.blazemeter.api.explorer.test.MultiTest;
import com.blazemeter.api.explorer.test.SingleTest;
import com.blazemeter.api.utils.BlazeMeterUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Project extends BZAObject {

    public static final String DEFAULT_PROJECT = "Default project";

    public Project(BlazeMeterUtils utils, String id, String name) {
        super(utils, id, name);
    }

    /**
     * Create Test in current Project
     * @param name - title of the new Test
     */
    public SingleTest createSingleTest(String name) throws IOException {
        String uri = utils.getAddress() + "/api/v4/tests";
        JSONObject data = new JSONObject();
        data.put("projectId", Long.parseLong(getId()));
        JSONObject configuration = new JSONObject();
        configuration.put("type", "external");
        data.put("configuration", configuration);
        data.put("name", name);
        JSONObject response = utils.execute(utils.createPost(uri, data.toString()));
        return SingleTest.fromJSON(utils, response.getJSONObject("result"));
    }

    /**
     * @return list of Tests in current Project
     */
    public List<SingleTest> getSingleTests() throws IOException {
        String uri = utils.getAddress() + "/api/v4/tests?projectId=" + encode(getId());
        JSONObject response = utils.execute(utils.createGet(uri));
        return extractSingleTests(response.getJSONArray("result"));
    }

    /**
     * @return list of Multi-Tests in current Project
     */
    public List<MultiTest> getMultiTests() throws IOException {
        String uri = utils.getAddress() + "/api/v4/tests?projectId=" + encode(getId());
        JSONObject response = utils.execute(utils.createGet(uri));
        return extractMultiTests(response.getJSONArray("result"));
    }


    private List<SingleTest> extractSingleTests(JSONArray result) {
        List<SingleTest> accounts = new ArrayList<>();

        for (Object obj : result) {
            accounts.add(SingleTest.fromJSON(utils, (JSONObject) obj));
        }

        return accounts;
    }

    private List<MultiTest> extractMultiTests(JSONArray result) {
        List<MultiTest> accounts = new ArrayList<>();

        for (Object obj : result) {
            accounts.add(MultiTest.fromJSON(utils, (JSONObject) obj));
        }

        return accounts;
    }

    public static Project fromJSON(BlazeMeterUtils utils, JSONObject obj) {
        return new Project(utils, obj.getString("id"), obj.getString("name"));
    }
}
