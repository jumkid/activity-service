package com.jumkid.activity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import static java.util.Arrays.stream;

public enum ActivityStatus {

    DRAFT("draft"),
    IN_PROGRESS("in_progress"),
    PENDING("pending"),
    COMPLETED("completed");

    @JsonValue
    private final String value;

    ActivityStatus(String value) {
        this.value = value;
    }

    public static boolean hasValue(String value) {
        if (value == null) {
            return false;
        }
        return stream(ActivityStatus.values()).anyMatch(item -> item.value.equals(value));
    }

    public static ActivityStatus of(String value) {
        if (value == null) {
            return null;
        }

        for (ActivityStatus item : ActivityStatus.values()) {
            if (value.equals(item.value())) {
                return item;
            }
        }

        throw new EnumConstantNotPresentException(ActivityStatus.class, value);
    }

    public String value() {
        return value;
    }

}
