package com.briar.server.services.tasks;

import com.briar.server.exception.DBException;
import com.briar.server.exception.ObjectAlreadyExistsException;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.handler.UserHandler;
import com.briar.server.mapper.UserMapper;
import com.briar.server.model.domainmodelclasses.User;

public class ModifyUser extends AbstractUserTask {

    private User oldUser;
    private UserHandler oldUserHandler;

    public ModifyUser(User newUser, User oldUser, UserHandler handler,
                      UserHandler oldUserHandler, UserMapper mapper) {
        super(newUser, handler, mapper);
        this.oldUser = oldUser;
        this.oldUserHandler = oldUserHandler;
    }


    @Override
    public void commitDB() throws DBException {
        try {
            userMapper.modifyUser(this.user);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void commitIdentityMap()
            throws ObjectDeletedException, ObjectAlreadyExistsException {
        this.handler.modify();
    }

    @Override
    public void revertDB() throws DBException {
        try {
            userMapper.modifyUser(this.oldUser);
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void revertIdentityMap()
            throws ObjectDeletedException, ObjectAlreadyExistsException {
        this.oldUserHandler.modify();
    }
}
