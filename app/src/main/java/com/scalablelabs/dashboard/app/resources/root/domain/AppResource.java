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
package com.scalablelabs.dashboard.app.resources.root.domain;

import com.google.common.collect.Lists;
import com.scalablelabs.dashboard.app.CrudApp;
import com.scalablelabs.dashboard.app.representations.AppRepresentation;
import com.scalablelabs.dashboard.core.entity.App;
import com.scalablelabs.pluot.app.result.EmptyResult;
import com.scalablelabs.pluot.app.result.ListResult;
import com.scalablelabs.pluot.app.result.SingleResult;
import com.scalablelabs.pluot.app.result.Status;
import org.joda.time.DateTime;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/apps")
public class AppResource {

    public static final String APP_ID_PATH = "/{app_id: [0-9]+}";

    public static CrudApp APP;

    public AppResource() {
        APP = new CrudApp();
    }

  @GET // e.g., GET /api/v1/apps gets a list of all apps
  @Produces(MediaType.APPLICATION_JSON)
  public List<AppRepresentation> getAllApps(/* @TODO mwright use: @Context UriInfo uriInfo */) throws Exception {
    List<AppRepresentation> representations = Lists.newArrayList();
    ListResult<App> apps = APP.findAll();
    for (App app : apps.getEntities()) {
	App subset = new App(app.getDescription()); // add  "{ ...omitted... }");
      subset.setKey(app.getKey());
      subset.setCreated(app.getCreated());
      representations.add(new AppRepresentation(subset, new URI("/apps/" + app.getKey())));
    }
    return representations;
  }

    // sub-resource methods

    @GET // e.g., GET /api/v1/apps/nnn gets the app with key=nnn
    @Path(APP_ID_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public AppRepresentation getApp(@PathParam("app_id") String appId,
                                          @Context UriInfo uriInfo) {
        int key = Integer.parseInt(appId);
        SingleResult<App> getAppResult = APP.findByKey(key);
        App app;
        if (getAppResult.getStatus().equals(Status.FOUND)) {
            app = getAppResult.getEntity();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
      APP.setLastFetchedTime(new DateTime());
      return new AppRepresentation(app, createAppResourceUri("" + key, uriInfo));
    }

  @POST // e.g., POST /api/v1/apps creates an app
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createAppFromFormParameters(@FormParam("description") @DefaultValue("default") String descr
                                                 /* @TODO mwright use or remove: ,@Context UriInfo uriInfo */) {
    App app = new App(descr);
    SingleResult<App> appCreateResult = APP.createApp(app);
    Status createStatus = appCreateResult.getStatus();
    boolean isAdded = appCreateResult.getStatus().equals(Status.ADDED);
    URI loc = URI.create("" + appCreateResult.getEntity().getKey());
    System.err.println((isAdded ? "created" : "replaced") + " app with key=" + app.getKey() +
            " and descr=" + descr); // @TODO mwright actually store app (and the app data)
    if (Status.ADDED.equals(createStatus)) {
      return Response.created(loc).build();
    }
    if (Status.UPDATED.equals(createStatus)) {
      return Response.ok(loc).contentLocation(loc).build();
    }
    if (Status.BAD_REQUEST.equals(createStatus)) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST); // @TODO give reason
    }
    throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR); // @TODO log error
  }


    @DELETE // e.g., DELETE /api/v1/apps/nnn deletes app with key=nnn
    @Path(APP_ID_PATH)
    public Response deleteApp(@PathParam("app_id") String appId
                               /* @TODO mwright use: , @Context UriInfo uriInfo */) {
        int key = Integer.parseInt(appId);
        EmptyResult deleteResult = APP.deleteByKey(key);
        Status deleteStatus = deleteResult.getStatus();
        if (Status.DELETED.equals(deleteStatus)) {
            return Response.noContent().build();
        }
        if (Status.NOT_FOUND.equals(deleteStatus)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR); // @TODO log error
    }

  private URI createAppResourceUri(String appId, UriInfo uriInfo) {
    return uriInfo.getBaseUriBuilder().path(AppResource.class).path(appId).build();
  }
}