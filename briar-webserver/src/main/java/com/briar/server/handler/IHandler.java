package com.briar.server.handler;

import com.briar.server.exception.ObjectAlreadyExistsException;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.exception.UserContactDoesntExistsException;

public interface IHandler {

    public boolean exists() throws ObjectDeletedException, ObjectAlreadyExistsException, UserContactDoesntExistsException;

    public void add() throws ObjectDeletedException, ObjectAlreadyExistsException, UserContactDoesntExistsException;

    public void modify() throws ObjectDeletedException, ObjectAlreadyExistsException, UserContactDoesntExistsException;

    public void remove() throws ObjectDeletedException, ObjectAlreadyExistsException, UserContactDoesntExistsException;
}
