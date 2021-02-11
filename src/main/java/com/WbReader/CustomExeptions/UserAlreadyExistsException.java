package com.WbReader.CustomExeptions;

import java.util.function.Supplier;

public class UserAlreadyExistsException extends CustomException{

    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
