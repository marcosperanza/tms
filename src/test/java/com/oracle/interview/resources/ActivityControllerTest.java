package com.oracle.interview.resources;

import com.oracle.interview.db.ActivityRepository;
import com.oracle.interview.db.entity.Activity;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class ActivityControllerTest {

    private static final ActivityRepository repository = mock(ActivityRepository.class);
    public static final ResourceExtension RULE = ResourceExtension.builder()
            .addResource(new ActivityController(repository))
            .build();

    private Activity activity;

    @BeforeEach
    void setup() {
        activity = new Activity();
        activity.setId("11-22-33");
        activity.setDate(1L);
        activity.setDescription("TEST");
    }

    @AfterEach
    void tearDown() {
        reset(repository);
    }


    @Test
    void activities() {
        when(repository.getActivities()).thenReturn(Optional.of(Collections.singletonList(activity)));

        List found = RULE.target("/activity").request().get(List.class);

        assertEquals(1, found.size());
    }

    @Test
    void activitiesEmptyShouldReturnNO_CONTENT() {
        when(repository.getActivities()).thenReturn(Optional.empty());

        Response found = RULE.target("/activity").request().get();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), found.getStatus());
    }

    @Test
    void activitiesErrorShouldReturn500() {
        when(repository.getActivities()).thenThrow(new IllegalArgumentException("fake"));

        Response found = RULE.target("/activity").request().get();

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), found.getStatus());
    }

    @Test
    void activity() {
        when(repository.getActivityById(eq("11-22-33"))).thenReturn(Optional.ofNullable(activity));

        Activity found = RULE.target("/activity/11-22-33").request().get(Activity.class);

        assertEquals(activity, found);
    }

    @Test
    void activityNotFountShouldReturn404() {
        when(repository.getActivityById(eq("11-22-33"))).thenReturn(Optional.empty());

        Response found = RULE.target("/activity/11-22-33").request().get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), found.getStatus());
    }

    @Test
    void activityErrorShouldReturn500() {
        when(repository.getActivityById(eq("11-22-33"))).thenThrow(new IllegalArgumentException("fake"));

        Response found = RULE.target("/activity/11-22-33").request().get();

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), found.getStatus());
    }


    @Test
    void add() {
        when(repository.addActivity(eq(activity))).thenReturn(activity);

        Response found = RULE.target("/activity").request(MediaType .APPLICATION_JSON_TYPE).post(Entity.entity(activity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.CREATED.getStatusCode(), found.getStatus());
    }


    @Test
    void addErrorShouldReturn500() {
        when(repository.addActivity(eq(activity))).thenThrow(new IllegalArgumentException("fake"));

        Response found = RULE.target("/activity").request(MediaType .APPLICATION_JSON_TYPE).post(Entity.entity(activity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), found.getStatus());
    }

    @Test
    void edit() {
        when(repository.getActivityById(eq("11-22-33"))).thenReturn(Optional.ofNullable(activity));
        when(repository.editActivity(eq(activity))).thenReturn(activity);

        Response found = RULE.target("/activity").request(MediaType .APPLICATION_JSON_TYPE).put(Entity.entity(activity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.OK.getStatusCode(), found.getStatus());
    }

    @Test
    void editNotFoundShouldReturn404() {
        when(repository.getActivityById(eq("11-22-33"))).thenReturn(Optional.empty());

        Response found = RULE.target("/activity").request(MediaType .APPLICATION_JSON_TYPE).put(Entity.entity(activity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), found.getStatus());
    }

    @Test
    void editErrorShouldReturn500() {
        when(repository.getActivityById(eq("11-22-33"))).thenThrow(new IllegalArgumentException("fake"));

        Response found = RULE.target("/activity").request(MediaType .APPLICATION_JSON_TYPE).put(Entity.entity(activity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), found.getStatus());
    }



    @Test
    void remove() {
    }

    @Test
    void removeAll() {
    }


}
