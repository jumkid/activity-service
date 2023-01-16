package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ActivityEntityLink;
import com.jumkid.activity.model.ActivityEntityLinkEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = {ActivityMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityEntityLinkMapper {

    @Mapping(target="activityId", source="entity.activityEntity.activityId")
    ActivityEntityLink entityToDto(ActivityEntityLinkEntity entity, @Context MapperContext ctx);

    @Mapping(target = "activityEntity", ignore = true)
    ActivityEntityLinkEntity dtoToEntity(ActivityEntityLink dto, @Context MapperContext ctx);
}
