package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ContentResource;
import com.jumkid.activity.model.ContentResourceEntity;
import org.mapstruct.*;

@Mapper(componentModel="spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityContentResourceMapper {

    @BeforeMapping
    default void setParent(ContentResourceEntity entity, @Context MapperContext ctx){
        entity.setActivityEntity(ctx.getActivityEntity());
    }

    @Mapping(target="activityId", source="entity.activityEntity.id")
    ContentResource entityToDTO(ContentResourceEntity entity, @Context MapperContext ctx);

    ContentResourceEntity dtoToEntity(ContentResource dto, @Context MapperContext ctx);
}
