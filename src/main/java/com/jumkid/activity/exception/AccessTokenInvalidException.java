package com.jumkid.activity.exception;

public class AccessTokenInvalidException extends RuntimeException {

    public static final String ERROR = "Access token is invalid. Please provide a valid access token.";

    public AccessTokenInvalidException() { super(ERROR); }

}
