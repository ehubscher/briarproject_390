package com.briar.server.exception;

public class UserContactDoesntExistsException extends Exception {

    public UserContactDoesntExistsException() { super("The user contact you're trying to delete doesn't exists!"); }
    public UserContactDoesntExistsException(String message) { super(message); }
    public UserContactDoesntExistsException(String message, Throwable cause) { super(message, cause); }
    public UserContactDoesntExistsException(Throwable cause) { super(cause); }
}
