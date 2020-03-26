package com.jumkid.activity.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValidDateComparisonValidator.class)
public @interface ValidDateComparison {
    String first();

    String second();

    // you are able to add more operator indicators to support variant forms of comparison here
    boolean greaterThen() default false;

    boolean greaterThenOrEquals() default false;

    String message() default "Two date fields comparison is failed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
