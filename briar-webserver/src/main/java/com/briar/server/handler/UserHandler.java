package com.briar.server.handler;

import com.briar.server.constants.Constants;
import com.briar.server.exception.ObjectAlreadyExistsException;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.patterns.identitymap.UserIdentityMap;

public class UserHandler implements IHandler {

    private User user;
    private UserIdentityMap map;

    public UserHandler(User user) {
        this(user, UserIdentityMap.getInstance());
    }

    // Parametrised constructor for testing purposes
    public UserHandler(User user, UserIdentityMap map) {
        this.user = user;
        this.map = map;
    }

    public boolean exists() {
        String userName = user.getPhoneGeneratedId();
        return this.map.doesUserExists(userName);
    }

    public void add() throws ObjectAlreadyExistsException {
        String userName = this.user.getPhoneGeneratedId();
        if (exists()) {
            throw new ObjectAlreadyExistsException();
        }
        this.map.addUser(userName, this.user);
    }

    public void modify() throws ObjectDeletedException {
        String userName = this.user.getPhoneGeneratedId();
        User userInMap = map.getUser(userName, Constants.Lock.writing);
        userInMap.copy(this.user);
        this.map.stopWriting(userName);
    }

    public void remove() {
        // TODO Write the code to allow for removal of user. Should be in different sprint since removal of user is not
        // part of this one. Lots of synchronization / deadlock traps with this task (removing user = remove all
        // it's contacts for both sides)
    }
}
