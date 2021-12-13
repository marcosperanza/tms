package com.oracle.interview.resources;

import com.oracle.interview.auth.RoleBasedAuthorizer;
import com.oracle.interview.auth.SimpleDatabaseAuthenticator;
import com.oracle.interview.db.ActivityRepository;
import com.oracle.interview.db.UserRepository;
import com.oracle.interview.db.entity.Activity;
import com.oracle.interview.db.entity.User;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class ActivityControllerTest {

    private static final ActivityRepository repository = mock(ActivityRepository.class);
    private static final UserRepository repositoryUSer = mock(UserRepository.class);
    public static final ResourceExtension RULE = ResourceExtension
            .builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<com.oracle.interview.db.entity.User>()
                    .setAuthenticator(new SimpleDatabaseAuthenticator(repositoryUSer))
                    .setAuthorizer(new RoleBasedAuthorizer())
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addResource(new ActivityController(repository))
            .build();

    private static User authUser = new User("auth", "secret", "AUTHENTICATED");
    private static User normalUser = new User("marco", "super-secret", "BASIC");


    private Activity guestUserActivity;
    private Activity normalUserActivity;

    @BeforeEach
    void setup() {
        guestUserActivity = new Activity();
        guestUserActivity.setId("11-22-33");
        guestUserActivity.setDate(1L);
        guestUserActivity.setDescription("TEST");
        guestUserActivity.setUser(SimpleDatabaseAuthenticator.GUEST_USER);

        normalUserActivity = new Activity();
        normalUserActivity.setId("11-22-33-44");
        normalUserActivity.setDate(1L);
        normalUserActivity.setDescription("TEST NORMAL");
        normalUserActivity.setUser(normalUser);

        when(repositoryUSer.getUser("auth")).thenReturn(Optional.ofNullable(authUser));
        when(repositoryUSer.getUser("marco")).thenReturn(Optional.ofNullable(normalUser));
    }

    @AfterEach
    void tearDown() {
        reset(repository);
    }


    @Test
    void activities() {
        when(repository.getActivities(any())).thenReturn(Optional.of(Collections.singletonList(guestUserActivity)));

        List found = RULE.target("/activity").request().get(List.class);

        assertEquals(1, found.size());
    }

    @Test
    void activitiesEmptyShouldReturnNO_CONTENT() {
        when(repository.getActivities(any())).thenReturn(Optional.empty());

        Response found = RULE.target("/activity").request().get();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), found.getStatus());
    }

    @Test
    void activitiesErrorShouldReturn500() {
        when(repository.getActivities(any())).thenThrow(new IllegalArgumentException("fake"));

        Response found = RULE.target("/activity").request().get();

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), found.getStatus());
    }

    @Test
    void activity() {
        when(repository.getActivityById(eq("11-22-33"))).thenReturn(Optional.ofNullable(guestUserActivity));

        Activity found = RULE.target("/activity/11-22-33").request().get(Activity.class);

        // The REST doesn't expose the user property, we have to remove it from the expectation.
        Activity expectedActivityWithoutUserSeriliazed = new Activity(guestUserActivity.getDescription(), guestUserActivity.getDate(), guestUserActivity.isDone());
        expectedActivityWithoutUserSeriliazed.setId("11-22-33");
        assertEquals(expectedActivityWithoutUserSeriliazed, found);
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
        when(repository.addActivity(eq(guestUserActivity))).thenReturn(guestUserActivity);

        Response found = RULE.target("/activity").request(MediaType .APPLICATION_JSON_TYPE).post(Entity.entity(guestUserActivity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.CREATED.getStatusCode(), found.getStatus());
    }


    @Test
    void addErrorShouldReturn500() {
        when(repository.addActivity(eq(guestUserActivity))).thenThrow(new IllegalArgumentException("fake"));

        Response found = RULE.target("/activity").request(MediaType .APPLICATION_JSON_TYPE).post(Entity.entity(guestUserActivity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), found.getStatus());
    }

    @Test
    void edit() {
        when(repository.getActivityById(eq("11-22-33"))).thenReturn(Optional.ofNullable(guestUserActivity));
        when(repository.editActivity(eq(guestUserActivity))).thenReturn(Optional.ofNullable(guestUserActivity));

        Response found = RULE.target("/activity").request(MediaType .APPLICATION_JSON_TYPE).put(Entity.entity(guestUserActivity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.OK.getStatusCode(), found.getStatus());
    }

    @Test
    void editNotFoundShouldReturn404() {
        when(repository.getActivityById(eq("11-22-33"))).thenReturn(Optional.empty());

        Response found = RULE.target("/activity").request(MediaType .APPLICATION_JSON_TYPE).put(Entity.entity(guestUserActivity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), found.getStatus());
    }

    @Test
    void editErrorShouldReturn500() {
        when(repository.editActivity(any())).thenThrow(new IllegalArgumentException("fake"));

        Response found = RULE.target("/activity").request(MediaType .APPLICATION_JSON_TYPE).put(Entity.entity(guestUserActivity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), found.getStatus());
    }



    @Test
    void remove() {
        when(repository.removeById(eq("11-22-33"))).thenReturn(Optional.ofNullable(guestUserActivity));

        Activity deleted = RULE.target("/activity/11-22-33").request(MediaType .APPLICATION_JSON_TYPE).delete(Activity.class);

        assertEquals(guestUserActivity.getId(), deleted.getId());
    }

    @Test
    void removeNotExistingElementReturn404() {
        when(repository.removeById(eq("11-22-33"))).thenReturn(Optional.empty());

        Response found = RULE.target("/activity/11-22-33").request(MediaType .APPLICATION_JSON_TYPE).delete();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), found.getStatus());
    }

    @Test
    void removeErrorReturn500() {
        when(repository.removeById(eq("11-22-33"))).thenThrow(new IllegalArgumentException("fake"));

        Response found = RULE.target("/activity/11-22-33").request(MediaType .APPLICATION_JSON_TYPE).delete();

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), found.getStatus());
    }

    @Test
    void removeAll() {
        String credential = "Basic " + Base64.getEncoder().encodeToString("auth:secret".getBytes());

        when(repository.removeAll()).thenReturn(100);

        Integer deleted = RULE.target("/activity")
                .request(MediaType .APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, credential)
                .delete(Integer.class);

        assertEquals(100, deleted);
    }


    @Test
    void addActivityOnUnauthorizedUserShouldUseGUEST_USER() {
        String credential = "Basic " + Base64.getEncoder().encodeToString("marco:WRONG_PASS".getBytes());

        when(repository.addActivity(eq(guestUserActivity))).thenReturn(guestUserActivity);

        Response found = RULE.target("/activity")
                .request(MediaType .APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, credential)
                .post(Entity.entity(guestUserActivity, MediaType.APPLICATION_JSON_TYPE));


        assertEquals(Response.Status.CREATED.getStatusCode(), found.getStatus());
        verify(repository, times(1)).addActivity(eq(guestUserActivity));
    }

    @Test
    void addActivityOnUnauthorizedUserShouldUseRight_USer() {
        String credential = "Basic " + Base64.getEncoder().encodeToString("marco:super-secret".getBytes());

        when(repository.addActivity(eq(normalUserActivity))).thenReturn(normalUserActivity);

        Response found = RULE.target("/activity")
                .request(MediaType .APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, credential)
                .post(Entity.entity(normalUserActivity, MediaType.APPLICATION_JSON_TYPE));


        assertEquals(Response.Status.CREATED.getStatusCode(), found.getStatus());
        verify(repository, times(1)).addActivity(eq(normalUserActivity));
    }





}
