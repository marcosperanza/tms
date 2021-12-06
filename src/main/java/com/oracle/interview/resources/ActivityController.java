package com.oracle.interview.resources;

import com.oracle.interview.db.ActivityRepository;
import com.oracle.interview.db.entity.Activity;
import io.dropwizard.hibernate.UnitOfWork;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/activity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActivityController {

    private ActivityRepository repository;


    @Inject
    public ActivityController(ActivityRepository repository) {
        this.repository = repository;
    }

    @POST
    @UnitOfWork
    public Activity add(Activity activity){
        return repository.addActivity(activity);
    }

    @GET
    @UnitOfWork
    public List<Activity> activities() {
        return repository.getActivities();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Activity activity(@PathParam("id") int id) {
        return null;
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Activity remove(@PathParam("id") int id) {
        return null;
    }

    @DELETE
    @UnitOfWork
    public Activity removeAll() {
        return null;
    }


    @PUT
    @Path("/{id}")
    @UnitOfWork
    public Activity edit(Activity activity){
        return null;
    }


}
