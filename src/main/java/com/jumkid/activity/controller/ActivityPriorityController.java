package com.jumkid.activity.controller;

import com.jumkid.activity.controller.dto.Priority;
import com.jumkid.activity.service.ActivityPriorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/activity/priorities")
public class ActivityPriorityController {

    private final ActivityPriorityService activityPriorityService;

    public ActivityPriorityController(ActivityPriorityService activityPriorityService) {
        this.activityPriorityService = activityPriorityService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('GUEST_ROLE', 'USER_ROLE', 'ADMIN_ROLE')")
    public List<Priority> get() {
        return activityPriorityService.getAllPriorities();
    }

}
