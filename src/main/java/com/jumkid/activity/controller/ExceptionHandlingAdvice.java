package com.jumkid.activity.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jumkid.activity.exception.ActivityNotFoundException;
import com.jumkid.share.controller.response.CustomErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Calendar;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlingAdvice {

    @ExceptionHandler({ActivityNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public CustomErrorResponse handle(Exception ex) {
        log.info("Entity could not be found.", ex);
        return new CustomErrorResponse(Calendar.getInstance().getTime(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public CustomErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("The provided argument is missing or invalid.", ex);
        return CustomErrorResponse.builder()
                .timestamp(Calendar.getInstance().getTime())
                .property(ex.getFieldErrors().stream().map(FieldError::getField).collect(Collectors.toList()))
                .details(ex.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()))
                .build();
    }

    @ExceptionHandler({InvalidFormatException.class})
    @ResponseStatus(BAD_REQUEST)
    public CustomErrorResponse handle(InvalidFormatException ex) {
        log.info("Datetime string value is invalid.", ex);
        return new CustomErrorResponse(Calendar.getInstance().getTime(), ex.getMessage());
    }

}
