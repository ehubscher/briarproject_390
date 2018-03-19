package com.briar.server.services;

import com.briar.server.constants.Constants;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.mapper.UserMapper;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.model.returnedtobriarclasses.BriarUser;
import com.briar.server.patterns.identitymap.UserIdentityMap;
import com.briar.server.patterns.unitofwork.UnitOfWork;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserServiceTest {

    private User user;
    private UnitOfWork unitOfWork;
    private UserIdentityMap userIdentityMap;
    private UserMapper userMapper;
    private UserService userService;
    private String userName;

    @Before
    public void setup() {
        this.userMapper = mock(UserMapper.class);
        this.userIdentityMap = mock(UserIdentityMap.class);
        this.unitOfWork = mock(UnitOfWork.class);
        long id = 123;
        String phoneGeneratedId = "hello";
        this.userName = phoneGeneratedId;
        String password = "qwerty";
        String ipAddress = "123.123.123.123";
        int portNumber = 1234;
        this.user =
                new User(id, phoneGeneratedId, password, ipAddress, portNumber);
        this.userService =
                new UserService(userIdentityMap, unitOfWork, userMapper);
    }

    @Test
    public void characteriseBriarUserTest() {
        BriarUser briarUser =
                this.userService.convertUserToBriarUser(this.user);
        assert (briarUser.toString().equalsIgnoreCase(
                "BriarUser{userName='hello', ip='123.123.123.123', port='1234'}"));
    }

    @Test
    public void testDoesUserExistsReturnsFalseOnException()
            throws ObjectDeletedException {
        // Creating the stubs to generate the exception
        Mockito.doThrow(new ObjectDeletedException()).when(this.userIdentityMap)
               .getUser(userName, Constants.Lock.reading);
        Mockito.doReturn(true).when(this.userIdentityMap)
               .doesUserExists(userName);
        Mockito.doReturn(null).when(this.userMapper).findUser(userName);

        // Calling the tested method.
        boolean isExceptionCaughtAndHandledAsFalse =
                this.userService.doesUserExists(userName);
        assert (isExceptionCaughtAndHandledAsFalse == false);
    }

    @Test
    public void readUserHappyPathExistInMap() throws ObjectDeletedException {
        // Generating the stubs for the happy path
        Mockito.doReturn(true).when(this.userIdentityMap)
               .doesUserExists(userName);
        Mockito.doReturn(this.user).when(this.userIdentityMap)
               .getUser(userName, Constants.Lock.reading);

        User userFromMethod = this.userService.readUser(userName);
        assertEquals(this.user, userFromMethod);
    }

    @Test
    public void readUserHappyPathDoesNotExistInMapButExistsInDB()
            throws ObjectDeletedException {
        // Generating the stubs for the happy path
        Mockito.doReturn(false).when(this.userIdentityMap)
               .doesUserExists(userName);
        Mockito.doReturn(user).when(this.userMapper).findUser(userName);

        User userFromMethod = this.userService.readUser(userName);
        assertEquals(this.user, userFromMethod);
        verify(this.userIdentityMap).addUser(userName, user);
    }

    @Test
    public void readUserDoesNotExistInMapNorInDB()
            throws ObjectDeletedException {
        // Generating the stubs for the happy path
        Mockito.doReturn(false).when(this.userIdentityMap)
               .doesUserExists(userName);
        Mockito.doReturn(null).when(this.userMapper).findUser(userName);

        User userFromMethod = this.userService.readUser(userName);
        assertEquals(null, userFromMethod);
    }

    @Test
    public void testReadUserErrorRecovery() throws ObjectDeletedException {
        // Creating the stubs to go down the error recovery path of the method
        Mockito.doThrow(new ObjectDeletedException()).when(this.userIdentityMap)
               .getUser(userName, Constants.Lock.reading);
        Mockito.doReturn(true).when(this.userIdentityMap)
               .doesUserExists(userName);
        Mockito.doReturn(user).when(this.userMapper).findUser(userName);

        User userFromMethod = this.userService.readUser(userName);
        assertEquals(this.user, userFromMethod);
        verify(this.userIdentityMap).addUser(userName, user);
    }

    @Test(expected = ObjectDeletedException.class)
    public void testReadUserThrowsException() throws ObjectDeletedException {
        // Creating the stubs to generate the exception
        Mockito.doThrow(new ObjectDeletedException()).when(this.userIdentityMap)
               .getUser(userName, Constants.Lock.reading);
        Mockito.doReturn(true).when(this.userIdentityMap)
               .doesUserExists(userName);
        Mockito.doReturn(null).when(this.userMapper).findUser(userName);

        // Should provoke the exception
        this.userService.readUser(userName);
    }


}
