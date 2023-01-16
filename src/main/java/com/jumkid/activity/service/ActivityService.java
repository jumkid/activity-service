package com.jumkid.activity.service;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.exception.ActivityNotFoundException;

import java.util.List;

public interface ActivityService {

    public Activity getActivity(long activityId) throws ActivityNotFoundException;

    public List<Activity> getUserActivities();

    public Activity addActivity(Activity activity);

    public Activity updateActivity(long activityId, Activity partialActivity) throws ActivityNotFoundException;

    public void deleteActivity(long activityId);
}
