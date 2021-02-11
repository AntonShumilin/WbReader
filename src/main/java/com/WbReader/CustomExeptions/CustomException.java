package com.WbReader.CustomExeptions;

import java.util.function.Supplier;

public class CustomException extends Exception implements Supplier<CustomException> {

    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }

    @Override
    public CustomException get() {
        return new CustomException();
    }
}
