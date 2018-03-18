package com.briar.server.exception;

public class DataCompromisedException extends Exception {
    public DataCompromisedException() { super("Something went wrong when trying to modify data on the database. Couldn't revert back the changes"); }
    public DataCompromisedException(String message) { super(message); }
    public DataCompromisedException(String message, Throwable cause) { super(message, cause); }
    public DataCompromisedException(Throwable cause) { super(cause); }
}
