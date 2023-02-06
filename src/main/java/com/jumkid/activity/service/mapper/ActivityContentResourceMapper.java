package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ContentResource;
import com.jumkid.activity.model.ContentResourceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface ActivityContentResourceMapper {

    @Mapping(target="activityId", source="entity.activityEntity.id")
    ContentResource entityToDTO(ContentResourceEntity entity);

    ContentResourceEntity dtoToEntity(ContentResource dto);
}
