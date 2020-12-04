package com.jumkid.activity;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.controller.dto.Priority;
import com.jumkid.activity.enums.ActivityStatus;

import java.time.LocalDateTime;

class TestsSetup {

    static long DUMMY_ID = -1L;

    static Activity buildActivity() {
        LocalDateTime now = LocalDateTime.now();
        return Activity.builder()
                .activityId(DUMMY_ID).name("test")
                .description("test activity")
                .priority(Priority.builder().priorityId(1).build())
                .status(ActivityStatus.DRAFT)
                .startDate(now)
                .endDate(now.plusDays(1))
                .createdBy("system")
                .creationDate(now)
                .build();
    }

}
