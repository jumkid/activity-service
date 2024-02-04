package com.jumkid.activity;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.controller.dto.ActivityEntityLink;
import com.jumkid.activity.controller.dto.ActivityNotification;
import com.jumkid.activity.controller.dto.Priority;
import com.jumkid.activity.enums.ActivityStatus;
import com.jumkid.activity.enums.NotifyTimeUnit;

import java.time.LocalDateTime;
import java.util.List;

public interface TestObjectsBuilder {

    long DUMMY_ID = 0L;

    default Activity buildActivity(String userId) {
        LocalDateTime now = LocalDateTime.now();
        return Activity.builder()
                .id(DUMMY_ID)
                .name("test activity")
                .description("This is a test activity")
                .priority(Priority.builder()
                        .id(1)
                        .identifier("normal")
                        .label("Normal")
                        .build())
                .activityNotification(ActivityNotification.builder()
                        .id(1L)
                        .notifyBefore(30)
                        .notifyBeforeUnit(NotifyTimeUnit.MINUTE)
                        .expired(false)
                        .build())
                .activityEntityLinks(List.of(ActivityEntityLink.builder()
                        .entityName("content")
                        .entityId("XXX")
                        .build()))
                .status(ActivityStatus.DRAFT)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .createdBy(userId)
                .creationDate(now)
                .modificationDate(now)
                .build();
    }

}
