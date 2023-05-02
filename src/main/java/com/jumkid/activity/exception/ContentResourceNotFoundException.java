package com.jumkid.activity.exception;

public class ContentResourceNotFoundException extends RuntimeException{

    private static final String ERROR = "Can not find activity content resource with Id: ";

    public ContentResourceNotFoundException(Long contentResourceId) { super(ERROR + contentResourceId); }
}
