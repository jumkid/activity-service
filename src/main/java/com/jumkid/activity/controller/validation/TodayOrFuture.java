package com.jumkid.activity.controller.validation;

import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Documented
public @interface TodayOrFuture {
    String message() default "datetime value must be today or future date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
