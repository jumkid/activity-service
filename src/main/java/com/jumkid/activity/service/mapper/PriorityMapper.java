package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.Priority;
import com.jumkid.activity.model.PriorityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface PriorityMapper {

    Priority entityToDTO(PriorityEntity entity);

    PriorityEntity dtoToEntity(Priority dto);

}
