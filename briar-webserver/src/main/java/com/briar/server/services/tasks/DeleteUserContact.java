package com.briar.server.services.tasks;


import com.briar.server.exception.DBException;
import com.briar.server.exception.ObjectAlreadyExistsException;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.exception.UserContactDoesntExistsException;
import com.briar.server.handler.UserContactHandler;
import com.briar.server.mapper.UserContactMapper;
import com.briar.server.model.domainmodelclasses.UserContact;

public class DeleteUserContact extends AbstractUserContactTask {

    public DeleteUserContact(UserContact userContactToDelete, UserContactHandler handler, UserContactMapper mapper) {
        super(userContactToDelete, handler, mapper);
    }

    @Override
    public void commitDB() throws DBException {
        try {
            this.userContactMapper.removeSpecificUserContact(this.userContact);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void commitIdentityMap() throws ObjectDeletedException, ObjectAlreadyExistsException, UserContactDoesntExistsException {

    }

    @Override
    public void revertDB() throws DBException {
        try {
            this.userContactMapper.addNewUserContact(this.userContact);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void revertIdentityMap() throws ObjectDeletedException, ObjectAlreadyExistsException, UserContactDoesntExistsException {

    }
}
