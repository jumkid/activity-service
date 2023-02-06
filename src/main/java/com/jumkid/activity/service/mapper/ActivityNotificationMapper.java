package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ActivityNotification;
import com.jumkid.activity.model.ActivityNotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel="spring", uses = {ActivityMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityNotificationMapper {

    @Mapping(target="activityId", source="entity.activityEntity.id")
    ActivityNotification entityToDTO(ActivityNotificationEntity entity);

    ActivityNotificationEntity dtoToEntity(ActivityNotification dto);

}
