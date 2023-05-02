package com.jumkid.activity.service;

import com.jumkid.activity.controller.dto.ContentResource;
import com.jumkid.activity.exception.ActivityNotFoundException;
import com.jumkid.activity.exception.ContentResourceNotFoundException;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.model.ContentResourceEntity;
import com.jumkid.activity.repository.ActivityRepository;
import com.jumkid.activity.repository.ContentResourceRepository;
import com.jumkid.activity.service.mapper.ActivityContentResourceMapper;
import com.jumkid.share.event.ContentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static com.jumkid.share.util.Constants.JOURNEY_ID;

@Slf4j
@Service
public class ActivityContentResourceServiceImpl implements ActivityContentResourceService{

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.kafka.topic.name.content.delete}")
    private String kafkaTopicContentDelete;

    private final ActivityRepository activityRepository;
    private final ContentResourceRepository contentResourceRepository;

    private final ActivityContentResourceMapper contentResourceMapper;

    private final KafkaTemplate<String, ContentEvent> kafkaTemplate;

    private final HttpServletRequest httpServletRequest;

    public ActivityContentResourceServiceImpl(ActivityRepository activityRepository,
                                              ContentResourceRepository contentResourceRepository,
                                              ActivityContentResourceMapper contentResourceMapper,
                                              KafkaTemplate<String, ContentEvent> kafkaTemplate,
                                              HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
        this.activityRepository = activityRepository;
        this.contentResourceRepository = contentResourceRepository;
        this.contentResourceMapper = contentResourceMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    @Override
    public ContentResource save(ContentResource contentResource) throws ActivityNotFoundException {
        Long activityId = contentResource.getActivityId();
        ActivityEntity activityEntity = activityRepository
                .findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));
        ContentResourceEntity entity = contentResourceMapper.dtoToEntity(contentResource);
        entity.setActivityEntity(activityEntity);

        entity = contentResourceRepository.save(entity);
        return contentResourceMapper.entityToDTO(entity);
    }

    @Transactional
    @Override
    public void delete(Long id) throws ContentResourceNotFoundException {
        ContentResourceEntity entity = contentResourceRepository
                .findById(id)
                .orElseThrow(() -> new ContentResourceNotFoundException(id));

        contentResourceRepository.delete(entity);

        //send event to content vault to remove physical file
        ContentEvent contentEvent = ContentEvent.builder()
                .journeyId(httpServletRequest.getHeader(JOURNEY_ID))
                .contentId(entity.getContentResourceId())
                .topic(kafkaTopicContentDelete)
                .sentBy(appName)
                .creationDate(LocalDateTime.now())
                .build();
        kafkaTemplate.send(kafkaTopicContentDelete, contentEvent);
    }
}
