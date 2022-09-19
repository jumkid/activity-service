package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ContentResource;
import com.jumkid.activity.model.ContentResourceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ActivityContentResourceMapper {

    ContentResource entityToDTO(ContentResourceEntity entity);

    ContentResourceEntity dtoToEntity(ContentResource dto);
}
