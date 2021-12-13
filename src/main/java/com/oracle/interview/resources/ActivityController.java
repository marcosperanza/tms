package com.oracle.interview.resources;

import com.codahale.metrics.annotation.Timed;
import com.oracle.interview.db.ActivityRepository;
import com.oracle.interview.db.entity.Activity;
import com.oracle.interview.db.entity.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.oracle.interview.auth.SimpleDatabaseAuthenticator.GUEST_USER;

@Path("/activity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "ActivityController")
public class ActivityController {

    private final ActivityRepository repository;

    public ActivityController(ActivityRepository repository) {
        this.repository = repository;
    }


    @ApiOperation(value = "Add a new activity ", response = Activity.class, tags = "ActivityController")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 500, message = "In case of error"),
    })
    @POST
    @Timed
    @UnitOfWork
    public Response add(@ApiParam(name = "user", hidden = true) @Auth Optional<User> user, Activity activity) {
        activity.setUser(user.orElse(GUEST_USER));
        Activity a = repository.addActivity(activity);
        return Response.created(URI.create("/activity/" + a.getId())).entity(a).build();
    }

    @ApiOperation(value = "Get the list of all activities ", response = Iterable.class, tags = "ActivityController")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 204, message = "No content if no activities are found"),
            @ApiResponse(code = 500, message = "In case of error")
    })
    @GET
    @Timed
    @UnitOfWork
    public Response activities(@ApiParam(name = "user", hidden = true) @Auth Optional<User> user) {
        Optional<List<Activity>> activities = repository.getActivities(user.orElse(GUEST_USER));
        if (!activities.isPresent()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok().entity(activities).build();
    }

    @ApiOperation(value = "Get a single activity ", response = Activity.class, tags = "ActivityController")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = Activity.class),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "In case of error") })
    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response activity(@PathParam("id") String id) {
        Optional<Activity> activity = repository.getActivityById(id);
        if (!activity.isPresent()) {
            return  Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(activity).build();
    }


    @ApiOperation(value = "Remove a single activity ", response = Activity.class, tags = "ActivityController")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = Activity.class),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "In case of error") })
    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response remove(@PathParam("id") String id) {
        Optional<Activity> activity = repository.removeById(id);
        if (!activity.isPresent()) {
            return  Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(activity.get()).build();
    }


    @ApiOperation(value = "Remove all activities", response = Activity.class, tags = "ActivityController")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK: number of deleter activities", response = Integer.class),
            @ApiResponse(code = 500, message = "In case of error") })
    @DELETE
    @UnitOfWork
    @RolesAllowed("AUTHENTICATED")
    public Response removeAll() {
        int entity = repository.removeAll();
        return Response.status(Response.Status.OK).entity(entity).build();
    }


    @ApiOperation(value = "Edit a single activity ", response = Activity.class, tags = "ActivityController")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK", response = Activity.class),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "In case of error") })
    @PUT
    @UnitOfWork
    public Response edit(@ApiParam(name = "user", hidden = true) @Auth Optional<User> user, Activity activity){
        activity.setUser(user.orElse(GUEST_USER));
        Optional<Activity> a = repository.editActivity(activity);
        if (!a.isPresent()) {
            return  Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(a.get()).build();

    }


}
