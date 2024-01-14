package com.jumkid.activity.service;

import com.jumkid.activity.TestSetup;
import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.exception.ActivityNotFoundException;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.repository.ActivityRepository;
import com.jumkid.activity.service.mapper.ActivityMapper;
import com.jumkid.activity.service.mapper.MapperContext;
import com.jumkid.share.event.ActivityEvent;
import com.jumkid.share.user.UserProfileManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:10092", "port=10092" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityServiceTest {

    @Autowired
    ActivityMapper activityMapper;

    private ActivityService activityService;

    @BeforeAll
    void setUp(@Mock KafkaTemplate<String, ActivityEvent> kafkaTemplateForActivity,
               @Mock ActivityRepository activityRepository,
               @Mock ActivityContentResourceService activityContentResourceService,
               @Mock UserProfileManager userProfileManager,
               @Mock MapperContext mapperContext) {
        activityService = new ActivityServiceImpl(kafkaTemplateForActivity, activityRepository,
                activityContentResourceService, userProfileManager, activityMapper, mapperContext);

        Activity activity = TestSetup.buildActivity();
        ActivityEntity activityEntity = activityMapper.dtoToEntity(activity, mapperContext);

        when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activityEntity));
    }

    @Test
    void givenActivityId_shouldGetActivity() throws ActivityNotFoundException {
        //given
        long activityId = 0L;
        //when
        Activity activity = activityService.getActivity(activityId);
        assertTrue(true);
    }

}
