package com.jumkid.activity.service.mapper;

import com.jumkid.activity.controller.dto.ActivityAssignee;
import com.jumkid.activity.model.ActivityAssigneeEntity;
import com.jumkid.activity.model.ActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel="spring")
public interface ListActivityAssigneeMapper {

    ListActivityAssigneeMapper INSTANCE = Mappers.getMapper( ListActivityAssigneeMapper.class );

    final ActivityAssigneeMapper activityAssigneeMapper = Mappers.getMapper( ActivityAssigneeMapper.class );

    default List<ActivityAssignee> entitiesToDTOs(List<ActivityAssigneeEntity> entities) {
        if (entities != null && !entities.isEmpty()) {
            return entities.stream()
                    .map(entity -> {
                        ActivityAssignee activityAssignee = activityAssigneeMapper.entityToDTO(entity);
                        activityAssignee.setActivityId(entity.getActivityEntity().getActivityId());
                        return activityAssignee;
                    })
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    default List<ActivityAssigneeEntity> dtosToEntities(List<ActivityAssignee> dtos, ActivityEntity activityEntity) {
        if (dtos != null && !dtos.isEmpty()) {
            return dtos.stream()
                    .map(dto -> {
                        ActivityAssigneeEntity activityAssigneeEntity = activityAssigneeMapper.dtoToEntity(dto);
                        activityAssigneeEntity.setActivityEntity(activityEntity);
                        return activityAssigneeEntity;
                    })
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
