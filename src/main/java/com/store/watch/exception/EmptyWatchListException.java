package com.store.watch.exception;

public class EmptyWatchListException extends RuntimeException {
    public EmptyWatchListException(String message) {
        super(message);
    }
}