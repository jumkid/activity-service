package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.model.ActivityEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring",
        uses = {ActivityNotificationMapper.class,
                ActivityEntityLinkMapper.class,
                PriorityMapper.class,
                ActivityAssigneeMapper.class})
public interface ActivityMapper {

    @Mapping(target="activityId", source = "entity.activityId")
    @Mapping(target="activityNotification", source="entity.activityNotificationEntity")
    @Mapping(target="activityEntityLinks", source="entity.activityEntityLinkEntities")
    @Mapping(target="activityAssignees", source="entity.activityAssigneeEntities")
    Activity entityToDTO(ActivityEntity entity);

    @Mapping(target="activityNotificationEntity", source="dto.activityNotification")
    @Mapping(target="activityEntityLinkEntities", source="dto.activityEntityLinks")
    @Mapping(target="activityAssigneeEntities", source="dto.activityAssignees")
    ActivityEntity dtoToEntity(Activity dto, @Context MapperContext ctx);

    List<Activity> entitiesToDTOS(List<ActivityEntity> entities);
}
