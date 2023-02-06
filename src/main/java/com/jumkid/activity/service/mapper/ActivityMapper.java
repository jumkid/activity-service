package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.ActivityEntityLinkEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel="spring",
        uses = {ActivityNotificationMapper.class,
                ActivityEntityLinkMapper.class,
                PriorityMapper.class,
                ActivityAssigneeMapper.class})
public interface ActivityMapper {

    @BeforeMapping
    default void setContext(ActivityEntity entity, @Context MapperContext ctx){
        ctx.setActivityEntity(entity);
    }

    @Mapping(target="activityNotification", source="entity.activityNotificationEntity")
    @Mapping(target="activityEntityLinks", source="entity.activityEntityLinkEntities")
    @Mapping(target="activityAssignees", source="entity.activityAssigneeEntities")
    @Mapping(target="priority", source="entity.priorityEntity")
    Activity entityToDTO(ActivityEntity entity, @Context MapperContext ctx);

    @Mapping(target="activityNotificationEntity", source="dto.activityNotification")
    @Mapping(target="activityEntityLinkEntities", source="dto.activityEntityLinks")
    @Mapping(target="activityAssigneeEntities", source="dto.activityAssignees")
    @Mapping(target="priorityEntity", source="dto.priority")
    ActivityEntity dtoToEntity(Activity dto, @Context MapperContext ctx);

    @Mapping(target="activityNotificationEntity", source="partialDto.activityNotification")
    @Mapping(target="activityEntityLinkEntities", source="partialDto.activityEntityLinks")
    @Mapping(target="activityAssigneeEntities", source="partialDto.activityAssignees")
    @Mapping(target="priorityEntity", source="partialDto.priority")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(Activity partialDto, @MappingTarget ActivityEntity updateEntity);

    List<Activity> entitiesToDTOS(List<ActivityEntity> entities);
}
