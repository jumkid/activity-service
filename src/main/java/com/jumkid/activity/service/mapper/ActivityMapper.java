package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.model.ActivityEntity;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Mapper(componentModel="spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ActivityNotificationMapper.class,
                ActivityEntityLinkMapper.class,
                ActivityContentResourceMapper.class,
                PriorityMapper.class,
                ActivityAssigneeMapper.class})
public interface ActivityMapper {

    @BeforeMapping
    default void setContext1(ActivityEntity entity, @Context MapperContext ctx){
        ctx.setActivityEntity(entity);
    }

    @BeforeMapping
    default void setContext2(Activity partialDto, @MappingTarget ActivityEntity updateEntity,
                             @Context MapperContext ctx){ ctx.setActivityEntity(updateEntity); }

    @Mapping(target="activityNotification", source="entity.activityNotificationEntity")
    @Mapping(target="activityEntityLinks", source="entity.activityEntityLinkEntities")
    @Mapping(target="activityAssignees", source="entity.activityAssigneeEntities")
    @Mapping(target="contentResources", source="entity.contentResourceEntities")
    @Mapping(target="priority", source="entity.priorityEntity")
    Activity entityToDTO(ActivityEntity entity, @Context MapperContext ctx);

    @Mapping(target="activityNotificationEntity", source="dto.activityNotification")
    @Mapping(target="activityEntityLinkEntities", source="dto.activityEntityLinks")
    @Mapping(target="activityAssigneeEntities", source="dto.activityAssignees")
    @Mapping(target="contentResourceEntities", source="dto.contentResources")
    @Mapping(target="priorityEntity", source="dto.priority")
    ActivityEntity dtoToEntity(Activity dto, @Context MapperContext ctx);

    @Mapping(target="activityNotificationEntity", source="partialDto.activityNotification")
    @Mapping(target="priorityEntity", source="partialDto.priority")
    void updateEntityFromDto(Activity partialDto, @MappingTarget ActivityEntity updateEntity,
                             @Context MapperContext ctx);

    List<Activity> entitiesToDTOS(List<ActivityEntity> entities);
}
