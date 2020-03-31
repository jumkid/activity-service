package com.jumkid.activity.config;

import com.jumkid.activity.model.ActivityAssigneeEntity;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.ActivityNotificationEntity;
import com.jumkid.activity.repository.ActivityNotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final ActivityNotificationRepository activityNotificationRepository;

    @Autowired
    public SchedulerConfig(ActivityNotificationRepository activityNotificationRepository) {
        this.activityNotificationRepository = activityNotificationRepository;
    }


    @Async
    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void scheduleActivityNotification() {
        LocalDateTime now = LocalDateTime.now();
        log.debug("Pick up effective activity notifications before {}", now);

        List<ActivityNotificationEntity> activeNotifications = activityNotificationRepository.findByActiveAndNotifyDatetimeBefore(true, now);
        if (!activeNotifications.isEmpty()) {
            log.info("Found total {} active notifications", activeNotifications.size());
            for (ActivityNotificationEntity activityNotificationEntity : activeNotifications) {
                ActivityEntity activityEntity = activityNotificationEntity.getActivityEntity();
                List<String> notifyReceivers = buildNotifyReceiverList(activityEntity);
                log.info("send notification to {}", String.join(", ", notifyReceivers));
            }
        }
    }

    private List<String> buildNotifyReceiverList(ActivityEntity activityEntity) {
        List<String> assigneeList = new ArrayList<>();
        for (ActivityAssigneeEntity activityAssigneeEntity : activityEntity.getActivityAssigneeEntities()) {
            assigneeList.add(activityAssigneeEntity.getAssigneeId());
        }
        return assigneeList;
    }

}
