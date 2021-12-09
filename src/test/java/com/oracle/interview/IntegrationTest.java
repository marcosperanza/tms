package com.oracle.interview;

import com.oracle.interview.db.entity.Activity;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers(disabledWithoutDocker = true)
@ExtendWith(DropwizardExtensionsSupport.class)
public class IntegrationTest {

    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.24"));

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-config.yml");

    public static final DropwizardAppExtension<TMSConfiguration> APP = new DropwizardAppExtension<>(
            TMSApplication.class, CONFIG_PATH,
            ConfigOverride.config("database.url", MY_SQL_CONTAINER::getJdbcUrl),
            ConfigOverride.config("database.user", MY_SQL_CONTAINER::getUsername),
            ConfigOverride.config("database.password", MY_SQL_CONTAINER::getPassword),
            ConfigOverride.config("database.properties.enabledTLSProtocols", "TLSv1.1,TLSv1.2,TLSv1.3")
    );

    @BeforeAll
    public static void migrateDb() throws Exception {
        APP.getApplication().run("db", "migrate", CONFIG_PATH);
    }


    @Test
    void addActivity() {
        String url = postActivity(new Activity("TEST", new Date().getTime(), true));
        Response response = APP.client().target(url).request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void editActivity() {
        String url = postActivity(new Activity("TEST", new Date().getTime(), true));

        Activity response = APP.client().target(url).request().get(Activity.class);

        Activity expectiation = new Activity(response.getDescription(), response.getDate(), response.isDone());
        expectiation.setId(response.getId());
        expectiation.setDone(true);

        putActivity(expectiation);

        response = APP.client().target(url).request().get(Activity.class);

        assertEquals(expectiation, response);

    }


    @Test
    void removeActivity() {
        String url = postActivity(new Activity("TEST", new Date().getTime(), true));

        Activity response = APP.client().target(url).request().get(Activity.class);

        deleteActivity(response.getId());

        Response notFound = APP.client().target(url).request().get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), notFound.getStatus());

    }

    private String postActivity(Activity activity) {
        Response post = APP.client().target("http://localhost:" + APP.getLocalPort() + "/activity")
                .request()
                .post(Entity.entity(activity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(201, post.getStatus());
        return post.getStringHeaders().get("location").get(0);
    }

    private void putActivity(Activity activity) {
        Response post = APP.client().target("http://localhost:" + APP.getLocalPort() + "/activity")
                .request()
                .put(Entity.entity(activity, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, post.getStatus());
    }

    private void deleteActivity(String id) {
        Response post = APP.client().target("http://localhost:" + APP.getLocalPort() + "/activity/" + id)
                .request()
                .delete();

        assertEquals(200, post.getStatus());
    }
}
