package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ActivityAssignee;
import com.jumkid.activity.model.ActivityAssigneeEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {ActivityMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityAssigneeMapper {

    @BeforeMapping
    default void setParent(ActivityAssigneeEntity entity, @Context MapperContext ctx){
        entity.setActivityEntity(ctx.getActivityEntity());
    }

    @Mapping(target="activityId", source="entity.activityEntity.id")
    ActivityAssignee entityToDTO(ActivityAssigneeEntity entity, @Context MapperContext ctx);

    ActivityAssigneeEntity dtoToEntity(ActivityAssignee dto, @Context MapperContext ctx);

}
