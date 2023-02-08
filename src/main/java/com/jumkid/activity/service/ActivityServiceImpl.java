package com.jumkid.activity.service;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.controller.dto.ActivityNotification;
import com.jumkid.activity.enums.NotifyTimeUnit;
import com.jumkid.activity.exception.ActivityNotFoundException;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.ActivityNotificationEntity;
import com.jumkid.activity.repository.ActivityRepository;
import com.jumkid.activity.service.mapper.ActivityMapper;
import com.jumkid.activity.service.mapper.MapperContext;
import com.jumkid.share.user.UserProfile;
import com.jumkid.share.user.UserProfileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("activityService")
public class ActivityServiceImpl implements ActivityService{

    private final ActivityRepository activityRepository;

    private final UserProfileManager userProfileManager;

    private final ActivityMapper activityMapper;
    private final MapperContext mapperContext;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository, UserProfileManager userProfileManager,
                               ActivityMapper activityMapper, MapperContext mapperContext) {
        this.activityRepository = activityRepository;
        this.userProfileManager = userProfileManager;
        this.activityMapper = activityMapper;
        this.mapperContext = mapperContext;
    }

    @Override
    public Activity getActivity(long activityId) throws ActivityNotFoundException{
        Optional<ActivityEntity> optional = activityRepository.findById(activityId);
        if (optional.isPresent()) {
            ActivityEntity entity = optional.get();
            return activityMapper.entityToDTO(entity, mapperContext);
        } else {
            throw new ActivityNotFoundException(activityId);
        }
    }

    @Override
    public List<Activity> getUserActivities() {
        log.debug("fetch all activities for current user");
        return activityMapper.entitiesToDTOS(activityRepository.findByUser(getCurrentUserId()));
    }

    @Override
    public List<Activity> getEntityLinkedActivities(String entityId, String entityName) {
        log.debug("fetch all activities linked to the entity id:[{}] name:[{}] for current user ", entityId, entityName);
        return activityMapper
                .entitiesToDTOS(activityRepository.findByEntityLink(entityId, entityName, getCurrentUserId()));
    }

    @Override
    @Transactional
    public Activity addActivity(Activity activity) {
        normalizeDTO(null, activity, null);
        ActivityEntity newActivity = activityMapper.dtoToEntity(activity, mapperContext);
        computeNotifyTriggerDatetime(newActivity);
        newActivity = activityRepository.save(newActivity);

        return activityMapper.entityToDTO(newActivity, mapperContext);
    }

    @Override
    @Transactional
    public Activity updateActivity(long activityId, Activity partialActivity) throws ActivityNotFoundException{
        ActivityEntity existActivityEntity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));

        normalizeDTO(activityId, partialActivity, existActivityEntity);

        activityMapper.updateEntityFromDto(partialActivity, existActivityEntity);

        computeNotifyTriggerDatetime(existActivityEntity);

        ActivityEntity updatedEntity = activityRepository.save(existActivityEntity);

        return activityMapper.entityToDTO(updatedEntity, mapperContext);
    }

    @Override
    @Transactional
    public void deleteActivity(long activityId) {
        ActivityEntity oldActivityEntity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));

        activityRepository.delete(oldActivityEntity);
    }

    private void normalizeDTO(Long activityId, Activity dto, ActivityEntity oldActivityEntity) {
        dto.setId(activityId);

        if (dto.getEndDate() == null && dto.getStartDate() != null) dto.setEndDate(dto.getStartDate().plusHours(1));

        if (dto.getAutoNotify() != null && dto.getAutoNotify() && dto.getActivityNotification() == null) {
            dto.setActivityNotification(ActivityNotification.builder()
                                            .notifyBefore(5)
                                            .notifyBeforeUnit(NotifyTimeUnit.MINUTE)
                                            .expired(false)
                                            .build());
        }

        LocalDateTime now = LocalDateTime.now();
        String userId = getCurrentUserId();

        dto.setModifiedBy(userId);
        dto.setModificationDate(now);

        if (oldActivityEntity != null) {
            dto.setCreatedBy(oldActivityEntity.getCreatedBy());
            dto.setCreationDate(oldActivityEntity.getCreationDate());
        } else {
            dto.setCreatedBy(userId);  //UserDetail uses password to carry user id
            dto.setCreationDate(now);
        }
    }

    private void computeNotifyTriggerDatetime(ActivityEntity activityEntity) {
        ActivityNotificationEntity activityNotificationEntity = activityEntity.getActivityNotificationEntity();
        LocalDateTime startDate = activityEntity.getStartDate();
        if (activityNotificationEntity != null) {
            LocalDateTime triggerDatetime = switch (activityNotificationEntity.getNotifyBeforeUnit()) {
                case DAY -> startDate.minusDays(activityNotificationEntity.getNotifyBefore());
                case HOUR -> startDate.minusHours(activityNotificationEntity.getNotifyBefore());
                case MINUTE -> startDate.minusMinutes(activityNotificationEntity.getNotifyBefore());
            };
            activityNotificationEntity.setTriggerDatetime(triggerDatetime);
            activityNotificationEntity.setActivityEntity(activityEntity);
        }
    }

    private String getCurrentUserId() {
        UserProfile userProfile = userProfileManager.fetchUserProfile();
        return userProfile != null ? userProfile.getId() : null;
    }

}
