package com.oracle.interview.resources;

import com.oracle.interview.db.ActivityRepository;
import com.oracle.interview.db.entity.Activity;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @POST
    @UnitOfWork
    public Activity add(Activity activity){
        return repository.addActivity(activity);
    }

    @ApiOperation(value = "Get the list of all activities ", response = Iterable.class, tags = "getActivities")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GET
    @UnitOfWork
    public List<Activity> activities() {
        return repository.getActivities();
    }

    @ApiOperation(value = "Get a single activity ", response = Activity.class, tags = "getActivity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = Activity.class),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GET
    @Path("/{id}")
    @UnitOfWork
    public Activity activity(@PathParam("id") String id) {
        return repository.getActivityById(id);
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
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PUT
    @Path("/{id}")
    @UnitOfWork
    public Activity edit(Activity activity){
        return repository.editActivity(activity);
    }


}
