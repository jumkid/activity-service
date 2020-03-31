package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ActivityAssignee;
import com.jumkid.activity.model.ActivityAssigneeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface ActivityAssigneeMapper {
    ActivityAssigneeMapper INSTANCE = Mappers.getMapper( ActivityAssigneeMapper.class );

    public ActivityAssignee entityToDTO(ActivityAssigneeEntity entity);

    public ActivityAssigneeEntity dtoToEntity(ActivityAssignee dto);

}
