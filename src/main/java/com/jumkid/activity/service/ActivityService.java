package com.jumkid.activity.service;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.controller.dto.ActivityNotification;
import com.jumkid.activity.enums.ActivityStatus;
import com.jumkid.activity.enums.NotifyTimeUnit;
import com.jumkid.activity.exception.ActivityNotFoundException;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.repository.ActivityRepository;
import com.jumkid.activity.service.mapper.ActivityMapper;
import com.jumkid.activity.service.mapper.ListActivityMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper = Mappers.getMapper( ActivityMapper.class );
    private final ListActivityMapper listActivityMapper = Mappers.getMapper(ListActivityMapper.class);

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Activity getActivity(long activityId) {
        Optional<ActivityEntity> optional = activityRepository.findById(activityId);
        if (optional.isPresent()) {
            ActivityEntity entity = optional.get();
            return activityMapper.entityToDTO(entity);
        } else {
            throw new ActivityNotFoundException(activityId);
        }
    }

    public List<Activity> getActivities() {
        log.debug("fetch all activities");
        return listActivityMapper.entitiesToDTOS(activityRepository.findAll());
    }

    @Transactional
    public Activity addActivity(Activity activity) {
        normalizeDTO(null, activity, null);
        ActivityEntity entity = activityMapper.dtoToEntity(activity);
        ActivityEntity newActivity = activityRepository.save(entity);

        return activityMapper.entityToDTO(newActivity);
    }

    @Transactional
    public Activity updateActivity(long activityId, Activity activity) {
        ActivityEntity oldActivityEntity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));

        normalizeDTO(activityId, activity, oldActivityEntity);

        ActivityEntity updateEntity = activityMapper.dtoToEntity(activity);

        updateEntity = activityRepository.save(updateEntity);

        return activityMapper.entityToDTO(updateEntity);
    }

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
        UserDetails userDetail = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetail != null) dto.setModifiedBy(userDetail.getPassword());
        dto.setModificationDate(now);

        if (oldActivityEntity != null) {
            dto.setCreatedBy(oldActivityEntity.getCreatedBy());
            dto.setCreationDate(oldActivityEntity.getCreationDate());
        } else {
            if (userDetail != null) dto.setCreatedBy(userDetail.getPassword());  //UserDetail uses password to carry user id
            dto.setCreationDate(now);
        }

    }

}
