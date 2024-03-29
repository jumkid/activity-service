package com.jumkid.activity.service;

import com.jumkid.activity.exception.ActivityNotFoundException;
import com.jumkid.activity.exception.ContentResourceNotFoundException;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.ContentResourceEntity;
import com.jumkid.activity.repository.ActivityRepository;
import com.jumkid.activity.repository.ContentResourceRepository;
import com.jumkid.share.security.exception.UserProfileNotFoundException;
import com.jumkid.share.user.UserProfile;
import com.jumkid.share.user.UserProfileManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "activityAccessAuthorizer")
public class ActivityAccessAuthorizer {

    private final UserProfileManager userProfileManager;

    private final ActivityRepository activityRepository;
    private final ContentResourceRepository contentResourceRepository;

    public ActivityAccessAuthorizer(UserProfileManager userProfileManager,
                                    ActivityRepository activityRepository,
                                    ContentResourceRepository contentResourceRepository) {
        this.userProfileManager = userProfileManager;
        this.activityRepository = activityRepository;
        this.contentResourceRepository = contentResourceRepository;
    }

    public boolean isOwner(long activityId) throws ActivityNotFoundException {
        Optional<ActivityEntity> optional = activityRepository.findById(activityId);
        if (optional.isPresent()) {
            return isCurrentUser(optional.get().getCreatedBy());
        } else {
            throw new ActivityNotFoundException(activityId);
        }
    }

    public boolean isOwner(ActivityEntity activityEntity) {
        return isCurrentUser(activityEntity.getCreatedBy());
    }

    public boolean hasPermissionForContentResource(long contentResourceId)
            throws ActivityNotFoundException, ContentResourceNotFoundException {
        Optional<ContentResourceEntity> optional = contentResourceRepository.findById(contentResourceId);
        if (optional.isPresent()) {
            Long activityId = optional.get().getActivityEntity().getId();
            return isOwner(activityId) || isAssignee(activityId);
        }

        throw new ContentResourceNotFoundException(contentResourceId);
    }

    public boolean isAssignee(long activityId) throws ActivityNotFoundException {
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
