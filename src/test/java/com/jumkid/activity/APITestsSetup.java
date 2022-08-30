package com.jumkid.activity;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.controller.dto.Priority;
import com.jumkid.activity.enums.ActivityStatus;

import java.time.LocalDateTime;

class APITestsSetup {

    static long DUMMY_ID = 0L;

    static Activity buildActivity() {
        LocalDateTime now = LocalDateTime.now();
        return Activity.builder()
                .activityId(DUMMY_ID)
                .name("test activity")
                .description("This is a test activity")
                .priority(Priority.builder().priorityId(1).build())
                .status(ActivityStatus.DRAFT)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .createdBy("test")
                .creationDate(now)
                .build();
    }

}
