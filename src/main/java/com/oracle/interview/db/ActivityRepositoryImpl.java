package com.oracle.interview.db;

import com.oracle.interview.db.entity.Activity;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class ActivityRepositoryImpl extends AbstractDAO<Activity> implements ActivityRepository {


    public ActivityRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Activity addActivity(Activity activity) {
        return persist(activity);
    }

    @Override
    public List<Activity> getActivities() {
        return list(namedTypedQuery("com.oracle.activity.findAll"));
    }

    @Override
    public Activity getActivityById(String id) {
        return get(id);
    }

    @Override
    public Activity editActivity(Activity activity) {
        return persist(activity);
    }
}
