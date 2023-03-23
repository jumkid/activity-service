package com.jumkid.activity.service;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.exception.ActivityNotFoundException;

import java.util.List;

public interface ActivityService {

    Activity getActivity(long activityId) throws ActivityNotFoundException;

    List<Activity> getUserActivities();

    List<Activity> getEntityLinkedActivities(String entityId, String entityName);

    Activity addActivity(Activity activity);

    Activity updateActivity(long activityId, Activity partialActivity) throws ActivityNotFoundException;

    void deleteActivity(long activityId);
}
