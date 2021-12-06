package com.oracle.interview.db;

import com.oracle.interview.db.entity.Activity;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Default implementation if {@link ActivityRepository}.
 * It uses the {@link AbstractDAO} to access to the data source
 */
public class ActivityRepositoryImpl extends AbstractDAO<Activity> implements ActivityRepository {


    public ActivityRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Activity addActivity(Activity activity) {
        return persist(activity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Activity> getActivities() {
        return list(namedTypedQuery("com.oracle.activity.findAll"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Activity getActivityById(String id) {
        return get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Activity editActivity(Activity activity) {
        return persist(activity);
    }
}
