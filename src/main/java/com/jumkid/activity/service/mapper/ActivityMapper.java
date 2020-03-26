package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.ActivityNotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring", uses = {PriorityMapper.class})
public class ActivityMapper {

    public Activity entityToDTO(ActivityEntity entity) {
        Activity dto = Activity.builder()
                .activityId(entity.getActivityId())
                .name(entity.getName())
                .status(entity.getStatus())
                .description(entity.getDescription())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .creationDate(entity.getCreationDate())
                .createdBy(entity.getCreatedBy())
                .modificationDate(entity.getModificationDate())
                .modifiedBy(entity.getModifiedBy())
                .build();

        if (entity.getPriorityEntity() != null) dto.setPriority(PriorityMapper.INSTANCE.entityToDTO(entity.getPriorityEntity()));

        if (entity.getActivityNotificationEntity() != null)
            dto.setActivityNotification(ActivityNotificationMapper.INSTANCE.entityToDTO(entity.getActivityNotificationEntity()));

        return dto;
    }

    public ActivityEntity dtoToEntity(Activity dto) {
        ActivityEntity entity = ActivityEntity.builder()
                .activityId(dto.getActivityId())
                .name(dto.getName())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .creationDate(dto.getCreationDate())
                .createdBy(dto.getCreatedBy())
                .modificationDate(dto.getModificationDate())
                .modifiedBy(dto.getModifiedBy())
                .build();

        if (dto.getPriority() != null) entity.setPriorityEntity(PriorityMapper.INSTANCE.dtoToEntity(dto.getPriority()));

        if (dto.getActivityNotification() != null) {
            ActivityNotificationEntity activityNotificationEntity = ActivityNotificationMapper.INSTANCE.dtoToEntity(dto.getActivityNotification());
            activityNotificationEntity.setActivityEntity(entity);
            entity.setActivityNotificationEntity(activityNotificationEntity);
        }


        return entity;
    }

}
