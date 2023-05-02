package com.jumkid.activity.service;

import com.jumkid.activity.controller.dto.ContentResource;
import com.jumkid.activity.exception.ActivityNotFoundException;
import com.jumkid.activity.exception.ContentResourceNotFoundException;

public interface ActivityContentResourceService {

    ContentResource save(ContentResource contentResource) throws ActivityNotFoundException;

    void delete(Long id) throws ContentResourceNotFoundException;

}