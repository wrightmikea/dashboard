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
package com.scalablelabs.dashboard.core.entity;

import com.scalablelabs.pluot.api.entity.AbstractEntity;
import com.scalablelabs.pluot.api.entity.Entity;

/**
 */
public class App extends AbstractEntity implements Entity {
    private String _description;
  private long _created;

    public App(String description) {
        _description = description;
    }

    public String getDescription() {
        return _description;
    }

    public long getCreated() {
      return _created;
    }

    public void setCreated(long created) {
      _created = created;
    }
}
