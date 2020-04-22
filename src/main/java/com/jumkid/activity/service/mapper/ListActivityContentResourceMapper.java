package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ContentResource;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.ContentResourceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel="spring")
public interface ListActivityContentResourceMapper {

    ListActivityContentResourceMapper INSTANCE = Mappers.getMapper( ListActivityContentResourceMapper.class );

    final ActivityContentResourceMapper contentResourceMapper = Mappers.getMapper( ActivityContentResourceMapper.class );

    default List<ContentResource> entitiesToDTOs(List<ContentResourceEntity> entities) {
        if (entities != null && !entities.isEmpty()) {
            return entities.stream()
                    .map(entity -> {
                        ContentResource contentResource = contentResourceMapper.entityToDTO(entity);
                        contentResource.setActivityId(entity.getActivityEntity().getActivityId());
                        return contentResource;
                    })
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    default List<ContentResourceEntity> dtosToEntities(List<ContentResource> dtos, ActivityEntity activityEntity) {
        if (dtos != null && !dtos.isEmpty()) {
            return dtos.stream()
                    .map(dto -> {
                        ContentResourceEntity contentResourceEntity = contentResourceMapper.dtoToEntity(dto);
                        contentResourceEntity.setActivityEntity(activityEntity);
                        return contentResourceEntity;
                    })
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
