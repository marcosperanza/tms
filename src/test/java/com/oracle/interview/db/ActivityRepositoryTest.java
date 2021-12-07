package com.oracle.interview.db;

import com.oracle.interview.db.entity.Activity;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class ActivityRepositoryTest {

    public DAOTestExtension daoTestRule = DAOTestExtension.newBuilder()
            .addEntityClass(Activity.class)
            .build();

    private ActivityRepository activityRepository;


    @BeforeEach
    void setUp() throws Exception {
        activityRepository = new ActivityRepositoryImpl(daoTestRule.getSessionFactory());
    }

    @Test
    void addActivity() {
        Activity activity = new Activity();
        activity.setDescription("TEST");
        activity.setId(null);
        activity.setDate(11111L);
        activity.setDone(true);

        final Activity a = daoTestRule.inTransaction(() -> activityRepository.addActivity(activity));
        assertNotNull(a.getId());
        assertEquals(activity.getDescription(), a.getDescription());
        assertEquals(activity.getDate(), a.getDate());
        assertEquals(activity.isDone(), a.isDone());
    }

    @Test
    void getActivities() {
        daoTestRule.inTransaction(() -> {
            activityRepository.addActivity(new Activity("TEST", 1, false));
            activityRepository.addActivity(new Activity("TEST 2", 1, false));
            activityRepository.addActivity(new Activity("TEST 3", 1, true));
        });

        Optional<List<Activity>> activities = activityRepository.getActivities();

        assertTrue(activities.isPresent());
        assertEquals(3, activities.get().size());
    }

    @Test
    void getActivityById() {
        AtomicReference<Activity> activity = new AtomicReference<>();
        daoTestRule.inTransaction(() -> {
            activityRepository.addActivity(new Activity("TEST", 1, false));
            activity.set(activityRepository.addActivity(new Activity("TEST 2", 1, false)));
            activityRepository.addActivity(new Activity("TEST 3", 1, true));
        });

        Optional<Activity> activityById = activityRepository.getActivityById(activity.get().getId());

        assertTrue(activityById.isPresent());
        assertEquals(activity.get(), activityById.get());
    }

    @Test
    void editActivity() {
        AtomicReference<Activity> activity = new AtomicReference<>();
        daoTestRule.inTransaction(() -> {
            activityRepository.addActivity(new Activity("TEST", 1, false));
            activity.set(activityRepository.addActivity(new Activity("TEST 2", 1, false)));
            activityRepository.addActivity(new Activity("TEST 3", 1, true));
        });

        Activity toBeEdited = new Activity("TEST 2", 1, true);
        toBeEdited.setId(activity.get().getId());

        Optional<Activity> activityById = activityRepository.editActivity(toBeEdited);

        assertTrue(activityById.isPresent());
        assertEquals(toBeEdited, activityById.get());

        assertEquals(3, activityRepository.getActivities().get().size());
        assertTrue( activityRepository.getActivityById(activity.get().getId()).get().isDone());
    }

    @Test
    void editActivityNonPresentElementsReturnsEmpty() {
        daoTestRule.inTransaction(() -> {
            activityRepository.addActivity(new Activity("TEST", 1, false));
            activityRepository.addActivity(new Activity("TEST 2", 1, false));
            activityRepository.addActivity(new Activity("TEST 3", 1, true));
        });

        Activity toBeEdited = new Activity("TEST 2", 1, true);
        toBeEdited.setId("not present");

        Optional<Activity> activityById = activityRepository.editActivity(toBeEdited);

        assertFalse(activityById.isPresent());
    }
}
