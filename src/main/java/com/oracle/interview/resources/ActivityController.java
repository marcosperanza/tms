package com.oracle.interview.resources;

import com.codahale.metrics.annotation.Timed;
import com.oracle.interview.db.ActivityRepository;
import com.oracle.interview.db.entity.Activity;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/activity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "ActivityController")
public class ActivityController {

    private final ActivityRepository repository;

    public ActivityController(ActivityRepository repository) {
        this.repository = repository;
    }


    @ApiOperation(value = "Add a new activity ", response = Activity.class, tags = "addActivity")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 500, message = "In case of error"),
    })
    @POST
    @Timed
    @UnitOfWork
    public Response add(Activity activity){
        Activity a = repository.addActivity(activity);
        return Response.created(URI.create("/activity/" + a.getId())).entity(a).build();
    }

    @ApiOperation(value = "Get the list of all activities ", response = Iterable.class, tags = "getActivities")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "In case of error")
    })
    @GET
    @Timed
    @UnitOfWork
    public Response activities() {
        List<Activity> activities = repository.getActivities();
        return Response.ok().entity(activities).build();
    }

    @ApiOperation(value = "Get a single activity ", response = Activity.class, tags = "getActivity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = Activity.class),
            @ApiResponse(code = 500, message = "In case of error") })
    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response activity(@PathParam("id") String id) {
        Activity activity = repository.getActivityById(id);
        return Response.ok().entity(activity).build();
    }


    // TODO
    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Activity remove(@PathParam("id") int id) {
        return null;
    }


    // TODO
    @DELETE
    @UnitOfWork
    public Activity removeAll() {
        return null;
    }


    @ApiOperation(value = "Edit a single activity ", response = Activity.class, tags = "editActivity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = Activity.class),
            @ApiResponse(code = 500, message = "In case of error") })
    @PUT
    @UnitOfWork
    public Response edit(Activity activity){
        Activity a = repository.editActivity(activity);
        return Response.ok().entity(a).build();

    }


}
