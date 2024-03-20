package com.jumkid.activity.service;

import com.jumkid.activity.TestObjectsBuilder;
import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.exception.ActivityNotFoundException;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.repository.ActivityNotificationRepository;
import com.jumkid.activity.repository.ActivityRepository;
import com.jumkid.activity.repository.ContentResourceRepository;
import com.jumkid.activity.repository.PriorityRepository;
import com.jumkid.activity.service.mapper.ActivityMapper;
import com.jumkid.activity.service.mapper.MapperContext;
import com.jumkid.share.event.ActivityEvent;
import com.jumkid.share.user.UserProfileManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration, " +
                "org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration, " +
                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:10092", "port=10092" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityServiceTest{
    @Autowired
    ActivityMapper activityMapper;
    @MockBean
    ActivityContentResourceService activityContentResourceService;
    @MockBean
    ActivityRepository activityRepository;
    @MockBean
    ActivityNotificationRepository activityNotificationRepository;
    @MockBean
    PriorityRepository priorityRepository;
    @MockBean
    ContentResourceRepository contentResourceRepository;
    @MockBean
    MapperContext mapperContext;
    @MockBean
    UserProfileManager userProfileManager;

    private ActivityService activityService;

    @BeforeAll
    void setUp(@Autowired KafkaTemplate<String, ActivityEvent> kafkaTemplateForActivity) {
        activityService = new ActivityServiceImpl(kafkaTemplateForActivity, activityRepository,
                activityContentResourceService, userProfileManager, activityMapper, mapperContext);
    }

    @Test
    void givenActivityId_shouldGetActivity() throws ActivityNotFoundException {
        //given
        Activity activity = TestObjectsBuilder.buildActivity();
        Long activityId = activity.getId();
        ActivityEntity activityEntity = activityMapper.dtoToEntity(activity, mapperContext);
        //when
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activityEntity));
        Activity _activity = activityService.getActivity(activityId);
        //then
        assertEquals(activityId, _activity.getId());
    }

}
