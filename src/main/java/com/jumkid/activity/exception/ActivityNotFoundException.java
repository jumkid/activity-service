package com.jumkid.activity.exception;

public class ActivityNotFoundException extends RuntimeException{

    private static final String ERROR = "Can not find activity with Id: ";

    public ActivityNotFoundException() { super(ERROR + null); }

    public ActivityNotFoundException(long activityId) { super(ERROR + activityId); }

}