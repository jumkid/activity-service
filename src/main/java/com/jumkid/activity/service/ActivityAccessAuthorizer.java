package com.jumkid.activity.service;

import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.repository.ActivityRepository;
import com.jumkid.share.security.exception.UserProfileNotFoundException;
import com.jumkid.share.user.UserProfile;
import com.jumkid.share.user.UserProfileManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "activityAccessAuthorizer")
public class ActivityAccessAuthorizer {

    private final UserProfileManager userProfileManager;

    private final ActivityRepository activityRepository;

    public ActivityAccessAuthorizer(UserProfileManager userProfileManager, ActivityRepository activityRepository) {
        this.userProfileManager = userProfileManager;
        this.activityRepository = activityRepository;
    }

    public boolean isOwner(long activityId) {
        Optional<ActivityEntity> optional = activityRepository.findById(activityId);
        return optional.filter(activityEntity -> isCurrentUserOwned(activityEntity.getCreatedBy())).isPresent();
    }

    public boolean isOwner(ActivityEntity activityEntity) {
        return isCurrentUserOwned(activityEntity.getCreatedBy());
    }

    private boolean isCurrentUserOwned(String userId) {
        return this.getUserProfile().getId().equals(userId);
    }

    private UserProfile getUserProfile() {
        UserProfile userProfile = userProfileManager.fetchUserProfile();
        if (userProfile == null) {
            throw new UserProfileNotFoundException("User profile could not be found. Access is denied");
        } else {
            return userProfile;
        }
    }
}
