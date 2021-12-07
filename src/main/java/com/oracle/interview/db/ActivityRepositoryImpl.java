package com.oracle.interview.db;

import com.oracle.interview.db.entity.Activity;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
    public Optional<List<Activity>> getActivities() {
        List<Activity> activities = list(namedTypedQuery("com.oracle.activity.findAll"));
        if (activities == null || activities.isEmpty())  {
            return Optional.empty();
        }
        return Optional.of(activities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Activity> getActivityById(String id) {
        return Optional.ofNullable(get(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Activity> editActivity(Activity activity) {
        Optional<Activity> activityById = getActivityById(activity.getId());
        if (!activityById.isPresent()) {
            return Optional.empty();
        }
        Activity a = activityById.get();
        a.setDescription(activity.getDescription());
        a.setDate(activity.getDate());
        a.setDone(activity.isDone());
        return Optional.ofNullable(persist(a));
    }
}
