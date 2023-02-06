package com.jumkid.activity.service.mapper;

import com.jumkid.activity.model.ActivityEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class MapperContext {

    @Getter
    @Setter
    private ActivityEntity activityEntity;

}
