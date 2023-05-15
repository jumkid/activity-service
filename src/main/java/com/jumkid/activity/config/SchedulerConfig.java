package com.jumkid.activity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.controller.dto.ActivityAssignee;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.ActivityNotificationEntity;
import com.jumkid.activity.repository.ActivityNotificationRepository;
import com.jumkid.activity.service.mapper.ActivityMapper;
import com.jumkid.activity.service.mapper.MapperContext;
import com.jumkid.share.event.ActivityEvent;
import com.jumkid.share.user.UserProfile;
import com.jumkid.share.user.UserProfileManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.kafka.topic.name.activity.notify}")
    private String kafkaTopicActivityNotify;

    private final ActivityNotificationRepository activityNotificationRepository;

    private final KafkaTemplate<String, ActivityEvent> kafkaTemplateForActivity;

    private final UserProfileManager userProfileManager;

    private final ActivityMapper activityMapper;
    private final MapperContext mapperContext;

    @Autowired
    public SchedulerConfig(ActivityNotificationRepository activityNotificationRepository,
                           KafkaTemplate<String, ActivityEvent> kafkaTemplateForActivity, UserProfileManager userProfileManager, ActivityMapper activityMapper, MapperContext mapperContext) {
        this.activityNotificationRepository = activityNotificationRepository;
        this.kafkaTemplateForActivity = kafkaTemplateForActivity;
        this.userProfileManager = userProfileManager;
        this.activityMapper = activityMapper;
        this.mapperContext = mapperContext;
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
                    sendNotificationEvent(activityEntity);

                    expireActivityNotification(activityEntity.getActivityNotificationEntity().getId());
                } catch (Exception e) {
                    log.error("failed to send notification event: {}", e.getMessage());
                }

            }
        }
    }

    private void sendNotificationEvent(ActivityEntity activityEntity) {
        Activity activity = activityMapper.entityToDTO(activityEntity, mapperContext);
        List<ActivityAssignee> assignees = activity.getActivityAssignees();
        for (ActivityAssignee assignee : assignees) {
            UserProfile userProfile = userProfileManager.fetchUserProfile(assignee.getAssigneeId(), null);
            if (userProfile != null) {
                assignee.setAssigneeName(userProfile.getUsername());
                assignee.setAssigneeEmail(userProfile.getEmail());
            }
        }

        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(activity);

            kafkaTemplateForActivity.send(kafkaTopicActivityNotify, ActivityEvent.builder()
                    .activityId(activity.getId())
                    .topic(kafkaTopicActivityNotify)
                    .creationDate(LocalDateTime.now())
                    .sentBy(appName)
                    .journeyId(UUID.randomUUID().toString())
                    .payload(json)
                    .build());

            activity.setActivityNotification(null);  //set null when notification is sent

            log.info("activity notification for activity id {} event is sent", activity.getId());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to send kafka event: {}", e.getMessage());
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
