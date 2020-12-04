package com.jumkid.activity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NotifyTimeUnit {

    MINUTE("minute"), HOUR("hour"), DAY("day");

    NotifyTimeUnit(String value) {
        this.value = value;
    }

    @JsonValue
    private final String value;

    public String value() { return this.value; }

}
