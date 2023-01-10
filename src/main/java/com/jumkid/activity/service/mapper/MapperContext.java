package com.jumkid.activity.service.mapper;

import com.jumkid.activity.model.ActivityAssigneeEntity;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.ActivityEntityLinkEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
public class MapperContext {

    private ActivityEntity activityEntity;

    @BeforeMapping
    public void setEntity(@MappingTarget ActivityEntity activityEntity) {
        this.activityEntity = activityEntity;
    }

    @AfterMapping
    public void establishRelation(@MappingTarget ActivityEntityLinkEntity activityEntityLinkEntity,
                                  @MappingTarget ActivityAssigneeEntity activityAssigneeEntity) {
        activityEntityLinkEntity.setActivityEntity( activityEntity );
        activityAssigneeEntity.setActivityEntity(activityEntity);
    }
}
