package com.jumkid.activity.service;

import com.jumkid.activity.TestObjectsBuilder;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@PropertySource("classpath:application.share.properties")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:10092", "port=10092" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityServiceTest{

    @Value("${com.jumkid.jwt.test.user-id}")
    private String testUserId;

    @Autowired
    ActivityMapper activityMapper;
    @Mock
    ActivityContentResourceService activityContentResourceService;
    @Mock
    ActivityRepository activityRepository;
    @Mock
    MapperContext mapperContext;
    @Mock
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
        long activityId = 0L;
        Activity activity = TestObjectsBuilder.buildActivity(testUserId);
        ActivityEntity activityEntity = activityMapper.dtoToEntity(activity, mapperContext);
        //when
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activityEntity));
        Activity _activity = activityService.getActivity(activityId);
        //then
        assertEquals(activityId, _activity.getId());
    }

}
