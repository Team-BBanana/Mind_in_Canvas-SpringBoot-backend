package com.example.Mind_in_Canvas.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExternalServerException extends RuntimeException {
    public ExternalServerException(String message) {
        super(message);
    }
}