package com.jumkid.activity.controller;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.share.controller.response.CommonResponse;
import com.jumkid.activity.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('admin')")
    public List<Activity> getActivities() {
        return activityService.getActivities();
    }

    @GetMapping("{activityId}")
    @ResponseStatus(HttpStatus.OK)
    public Activity getActivity(@PathVariable long activityId) {
        return activityService.getActivity(activityId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Activity addActivity(@NotNull @Valid @RequestBody Activity activity) {
        log.debug("add new activity");
        return activityService.addActivity(activity);
    }

    @PutMapping(value = "{activityId}")
    @ResponseStatus(HttpStatus.OK)
    public Activity updateActivity(@PathVariable long activityId,
                                   @NotNull @Valid @RequestBody Activity activity) {
        log.debug("update existing activity with id {}", activityId);
        return activityService.updateActivity(activityId, activity);
    }

    @DeleteMapping(value = "{activityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CommonResponse deleteActivity(@PathVariable long activityId) {
        log.debug("delete activity by id {}", activityId);
        activityService.deleteActivity(activityId);
        return CommonResponse.builder().success(true).data(String.valueOf(activityId)).build();
    }

}
