package com.briar.server.handler;


import com.briar.server.constants.Constants;
import com.briar.server.exception.ObjectAlreadyExistsException;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.patterns.identitymap.UserIdentityMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserHandlerTest {

    private User user;
    private UserIdentityMap map;

    @Before
    public void setup() {
        this.map = UserIdentityMap.getTestInstance();
        long id = 123;
        String phoneGeneratedId = "hello";
        String password = "qwerty";
        String ipAddress = "123.123.123.123";
        int portNumber = 1234;
        this.user = new User(id, phoneGeneratedId, password, ipAddress, portNumber);
    }

    @Test
    public void testUserExistsInMap() {
        // Setting up the map so that the user exists
        this.map.addUser(this.user.getPhoneGeneratedId(), this.user);

        // Creating the handler
        UserHandler handler = new UserHandler(user, map);
        assert(handler.exists());
    }

    @Test
    public void testUserDoesNotExistsInMap() {
        // No work is done to create a user in the map so the map is empty.

        // Creating the handler
        UserHandler handler = new UserHandler(user, map);
        assert(handler.exists() == false);
    }

    @Test
    public void testThatAddMethodPutsUserInMap() {
        // Creating the handler
        UserHandler handler = new UserHandler(user, map);

        try {
            handler.add();
        } catch (ObjectAlreadyExistsException e) {
            fail("ObjectAlreadyExistsException Exception thrown, shouldn't enter here");
        }

        try {
            assertEquals(user, this.map.getUser(user.getPhoneGeneratedId(), Constants.Lock.reading));
            assert(user.equals(this.map.getUser(user.getPhoneGeneratedId(), Constants.Lock.reading)));
        } catch (ObjectDeletedException e) {
            fail("ObjectDeletedException Exception thrown, shouldn't enter here");
        }
    }

    @Test(expected=ObjectAlreadyExistsException.class)
    public void testAddWhereExceptionIsCorrectlyThrownWhenUserAlreadyExistsInMap() throws ObjectAlreadyExistsException {
        // Setting up the map so that the user exists
        this.map.addUser(this.user.getPhoneGeneratedId(), this.user);

        // Creating the handler
        UserHandler handler = new UserHandler(user, map);

        // Should throw the exception
        handler.add();
    }

    @Test
    public void testModifyAUserInMap(){
        // Setting up the map so that the user exists
        this.map.addUser(this.user.getPhoneGeneratedId(), this.user);

        // Creating a new different user
        User differentUser = this.user.clone();
        differentUser.setIp("A different IP!!!");

        // Creating the handler
        UserHandler handler = new UserHandler(differentUser, map);

        // Should throw the exception
        try {
            handler.modify();
            User userInMap = this.map.getUser(this.user.getPhoneGeneratedId(), Constants.Lock.writing);

            // The user in map has the same content has the different user
            assert(userInMap.equals(differentUser));
            // The user in map doesn't have the same address on disk as the "different user"
            assert(userInMap != differentUser);
            // In fact, the user in map has the exact same address as the first user we put in there
            assert(userInMap == user);
        } catch (ObjectDeletedException e) {
            fail("ObjectDeletedException was wrongly thrown");
        }
    }

}
