package com.jumkid.activity.service;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.controller.dto.ActivityNotification;
import com.jumkid.activity.enums.ActivityStatus;
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
            return activityMapper.entityToDTO(entity);
        } else {
            throw new ActivityNotFoundException(activityId);
        }
    }

    @Override
    public List<Activity> getUserActivities() {
        log.debug("fetch all activities for current user");
        return activityMapper.entitiesToDTOS(activityRepository.findAll());
    }

    @Override
    @Transactional
    public Activity addActivity(Activity activity) {
        normalizeDTO(null, activity, null);
        ActivityEntity entity = activityMapper.dtoToEntity(activity, mapperContext);
        computeNotifyTriggerDatetime(entity);
        ActivityEntity newActivity = activityRepository.save(entity);

        return activityMapper.entityToDTO(newActivity);
    }

    @Override
    @Transactional
    public Activity updateActivity(long activityId, Activity activity) throws ActivityNotFoundException{
        ActivityEntity oldActivityEntity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));

        normalizeDTO(activityId, activity, oldActivityEntity);

        ActivityEntity updateEntity = activityMapper.dtoToEntity(activity, mapperContext);
        computeNotifyTriggerDatetime(updateEntity);

        updateEntity = activityRepository.save(updateEntity);

        return activityMapper.entityToDTO(updateEntity);
    }

    @Override
    @Transactional
    public void deleteActivity(long activityId) {
        ActivityEntity oldActivityEntity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));

        activityRepository.delete(oldActivityEntity);
    }

    private void normalizeDTO(Long activityId, Activity dto, ActivityEntity oldActivityEntity) {
        dto.setActivityId(activityId);
        if (dto.getStatus() == null) dto.setStatus(ActivityStatus.DRAFT);
        if (dto.getEndDate() == null) dto.setEndDate(dto.getStartDate().plusHours(1));
        if (dto.getAutoNotify() != null && dto.getAutoNotify() && dto.getActivityNotification() == null) {
            dto.setActivityNotification(ActivityNotification.builder()
                                            .notifyBefore(5)
                                            .notifyBeforeUnit(NotifyTimeUnit.MINUTE)
                                            .expired(false)
                                            .build());
        }

        LocalDateTime now = LocalDateTime.now();
        UserProfile userProfile = userProfileManager.fetchUserProfile();
        String userId = userProfile != null ? userProfile.getId() : null;

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
        }
    }

}
