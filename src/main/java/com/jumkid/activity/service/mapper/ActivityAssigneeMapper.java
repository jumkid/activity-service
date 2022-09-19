package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ActivityAssignee;
import com.jumkid.activity.model.ActivityAssigneeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ActivityAssigneeMapper {

    ActivityAssignee entityToDTO(ActivityAssigneeEntity entity);

    ActivityAssigneeEntity dtoToEntity(ActivityAssignee dto);

}
