package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ActivityAssignee;
import com.jumkid.activity.model.ActivityAssigneeEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = {ActivityMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityAssigneeMapper {

    @Mapping(target="id", source="entity.activityEntity.id")
    ActivityAssignee entityToDTO(ActivityAssigneeEntity entity);

    @Mapping(target = "activityEntity", ignore = true)
    ActivityAssigneeEntity dtoToEntity(ActivityAssignee dto, @Context MapperContext ctx);

}
