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

package com.blazemeter.api.explorer.test;

import com.blazemeter.api.utils.BlazeMeterUtils;
import net.sf.json.JSONObject;

import java.io.IOException;

public class SingleTest extends AbstractTest implements ITest {

    public SingleTest(BlazeMeterUtils utils, String id, String name) {
        super(utils, id, name);
    }

    @Override
    public void start() throws IOException {
        JSONObject result = sendStartTest(utils.getAddress() + String.format("/api/v4/tests/%s/start", encode(getId())));
        fillFields(result);
    }

    @Override
    public void startExternal() throws IOException {
        JSONObject result = sendStartTest(utils.getAddress() + String.format("/api/v4/tests/%s/start-external", encode(getId())));
        fillFields(result);
    }

    public static SingleTest fromJSON(BlazeMeterUtils utils, JSONObject obj) {
        return new SingleTest(utils, obj.getString("id"), obj.getString("name"));
    }
}
