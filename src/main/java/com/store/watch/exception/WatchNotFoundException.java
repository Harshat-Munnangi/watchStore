package com.store.watch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WatchNotFoundException extends RuntimeException {
    public WatchNotFoundException(String message) {
        super(message);
    }
}

