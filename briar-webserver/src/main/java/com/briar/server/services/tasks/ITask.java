package com.briar.server.services.tasks;


import com.briar.server.exception.DBException;
import com.briar.server.exception.ObjectAlreadyExistsException;
import com.briar.server.exception.ObjectDeletedException;

public interface ITask {

    public void commitDB() throws DBException;

    public void commitIdentityMap()
            throws ObjectDeletedException, ObjectAlreadyExistsException;

    public void revertDB() throws DBException;

    public void revertIdentityMap()
            throws ObjectDeletedException, ObjectAlreadyExistsException;
}
