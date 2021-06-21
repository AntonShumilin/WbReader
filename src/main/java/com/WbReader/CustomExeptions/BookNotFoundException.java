package com.WbReader.CustomExeptions;

public class BookNotFoundException extends CustomException{

    public BookNotFoundException() {
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
