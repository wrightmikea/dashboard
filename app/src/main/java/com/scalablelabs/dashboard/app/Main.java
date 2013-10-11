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


import com.scalablelabs.dashboard.app.config.DashboardConfig;
import com.scalablelabs.dashboard.app.healthchecks.DashboardHealthCheck;
import com.scalablelabs.dashboard.app.resources.root.domain.AppResource;
import com.scalablelabs.pluot.app.resources.root.admin.ServerStatusResource;
import com.scalablelabs.pluot.app.service.EmbeddableService;
import com.scalablelabs.pluot.app.status.ServerDescription;
import com.scalablelabs.pluot.app.status.ServerStatus;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.joda.time.DateTime;

public class Main extends EmbeddableService<DashboardConfig> {

  public final static String DESCRIPTION = "Scalable Labs Hypermedia Meta Application";
  public final static String NAME = "dashboard-app";
  public final static String PROP_FILE_PREFIX = "dashboard-app";
  public final static String PROP_PREFIX = "com.scalablelabs.dashboard";
  public final static DateTime STARTED_AT = new DateTime();

  public static void main(String... args) throws Exception {
    Main main = new Main();
    System.out.println(initServerStatus());
    if (2 == args.length) {
      main.run(args);
    } else {
      main.run(new String[] {"server", "src/main/resources/dashboard.yml"}); // $ gradle run
    }
  }

  @Override
  public void initialize(Bootstrap<DashboardConfig> bootstrap) {
    bootstrap.setName(NAME);
    bootstrap.addBundle(new AssetsBundle("/assets/", "/assets/"));
  }

  @Override
  public void run(DashboardConfig ccatConfiguration, Environment environment) throws Exception {
    // root admin resource
    environment.addResource(new ServerStatusResource());

    // root domain resources
    environment.addResource(new AppResource());

    // health checks
    environment.addHealthCheck(new DashboardHealthCheck());
  }

  private static String initServerStatus() {
    ServerDescription description = new ServerDescription();
    description.setName(NAME);
    description.setDescription(DESCRIPTION);
    description.setPropFilePrefix(PROP_FILE_PREFIX);
    description.setPropPrefix(PROP_PREFIX);
    description.setStarted(STARTED_AT);
    description.setStatusSeparator(getStatusSeparator());
    ServerStatus.setStatus(new DashboardServerStatus(description));
    return NAME + " " +
            DESCRIPTION + " " +
            "(version: " + description.getVersion() +
            ", built: " + description.getBuilt() +
            ") started at: " + STARTED_AT;
  }

  private static String getStatusSeparator() {
    String separator = "\ndashboard =\\|/= ";
    for (int i = 0; i < 55; ++i) {
      separator += "~";
    }
    separator += " \\_/ ";
    for (int i = 0; i < 55; ++i) {
      separator += "~";
    }
    return separator + " =\\|/= dashboard\n";
  }

}

class DashboardServerStatus extends ServerStatus {
  DashboardServerStatus(ServerDescription description) {
    super(description);
  }
}