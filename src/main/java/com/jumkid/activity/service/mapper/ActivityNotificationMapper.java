package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ActivityNotification;
import com.jumkid.activity.model.ActivityNotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel="spring", uses = {ActivityMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ActivityNotificationMapper {

    ActivityNotification entityToDTO(ActivityNotificationEntity entity);

    ActivityNotificationEntity dtoToEntity(ActivityNotification dto);

}
