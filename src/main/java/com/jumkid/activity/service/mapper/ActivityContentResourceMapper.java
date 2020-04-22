package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ContentResource;
import com.jumkid.activity.model.ContentResourceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface ActivityContentResourceMapper {
    ActivityContentResourceMapper INSTANCE = Mappers.getMapper( ActivityContentResourceMapper.class );

    public ContentResource entityToDTO(ContentResourceEntity entity);

    public ContentResourceEntity dtoToEntity(ContentResource dto);
}
