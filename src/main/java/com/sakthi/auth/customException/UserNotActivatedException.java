package com.sakthi.auth.customException;


public class UserNotActivatedException extends Exception{
    public UserNotActivatedException(String errorMessage) {
        super(errorMessage);
    }
}
