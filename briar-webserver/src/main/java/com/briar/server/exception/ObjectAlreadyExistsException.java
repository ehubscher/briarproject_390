package com.briar.server.exception;

public class ObjectAlreadyExistsException extends Exception {

    public ObjectAlreadyExistsException() { super("The object you're trying to create already exists!"); }
    public ObjectAlreadyExistsException(String message) { super(message); }
    public ObjectAlreadyExistsException(String message, Throwable cause) { super(message, cause); }
    public ObjectAlreadyExistsException(Throwable cause) { super(cause); }
}
