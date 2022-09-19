package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.ActivityNotificationEntity;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel="spring", uses = {PriorityMapper.class, ActivityAssigneeMapper.class})
public interface ActivityMapper {

    default Activity entityToDTO(ActivityEntity entity) {
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

        if (entity.getActivityAssigneeEntities() != null && !entity.getActivityAssigneeEntities().isEmpty())
            dto.setActivityAssignees(ListActivityAssigneeMapper.INSTANCE.entitiesToDTOs(entity.getActivityAssigneeEntities()));

        if (entity.getContentResourceEntities() != null && !entity.getContentResourceEntities().isEmpty())
            dto.setContentResources(ListActivityContentResourceMapper.INSTANCE.entitiesToDTOs(entity.getContentResourceEntities()));

        return dto;
    }

    default ActivityEntity dtoToEntity(Activity dto) {
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

            //reset trigger time
            LocalDateTime startDate = entity.getStartDate();
            LocalDateTime triggerDatetime;
            switch (activityNotificationEntity.getNotifyBeforeUnit()) {
                case DAY:
                    triggerDatetime = startDate.minusDays(activityNotificationEntity.getNotifyBefore());
                    break;
                case HOUR:
                    triggerDatetime = startDate.minusHours(activityNotificationEntity.getNotifyBefore());
                    break;
                case MINUTE:
                    triggerDatetime = startDate.minusMinutes(activityNotificationEntity.getNotifyBefore());
                    break;
                default:
                    triggerDatetime = startDate;
            }
            activityNotificationEntity.setTriggerDatetime(triggerDatetime);

            entity.setActivityNotificationEntity(activityNotificationEntity);
        }

        if (dto.getActivityAssignees() != null && !dto.getActivityAssignees().isEmpty())
            entity.setActivityAssigneeEntities(ListActivityAssigneeMapper.INSTANCE.dtosToEntities(dto.getActivityAssignees(), entity));

        if (dto.getContentResources() != null && !dto.getContentResources().isEmpty())
            entity.setContentResourceEntities(ListActivityContentResourceMapper.INSTANCE.dtosToEntities(dto.getContentResources(), entity));

        return entity;
    }

}
