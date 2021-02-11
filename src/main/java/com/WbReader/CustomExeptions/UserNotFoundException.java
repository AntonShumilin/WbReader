package com.WbReader.CustomExeptions;

public class UserNotFoundException extends CustomException{

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
