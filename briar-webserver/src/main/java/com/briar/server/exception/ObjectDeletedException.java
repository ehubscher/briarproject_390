package com.briar.server.exception;


public class ObjectDeletedException extends Exception {

    public ObjectDeletedException() {
        super("The user doesn't exist anymore");
    }

    public ObjectDeletedException(String message) {
        super(message);
    }

    public ObjectDeletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectDeletedException(Throwable cause) {
        super(cause);
    }
}
