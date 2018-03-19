package com.briar.server.handler;

import com.briar.server.exception.ObjectAlreadyExistsException;
import com.briar.server.exception.ObjectDeletedException;

public interface IHandler {

    public boolean exists()
            throws ObjectDeletedException, ObjectAlreadyExistsException;

    public void add()
            throws ObjectDeletedException, ObjectAlreadyExistsException;

    public void modify()
            throws ObjectDeletedException, ObjectAlreadyExistsException;

    public void remove()
            throws ObjectDeletedException, ObjectAlreadyExistsException;
}
