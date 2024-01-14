package com.jumkid.activity.controller;

import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.exception.ActivityNotFoundException;
import com.jumkid.activity.service.ActivityService;
import com.jumkid.share.controller.response.CommonResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('USER_ROLE', 'ADMIN_ROLE')")
    public List<Activity> getAll(@RequestParam(required = false)
                                            @Pattern(regexp = "^\\S+$", message = "invalid entity id")
                                                    String entityId,
                                        @RequestParam(required = false)
                                            @Pattern(regexp="^\\S+$", message = "invalid entity name")
                                                String entityName) {
        if (entityId == null && entityName == null) return activityService.getUserActivities();
        else return activityService.getEntityLinkedActivities(entityId, entityName);
    }

    @GetMapping("{activityId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('USER_ROLE', 'ADMIN_ROLE')" +
            " && (@activityAccessAuthorizer.isOwner(#activityId) || @activityAccessAuthorizer.isAssignee(#activityId))")
    public Activity get(@PathVariable long activityId) throws ActivityNotFoundException {
        return activityService.getActivity(activityId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('USER_ROLE', 'ADMIN_ROLE')")
    public Activity create(@NotNull @Valid @RequestBody Activity activity) {
        log.debug("add new activity");
        return activityService.addActivity(activity);
    }

    @PutMapping(value = "{activityId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('USER_ROLE', 'ADMIN_ROLE')" +
            " && @activityAccessAuthorizer.isOwner(#activityId)")
    public Activity update(@PathVariable long activityId,
                           @NotNull @RequestBody Activity partialActivity) throws ActivityNotFoundException {
        log.debug("update existing activity with id {}", activityId);
        return activityService.updateActivity(activityId, partialActivity);
    }

    @DeleteMapping(value = "{activityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('USER_ROLE', 'ADMIN_ROLE')" +
            " && @activityAccessAuthorizer.isOwner(#activityId)")
    public CommonResponse delete(@PathVariable long activityId) throws ActivityNotFoundException {
        log.debug("delete activity by id {}", activityId);
        Integer count = activityService.deleteActivity(activityId);
        return CommonResponse.builder().success(count == 1).data(String.valueOf(activityId)).build();
    }

}
