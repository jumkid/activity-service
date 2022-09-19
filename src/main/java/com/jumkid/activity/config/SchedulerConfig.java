package com.jumkid.activity.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.controller.dto.ActivityAssignee;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.ActivityNotificationEntity;
import com.jumkid.activity.repository.ActivityNotificationRepository;
import com.jumkid.activity.service.mapper.ActivityMapper;
import com.jumkid.share.user.UserProfile;
import com.jumkid.share.user.UserProfileManager;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Value("${kafka.topic.activity.notification.name}")
    private String topic;

    private final ActivityNotificationRepository activityNotificationRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final UserProfileManager userProfileManager;

    private static final ActivityMapper activityMapper = Mappers.getMapper( ActivityMapper.class );

    @Autowired
    public SchedulerConfig(ActivityNotificationRepository activityNotificationRepository,
                           KafkaTemplate<String, String> kafkaTemplate, UserProfileManager userProfileManager) {
        this.activityNotificationRepository = activityNotificationRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.userProfileManager = userProfileManager;
    }

    @Async
    @Scheduled(cron = "0 * * * * ?")  //trigger every minute
    @Transactional
    public void scheduleActivityNotification() {
        LocalDateTime now = LocalDateTime.now();
        log.debug("Pick up effective activity notifications before {}", now);

        List<ActivityNotificationEntity> activeNotifications =
                activityNotificationRepository.findByExpiredAndTriggerDatetimeBefore(false, now);
        if (!activeNotifications.isEmpty()) {
            log.info("Found total {} active notifications", activeNotifications.size());
            for (ActivityNotificationEntity activityNotificationEntity : activeNotifications) {
                ActivityEntity activityEntity = activityNotificationEntity.getActivityEntity();

                try {
                    long notificationId = activityEntity.getActivityNotificationEntity().getActivityNotificationId();

                    sendNotificationEvent(activityEntity);

                    expireActivityNotification(notificationId);
                } catch (Exception e) {
                    log.error("failed to send notification event: {}", e.getMessage());
                }

            }
        }
    }

    private void sendNotificationEvent(ActivityEntity activityEntity) {
        Activity activity = activityMapper.entityToDTO(activityEntity);
        ObjectMapper objMapper = new ObjectMapper();
        try {
            List<ActivityAssignee> assignees = activity.getActivityAssignees();
            for (ActivityAssignee assignee : assignees) {
                UserProfile userProfile = userProfileManager.fetchUserProfile(assignee.getAssigneeId(), null);
                assignee.setAssigneeName(userProfile.getUsername());
                assignee.setAssigneeEmail(userProfile.getEmail());
            }

            activity.setActivityNotification(null); //don't send notification data
            kafkaTemplate.send(topic, objMapper.writeValueAsString(activity));

            log.info("activity notification for activity id {} event is sent", activity.getActivityId());
        } catch (JsonProcessingException jpe) {
            log.error("failed to publish message to kafka event {}", jpe.getMessage());
        }
    }

    private void expireActivityNotification(long notificationId) {
        Optional<ActivityNotificationEntity> optional = activityNotificationRepository.findById(notificationId);
        if (optional.isPresent()) {
            ActivityNotificationEntity activityNotificationEntity = optional.get();
            activityNotificationEntity.setExpired(true);

            activityNotificationRepository.save(activityNotificationEntity);
        }
    }

}
