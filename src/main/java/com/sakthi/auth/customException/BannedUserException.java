package com.sakthi.auth.customException;

public class BannedUserException extends Exception{
    public BannedUserException(String error){
        super(error);
    }
}
