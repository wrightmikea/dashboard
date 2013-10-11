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
package com.scalablelabs.dashboard.app;

import com.scalablelabs.dashboard.core.entity.App;
import com.scalablelabs.pluot.app.result.EmptyResult;
import com.scalablelabs.pluot.app.result.ListResult;
import com.scalablelabs.pluot.app.result.SingleResult;
import com.scalablelabs.pluot.app.result.Status;
import com.scalablelabs.pluot.repo.Store;
import org.joda.time.DateTime;

import java.util.Date;

/**
 *
 */
public class CrudApp {
    private Store<App> _store = new Store<>();

  private DateTime _lastFetchedTime = Main.STARTED_AT;
  private DateTime _lastPublishedTime = Main.STARTED_AT;

    public SingleResult<App> createApp(App app) {
      app.setCreated(new Date().getTime());
        _store.storeEntity(app);
      System.err.println("stored app key=" + app.getKey());
      System.err.println("stored app created=" + app.getCreated());
        return new SingleResult<>(Status.ADDED, app);
    }

    public SingleResult<App> findByKey(int key) {
        App app = _store.getEntity(key);
        if (null != app) {
            return new SingleResult<>(Status.FOUND, app);

        }
        return new SingleResult<>(Status.NOT_FOUND, null);
    }

    public ListResult<App> findAll() {
        return new ListResult<>(Status.FOUND, _store.getEntities());
    }

    public EmptyResult deleteByKey(int key) {
        Status maybeDeleted = (null != _store.deleteEntity(key)) ? Status.DELETED : Status.NOT_FOUND;
        return new EmptyResult(maybeDeleted);
    }

    public SingleResult<App> replaceByKey(App replacement, int key) {
        if (null != _store.deleteEntity(key)) {
            replacement.setKey(key);
            _store.storeEntity(replacement);
            return new SingleResult<>(Status.UPDATED, replacement);
        } else {
            return new SingleResult<>(Status.NOT_FOUND, null);
        }
    }

  public DateTime getLastFetchedTime() {
    return _lastFetchedTime;
  }

  public DateTime getLastPublishedTime() {
    return _lastPublishedTime;
  }

  public void setLastFetchedTime(DateTime lastFetchedTime) {
    _lastFetchedTime = lastFetchedTime;
  }

  public void setLastPublishedTime(DateTime lastPublishedTime) {
    _lastPublishedTime = lastPublishedTime;
  }


}
