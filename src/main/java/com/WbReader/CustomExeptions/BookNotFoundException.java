package com.WbReader.CustomExeptions;

import java.util.function.Supplier;

public class BookNotFoundException extends CustomException{

    public BookNotFoundException() {
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
