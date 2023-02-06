package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ActivityEntityLink;
import com.jumkid.activity.model.ActivityEntityLinkEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityEntityLinkMapper {

    @BeforeMapping
    default void setParent(ActivityEntityLinkEntity entity, @Context MapperContext ctx){
        entity.setActivityEntity(ctx.getActivityEntity());
    }

    @Mapping(target="activityId", source="entity.activityEntity.id")
    ActivityEntityLink entityToDto(ActivityEntityLinkEntity entity, @Context MapperContext ctx);

    ActivityEntityLinkEntity dtoToEntity(ActivityEntityLink dto);
}
