package com.jumkid.activity.controller;

import com.jumkid.activity.controller.response.CustomErrorResponse;
import com.jumkid.activity.exception.ActivityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Calendar;

import org.springframework.http.HttpStatus;

@RestControllerAdvice
@Slf4j
public class AdviceController {

    @ExceptionHandler({ActivityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomErrorResponse handle(Exception ex) {
        log.info("Entity could not be found.", ex);
        return new CustomErrorResponse(Calendar.getInstance().getTime(), ex.getMessage());
    }

}
