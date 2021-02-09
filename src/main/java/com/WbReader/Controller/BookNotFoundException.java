package com.WbReader.Controller;

import java.util.function.Supplier;

public class BookNotFoundException extends Exception implements Supplier<BookNotFoundException> {

    public BookNotFoundException() {
    }

    public BookNotFoundException(String message) {
        super(message);
    }

    @Override
    public BookNotFoundException get() {
        return new BookNotFoundException();
    }
}
