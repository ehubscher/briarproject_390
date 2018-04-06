package com.briar.server.services.tasks;

import com.briar.server.exception.DBException;
import com.briar.server.exception.ObjectAlreadyExistsException;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.exception.UserContactDoesntExistsException;
import com.briar.server.handler.UserContactHandler;
import com.briar.server.mapper.UserContactMapper;
import com.briar.server.model.domainmodelclasses.UserContact;

public class InsertNewUserContact extends AbstractUserContactTask {

    public InsertNewUserContact(UserContact userContactToAdd, UserContactHandler handler, UserContactMapper userContactMapper) {
        super(userContactToAdd, handler, userContactMapper);
    }

    @Override
    public void commitDB() throws DBException {
        try {
            userContactMapper.addNewUserContact(this.userContact);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void commitIdentityMap() throws ObjectDeletedException, ObjectAlreadyExistsException, UserContactDoesntExistsException {
        handler.add();
    }

    @Override
    public void revertDB() throws DBException {
        try {
            userContactMapper.removeSpecificUserContact(this.userContact);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void revertIdentityMap() throws ObjectDeletedException, ObjectAlreadyExistsException, UserContactDoesntExistsException {
        handler.remove();
    }
}
