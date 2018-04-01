package com.briar.server.services.tasks;

import com.briar.server.exception.DBException;
import com.briar.server.exception.ObjectAlreadyExistsException;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.handler.UserHandler;
import com.briar.server.mapper.UserMapper;
import com.briar.server.model.domainmodelclasses.User;

public class DeleteUser extends AbstractUserTask {

    public DeleteUser(User userToDelete, UserHandler handler,
                      UserMapper mapper) {
        super(userToDelete, handler, mapper);
    }

    @Override
    public void commitDB() throws DBException {
        try {
            this.userMapper.removeUser(this.user);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void commitIdentityMap()
            throws ObjectDeletedException, ObjectAlreadyExistsException {
        this.handler.remove();
    }

    @Override
    public void revertDB() throws DBException {
        //TODO Implement the revert algo. Probably need to compile a list of things that are going to be modified by
        // the commit and revert them afterwards.
    }

    @Override
    public void revertIdentityMap()
            throws ObjectDeletedException, ObjectAlreadyExistsException {
        //TODO Implement the revert algo. Probably need to compile a list of things that are going to be modified by
        // the commit and revert them afterwards.
    }
}
