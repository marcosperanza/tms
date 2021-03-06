package com.oracle.interview.db;

import com.oracle.interview.db.entity.Activity;
import com.oracle.interview.db.entity.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

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
     * @param user
     */
    @Override
    public Optional<List<Activity>> getActivities(User user) {
        Query<Activity> query = namedTypedQuery("com.oracle.activity.findAll");
        query.setParameter("user", user);
        List<Activity> activities = list(query);

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

    @Override
    public int removeAll() {
        Query<?> activityQuery = namedQuery("com.oracle.activity.deleteAll");
        return activityQuery.executeUpdate();
    }

    @Override
    public Optional<Activity> removeById(String id) {
        Optional<Activity> activityById = getActivityById(id);
        if (!activityById.isPresent()) {
            return Optional.empty();
        }
        Query<?> activityQuery = namedQuery("com.oracle.activity.deleteById");
        activityQuery.setParameter("id", id);
        int deleted = activityQuery.executeUpdate();
        if (deleted == 0) {
            return Optional.empty();
        }
        return activityById;
    }

}
