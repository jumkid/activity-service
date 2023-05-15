package com.jumkid.activity.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TodayOrFutureValidator implements ConstraintValidator<TodayOrFuture, LocalDateTime> {
    @Override
    public void initialize(TodayOrFuture constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDateTime currentDate = LocalDateTime.now();
        return localDateTime.format(formatters).equals(currentDate.format(formatters))
                ||
                localDateTime.isAfter(currentDate);
    }
}
