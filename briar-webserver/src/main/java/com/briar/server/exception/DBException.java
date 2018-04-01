package com.briar.server.exception;

public class DBException extends Exception {

    public DBException() { super("Something went wrong when trying to modify data on the database. Rolling back changes"); }
    public DBException(String message) { super(message); }
    public DBException(String message, Throwable cause) { super(message, cause); }
    public DBException(Throwable cause) { super(cause); }
}
