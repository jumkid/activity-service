package com.jumkid.activity.service;

import com.jumkid.activity.exception.ActivityNotFoundException;
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
        if (optional.isPresent()) {
            return optional.filter(activityEntity -> isCurrentUser(activityEntity.getCreatedBy())).isPresent();
        } else {
            throw new ActivityNotFoundException(activityId);
        }
    }

    public boolean isOwner(ActivityEntity activityEntity) {
        return isCurrentUser(activityEntity.getCreatedBy());
    }

    public boolean isAssignee(long activityId) {
        Optional<ActivityEntity> optional = activityRepository.findById(activityId);
        if (optional.isPresent()) {
            return isAssignee(optional.get());
        } else {
            throw new ActivityNotFoundException(activityId);
        }
    }

    public boolean isAssignee(ActivityEntity activityEntity) {
        String currentUserId = this.getUserProfile().getId();

        if (activityEntity.getActivityAssigneeEntities() == null ||
                activityEntity.getActivityAssigneeEntities().isEmpty()) {
            return false;
        } else {
            return activityEntity.getActivityAssigneeEntities()
                    .stream()
                    .anyMatch( m -> m.getAssigneeId().equals(currentUserId));
        }
    }

    private boolean isCurrentUser(String userId) {
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
