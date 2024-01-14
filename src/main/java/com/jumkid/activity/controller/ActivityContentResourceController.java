package com.jumkid.activity.controller;

import com.jumkid.activity.controller.dto.ContentResource;
import com.jumkid.activity.exception.ActivityNotFoundException;
import com.jumkid.activity.exception.ContentResourceNotFoundException;
import com.jumkid.activity.service.ActivityContentResourceService;
import com.jumkid.share.controller.response.CommonResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Validated
@RequestMapping("/activity/content")
public class ActivityContentResourceController {

    private final ActivityContentResourceService contentResourceService;

    public ActivityContentResourceController(ActivityContentResourceService contentResourceService) {
        this.contentResourceService = contentResourceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('USER_ROLE', 'ADMIN_ROLE')")
    public ContentResource add(@Valid @NotNull @RequestBody ContentResource contentResource) throws ActivityNotFoundException {
        return contentResourceService.save(contentResource);
    }

    @DeleteMapping("{contentResourceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('USER_ROLE', 'ADMIN_ROLE')" +
            " && @activityAccessAuthorizer.hasPermissionForContentResource(#contentResourceId)")
    public CommonResponse delete(@PathVariable long contentResourceId) throws ContentResourceNotFoundException {
        contentResourceService.delete(contentResourceId);
        return CommonResponse.builder().success(true).data(String.valueOf(contentResourceId)).build();
    }

}
