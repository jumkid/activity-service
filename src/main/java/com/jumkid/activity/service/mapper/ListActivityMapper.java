package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.model.ActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel="spring")
public interface ListActivityMapper {

    default List<Activity> entitiesToDTOS(List<ActivityEntity> entities) {

        final ActivityMapper activityMapper = Mappers.getMapper( ActivityMapper.class );

        if (entities != null && !entities.isEmpty()) {
            return entities.stream()
                    .map(activityMapper::entityToDTO)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
