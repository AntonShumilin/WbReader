package com.WbReader.Controller;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ControllerAdvice
public class CustomExceptionHandler {

    @Autowired
    Logger LOGGER;

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(BookNotFoundException.class)
    public String handleBookNotFoundException(Throwable t, Model model) {
        LOGGER.error(t.getMessage(), t);
        return "error";
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IOException.class, XmlException.class})
    public String handleIOException(Throwable t, Model model) {
        LOGGER.error(t.getMessage(), t);
        return "error";
    }
}
