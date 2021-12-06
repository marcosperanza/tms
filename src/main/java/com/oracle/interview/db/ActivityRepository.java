package com.oracle.interview.db;


import com.oracle.interview.db.entity.Activity;

import java.util.List;

public interface ActivityRepository {


    Activity addActivity(Activity activity);

    List<Activity> getActivities();
}
