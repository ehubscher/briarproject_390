package com.briar.server.services;

import com.briar.server.constants.Constants;
import com.briar.server.exception.DataCompromisedException;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.handler.UserHandler;
import com.briar.server.mapper.UserMapper;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.model.returnedtobriarclasses.BriarUser;
import com.briar.server.patterns.identitymap.UserIdentityMap;
import com.briar.server.patterns.unitofwork.UnitOfWork;
import com.briar.server.services.tasks.DeleteUser;
import com.briar.server.services.tasks.InsertNewUser;
import com.briar.server.services.tasks.ModifyUser;

public class UserService extends AbstractService<User> {

    private UserIdentityMap userIdentityMap;

    UserMapper mapper;

    public UserService(UserMapper mapper) {
        this(UserIdentityMap.getInstance(), UnitOfWork.getInstance(), mapper);
    }

    // Parametrised constructor for testing purposes
    public UserService(UserIdentityMap userIdentityMap, UnitOfWork unitOfWork, UserMapper mapper) {
        super(unitOfWork);
        this.userIdentityMap = userIdentityMap;
        this.mapper = mapper;
    }


    public BriarUser convertUserToBriarUser(User user) {
        return new BriarUser(user.getPhoneGeneratedId(), user.getIp(), user.getPort());
    }

    public boolean validateHackUserParams(User user) {
        return user.getPhoneGeneratedId() != null && !user.getPhoneGeneratedId().equalsIgnoreCase("") &&
                user.getPassword() != null && !user.getPassword().equalsIgnoreCase("");
    }

    public boolean validateUserParams(User user) {
        return user.getPhoneGeneratedId() != null && !user.getPhoneGeneratedId().equalsIgnoreCase("") &&
                user.getPort() != 0 &&
                user.getPassword() != null && !user.getPassword().equalsIgnoreCase("") &&
                user.getIp() != null && !user.getIp().equalsIgnoreCase("");
    }

    public boolean authenticate(User user) throws ObjectDeletedException {
        String userName = user.getPhoneGeneratedId();
        User readUser = readUser(userName);
        boolean isAuthenticated = user.getPassword().equals(readUser.getPassword());
        return isAuthenticated;
    }

    public boolean doesUserExists(String userName) {
        User user = null;
        try {
            user = readUser(userName);
        } catch (ObjectDeletedException e) {
            // We don't need to do anything. The user doesn't exist anymore. Value above is null so the method will
            // return false
        }
        return user != null;
    }

    /**
     * For reading purposes only. If you need to modify the user object, use another method.
     * Returns a clone of the user found in the identity map. Modifying the clone is safe.
     *
     * @param userName
     * @return User
     */
    public User readUser(String userName) throws ObjectDeletedException {
        boolean existsInMap = this.userIdentityMap.doesUserExists(userName);
        User user;
        if (existsInMap) {
            try {
                user = this.userIdentityMap.getUser(userName, Constants.Lock.reading);
            } catch (ObjectDeletedException e) {
                user = this.mapper.findUser(userName);
                if (user == null) {
                    throw e;
                } else {
                    this.userIdentityMap.addUser(userName, user);
                }
            }
        } else {
            user = this.mapper.findUser(userName);
            if (user == null) {
                return null;
            } else {
                this.userIdentityMap.addUser(userName, user);
            }
        }
        return user;
    }

    /**
     * To add a user to the DB and the IdentityMap
     *
     * @param user
     * @throws DataCompromisedException
     */
    public BriarUser addUser(User user) throws DataCompromisedException {
        UserHandler userHandler = new UserHandler(user);
        InsertNewUser insertUserTask = new InsertNewUser(user, userHandler, mapper);
        this.commitAndPush(user, insertUserTask);
        return this.convertUserToBriarUser(user);
    }

    /**
     * To modify a user from the DB and the IdentityMap
     *
     * @param user
     * @throws ObjectDeletedException
     * @throws DataCompromisedException
     */
    public BriarUser modifyUser(User user) throws ObjectDeletedException, DataCompromisedException {
        String userName = user.getPhoneGeneratedId();
        User oldUser = readUser(userName);
        UserHandler userHandler = new UserHandler(user);
        UserHandler oldUserHandler = new UserHandler(oldUser);
        ModifyUser modifyUserTask = new ModifyUser(user, oldUser, userHandler, oldUserHandler, mapper);
        this.commitAndPush(user, modifyUserTask);
        return this.convertUserToBriarUser(user);
    }

    /**
     * To remove a user from the DB and the IdentityMap
     * NOTE: The code this method relies on isn't implemented at the moment.
     *
     * @param user
     * @throws DataCompromisedException
     */
    public BriarUser removeUser(User user) throws DataCompromisedException {
        UserHandler userHandler = new UserHandler(user);
        DeleteUser removeUserTask = new DeleteUser(user, userHandler, mapper);
        this.commitAndPush(user, removeUserTask);
        return this.convertUserToBriarUser(user);
    }


}
