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

import com.blazemeter.api.explorer.Master;
import com.blazemeter.api.explorer.base.BZAObject;
import com.blazemeter.api.utils.BlazeMeterUtils;
import net.sf.json.JSONObject;

import java.io.IOException;

/*
Test which is ready to be started on server.
 */
public abstract class AbstractTest extends BZAObject implements ITest {

    protected Master master;
    protected String signature;
    protected String testType;

    public AbstractTest(BlazeMeterUtils utils, String id, String name, String testType) {
        super(utils, id, name);
        this.testType = testType;
    }

    protected JSONObject sendStartTest(String uri) throws IOException {
        JSONObject response = utils.execute(utils.createPost(uri, ""));
        return response.getJSONObject("result");
    }

    public abstract void fillFields(JSONObject result);

    public Master getMaster() {
        return master;
    }

    public String getSignature() {
        return signature;
    }

    public String getTestType() {
        return testType;
    }
}
