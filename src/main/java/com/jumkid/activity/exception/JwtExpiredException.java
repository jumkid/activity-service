package com.jumkid.activity.exception;

public class JwtExpiredException extends RuntimeException{

    private static final String ERROR = "Jwt token is expired!";

    public JwtExpiredException(){ super(ERROR); }

}
