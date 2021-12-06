package com.oracle.interview.db;


import com.oracle.interview.db.entity.Activity;

import java.util.List;
import java.util.Optional;

/**
 * The storage APIs for manipulate the activities list
 */
public interface ActivityRepository {


    /**
     * Add a new {@link Activity} in the storage
     * @param activity the {@link Activity} to be added
     * @return the activity stored into the data source. The attribute {@link Activity#getId()} should be unique in the datasource
     */
    Activity addActivity(Activity activity);

    /**
     * Returns the list of the activities stored
     * @return the list of the activities stored
     */
    Optional<List<Activity>> getActivities();

    /**
     * Returns the {@link Activity} with the same id.
     * @param id the id of the activity that you are looking for.
     * @return the {@link Activity} with the id, null otherwise.
     */
    Optional<Activity> getActivityById(String id);

    /**
     * Modify the {@link Activity} with the same id as {@link Activity#getId()}.
     * @param activity the {@link Activity} to store.
     * @return the {@link Activity} with the id, null if the existing activity doesn't exist.
     */
    Activity editActivity(Activity activity);
}
