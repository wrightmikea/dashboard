/**
 Copyright (c) 2013 Michael A. Wright.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.scalablelabs.dashboard.app.representations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scalablelabs.dashboard.core.entity.App;

import java.net.URI;

public class AppRepresentation {
    @JsonIgnore
    final App _app;

    public AppRepresentation(App app, URI uri) {
        _app = app;
    }

    @JsonProperty("id_pk")
    public int getId() {
        return _app.getKey();
    }

    @JsonProperty("created")
    public long getCreated() {
        return _app.getCreated();
    }

    @JsonProperty("description")
    public String getDescription() {
        return _app.getDescription();
    }
}
