package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ActivityNotification;
import com.jumkid.activity.model.ActivityNotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface ActivityNotificationMapper {

    ActivityNotificationMapper INSTANCE = Mappers.getMapper( ActivityNotificationMapper.class );

    ActivityNotification entityToDTO(ActivityNotificationEntity entity);

    ActivityNotificationEntity dtoToEntity(ActivityNotification dto);

}
