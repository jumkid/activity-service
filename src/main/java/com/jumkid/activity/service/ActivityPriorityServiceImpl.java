package com.jumkid.activity.service;

import com.jumkid.activity.controller.dto.Priority;
import com.jumkid.activity.repository.PriorityRepository;
import com.jumkid.activity.service.mapper.PriorityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ActivityPriorityServiceImpl implements ActivityPriorityService{

    private final PriorityRepository priorityRepository;

    private final PriorityMapper priorityMapper;

    public ActivityPriorityServiceImpl(PriorityRepository priorityRepository, PriorityMapper priorityMapper) {
        this.priorityRepository = priorityRepository;
        this.priorityMapper = priorityMapper;
    }

    @Override
    public List<Priority> getAllPriorities() {
        return priorityMapper.entitiesToDTOs(priorityRepository.findAll());
    }
}
