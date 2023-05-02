package com.jumkid.activity.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.time.LocalDateTime;

public class ValidDateComparisonValidator implements ConstraintValidator<ValidDateComparison, Object> {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String first;
    private String second;
    // you are able to add more operator indicators to support different forms of comparison here
    private boolean greaterThen;
    private boolean greaterThenOrEquals;

    @Override
    public void initialize(ValidDateComparison constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
        this.greaterThen = constraintAnnotation.greaterThen();
        this.greaterThenOrEquals = constraintAnnotation.greaterThenOrEquals();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        LocalDateTime firstDate = (LocalDateTime) PARSER.parseExpression(first).getValue(object);
        LocalDateTime secondDate = (LocalDateTime) PARSER.parseExpression(second).getValue(object);
        if (firstDate == null || secondDate == null) {
            // it is valid if any one of date fields is not presented
            return true;
        }
        if (greaterThen) {
            return firstDate.compareTo(secondDate) > 0;
        }
        if (greaterThenOrEquals) {
            return firstDate.compareTo(secondDate) >= 0;
        }
        return false;
    }

}
