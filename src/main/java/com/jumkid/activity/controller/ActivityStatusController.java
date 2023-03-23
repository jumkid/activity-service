package com.jumkid.activity.controller;

import com.jumkid.activity.enums.ActivityStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/activity/statuses")
public class ActivityStatusController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('GUEST_ROLE', 'USER_ROLE', 'ADMIN_ROLE')")
    public ActivityStatus[] get() {
        return ActivityStatus.values();
    }

}
