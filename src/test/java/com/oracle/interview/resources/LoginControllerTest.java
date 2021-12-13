package com.oracle.interview.resources;

import com.oracle.interview.auth.RoleBasedAuthorizer;
import com.oracle.interview.auth.SimpleDatabaseAuthenticator;
import com.oracle.interview.db.ActivityRepository;
import com.oracle.interview.db.UserRepository;
import com.oracle.interview.db.entity.User;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class LoginControllerTest {

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
            .addResource(new LoginController())
            .build();


    private static User authUser = new User("marco", "super-secret", "AUTHENTICATED");


    @AfterEach
    void tearDown() {
        reset(repositoryUSer);
    }


    @Test
    void simpleBasicLogin() {
        String credential = "Basic " + Base64.getEncoder().encodeToString("marco:super-secret".getBytes());

        when(repositoryUSer.getUser(any())).thenReturn(Optional.of(authUser));

        String found = RULE.target("/auth")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .get(String.class);

        assertEquals(found, "marco");
    }

    @Test
    void loggedUserName() {
        String credential = "Basic " + Base64.getEncoder().encodeToString("marco:super-secret".getBytes());

        when(repositoryUSer.getUser(any())).thenReturn(Optional.of(authUser));

        String found = RULE.target("/auth/username")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .get(String.class);

        assertEquals(found, "marco");
    }
}
