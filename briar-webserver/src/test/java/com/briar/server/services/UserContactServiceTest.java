package com.briar.server.services;

import com.briar.server.constants.Constants;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.exception.UserContactDoesntExistsException;
import com.briar.server.mapper.UserContactMapper;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.model.domainmodelclasses.UserContact;
import com.briar.server.model.domainmodelclasses.UserContacts;
import com.briar.server.model.returnedtobriarclasses.BriarUser;
import com.briar.server.patterns.identitymap.UserContactsIdentityMap;
import com.briar.server.patterns.unitofwork.UnitOfWork;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserContactServiceTest {
    private User user;
    private UnitOfWork unitOfWork;
    private UserContactsIdentityMap userContactsIdentityMap;
    private UserContactMapper userContactMapper;
    private UserService userService;
    private UserContactService userContactService;
    private String userName;
    private UserContact user1;
    private UserContact user2;
    private UserContact otherUser1;
    private UserContact otherUser2;
    private List<UserContact> contactList;

    @Before
    public void setup() {
        this.userContactMapper = mock(UserContactMapper.class);
        this.userContactsIdentityMap = UserContactsIdentityMap.getTestInstance();
        this.unitOfWork = mock(UnitOfWork.class);
        long id = 123;
        String phoneGeneratedId = "hello";
        this.userName = phoneGeneratedId;
        String password = "qwerty";
        String ipAddress = "123.123.123.123";
        int portNumber = 1234;
        int statusId = 2;
        int avatarId = 12;
        this.user = new User(id, phoneGeneratedId, password, ipAddress,
                portNumber, statusId, avatarId);
        this.userService = mock(UserService.class);
        this.userContactService = new UserContactService(this.userContactsIdentityMap, this.unitOfWork, this.userService, this.userContactMapper);

        user1 = new UserContact(1, phoneGeneratedId, id, true, "second user 1", 111, true);
        user2 = new UserContact(2, phoneGeneratedId, id, false, "second user 2", 222, true);
        otherUser1 = new UserContact(3, "second user 3", 333, true, phoneGeneratedId, id, true);
        otherUser2 = new UserContact(4, "second user 4", 444, false, phoneGeneratedId, id, true);

        contactList = new ArrayList<>();
        contactList.add(user1);
        contactList.add(user2);
        contactList.add(otherUser1);
        contactList.add(otherUser2);

        try {
            when(userService.readUser(userName)).thenReturn(user);
            when(userContactMapper.findContacts(id)).thenReturn(contactList);
        } catch (ObjectDeletedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testEmptyIdentityMapGettingPopulatedWithObjectsFromDatabase() throws ObjectDeletedException, UserContactDoesntExistsException {
        this.userContactService.updateUserIdentityMapWithDB(user.getPhoneGeneratedId());

        UserContacts userContacts1 = this.userContactsIdentityMap.getUserContacts(user.getPhoneGeneratedId(), Constants.Lock.reading);
        UserContacts userContacts2 = this.userContactsIdentityMap.getUserContacts("second user 1", Constants.Lock.reading);
        UserContacts userContacts3 = this.userContactsIdentityMap.getUserContacts("second user 2", Constants.Lock.reading);
        UserContacts userContacts4 = this.userContactsIdentityMap.getUserContacts("second user 3", Constants.Lock.reading);
        UserContacts userContacts5 = this.userContactsIdentityMap.getUserContacts("second user 4", Constants.Lock.reading);

        Assert.assertEquals(user1, userContacts1.getUserContact("second user 1"));
        Assert.assertEquals(user2, userContacts1.getUserContact("second user 2"));
        Assert.assertEquals(otherUser1, userContacts1.getUserContact("second user 3"));
        Assert.assertEquals(otherUser2, userContacts1.getUserContact("second user 4"));

        Assert.assertEquals(user1, userContacts2.getUserContact(user.getPhoneGeneratedId()));
        Assert.assertEquals(user2, userContacts3.getUserContact(user.getPhoneGeneratedId()));
        Assert.assertEquals(otherUser1, userContacts4.getUserContact(user.getPhoneGeneratedId()));
        Assert.assertEquals(otherUser2, userContacts5.getUserContact(user.getPhoneGeneratedId()));
    }

    @Test
    public void testPartialIdentityMapGettingPopulatedWithObjectsFromDatabase() throws ObjectDeletedException, UserContactDoesntExistsException {
        //Created clone users and change the information of the first 2 users
        UserContact testUserContact1 = this.user1.clone();
        testUserContact1.setFirstUserContactAcceptance(false);
        UserContact testUserContact2 = this.user2.clone();
        testUserContact2.setFirstUserContactAcceptance(true);

        UserContacts userContacts = new UserContacts();

        //Added local users contacts to identityMap of current user
        userContacts.addContact(testUserContact1.getOtherUser(user.getPhoneGeneratedId()), testUserContact1);
        userContacts.addContact(testUserContact2.getOtherUser(user.getPhoneGeneratedId()), testUserContact2);

        this.userContactsIdentityMap.addUserContacts(user.getPhoneGeneratedId(), userContacts);

        this.userContactService.updateUserIdentityMapWithDB(user.getPhoneGeneratedId());

        UserContacts userContacts1 = this.userContactsIdentityMap.getUserContacts(user.getPhoneGeneratedId(), Constants.Lock.reading);
        UserContacts userContacts2 = this.userContactsIdentityMap.getUserContacts("second user 1", Constants.Lock.reading);
        UserContacts userContacts3 = this.userContactsIdentityMap.getUserContacts("second user 2", Constants.Lock.reading);
        UserContacts userContacts4 = this.userContactsIdentityMap.getUserContacts("second user 3", Constants.Lock.reading);
        UserContacts userContacts5 = this.userContactsIdentityMap.getUserContacts("second user 4", Constants.Lock.reading);

        Assert.assertEquals(this.user1, userContacts1.getUserContact("second user 1"));
        Assert.assertEquals(this.user2, userContacts1.getUserContact("second user 2"));
        Assert.assertEquals(otherUser1, userContacts1.getUserContact("second user 3"));
        Assert.assertEquals(otherUser2, userContacts1.getUserContact("second user 4"));

        Assert.assertEquals(this.user1, userContacts2.getUserContact(user.getPhoneGeneratedId()));
        Assert.assertEquals(this.user2, userContacts3.getUserContact(user.getPhoneGeneratedId()));
        Assert.assertEquals(otherUser1, userContacts4.getUserContact(user.getPhoneGeneratedId()));
        Assert.assertEquals(otherUser2, userContacts5.getUserContact(user.getPhoneGeneratedId()));

        Assert.assertTrue(this.user1.equals(testUserContact1));
        Assert.assertTrue(this.user2.equals(testUserContact2));
    }

    @Test
    public void testFullIdentityMapGettingPopulatedWithObjectsFromDatabase() throws ObjectDeletedException, UserContactDoesntExistsException {
        //Created clone users and change the information of the first 2 users
        UserContact testUserContact1 = this.user1.clone();
        testUserContact1.setFirstUserContactAcceptance(false);
        UserContact testUserContact2 = this.user2.clone();
        testUserContact2.setFirstUserContactAcceptance(true);
        UserContact testUserContact3 = this.otherUser1.clone();
        UserContact testUserContact4 = this.otherUser2.clone();

        UserContacts userContacts = new UserContacts();

        //Added all users contacts to identityMap of current user
        userContacts.addContact(testUserContact1.getOtherUser(user.getPhoneGeneratedId()), testUserContact1);
        userContacts.addContact(testUserContact2.getOtherUser(user.getPhoneGeneratedId()), testUserContact2);
        userContacts.addContact(testUserContact3.getOtherUser(user.getPhoneGeneratedId()), testUserContact3);
        userContacts.addContact(testUserContact4.getOtherUser(user.getPhoneGeneratedId()), testUserContact4);

        this.userContactsIdentityMap.addUserContacts(user.getPhoneGeneratedId(), userContacts);

        this.userContactService.updateUserIdentityMapWithDB(user.getPhoneGeneratedId());

        UserContacts userContacts1 = this.userContactsIdentityMap.getUserContacts(user.getPhoneGeneratedId(), Constants.Lock.reading);
        UserContacts userContacts2 = this.userContactsIdentityMap.getUserContacts("second user 1", Constants.Lock.reading);
        UserContacts userContacts3 = this.userContactsIdentityMap.getUserContacts("second user 2", Constants.Lock.reading);
        UserContacts userContacts4 = this.userContactsIdentityMap.getUserContacts("second user 3", Constants.Lock.reading);
        UserContacts userContacts5 = this.userContactsIdentityMap.getUserContacts("second user 4", Constants.Lock.reading);

        Assert.assertEquals(this.user1, userContacts1.getUserContact("second user 1"));
        Assert.assertEquals(this.user2, userContacts1.getUserContact("second user 2"));
        Assert.assertEquals(otherUser1, userContacts1.getUserContact("second user 3"));
        Assert.assertEquals(otherUser2, userContacts1.getUserContact("second user 4"));

        Assert.assertEquals(this.user1, userContacts2.getUserContact(user.getPhoneGeneratedId()));
        Assert.assertEquals(this.user2, userContacts3.getUserContact(user.getPhoneGeneratedId()));
        Assert.assertEquals(otherUser1, userContacts4.getUserContact(user.getPhoneGeneratedId()));
        Assert.assertEquals(otherUser2, userContacts5.getUserContact(user.getPhoneGeneratedId()));

        Assert.assertTrue(this.user1.equals(testUserContact1));
        Assert.assertTrue(this.user2.equals(testUserContact2));
    }

    @Test
    public void testReturnBriarUser() throws ObjectDeletedException, UserContactDoesntExistsException {
        BriarUser bUser1 = new BriarUser("user 1", "111.222.333.444", 123, 1,1);
        BriarUser bUser2 = new BriarUser("user 2", "111.333.222.444", 121, 1,0);

        List<BriarUser> briarUserList = new ArrayList<>();
        briarUserList.add(bUser1);
        briarUserList.add(bUser2);

        long id1 = 321;
        String phoneGeneratedId1 = "hello2";
        String password1 = "qwerty";
        String ipAddress1 = "123.321.123.321";
        int portNumber1 = 1234;
        int statusId1 = 2;
        int avatarId1 = 10;
        User testUser1 = new User(id1, phoneGeneratedId1, password1, ipAddress1,
                portNumber1, statusId1, avatarId1);

        long id2 = 132;
        String phoneGeneratedId2 = "hello3";
        String password2 = "qwerty";
        String ipAddress2 = "321.123.321.123";
        int portNumber2 = 1234;
        int statusId2 = 1;
        int avatarId2 = 9;
        User testUser2 = new User(id2, phoneGeneratedId2, password2, ipAddress2,
                portNumber2, statusId2, avatarId2);

        when(this.userService.readUser(testUser1.getPhoneGeneratedId())).thenReturn(testUser1);
        when(this.userService.readUser(testUser2.getPhoneGeneratedId())).thenReturn(testUser2);

        when(this.userService.convertUserToBriarUser(testUser1)).thenReturn(bUser1);
        when(this.userService.convertUserToBriarUser(testUser2)).thenReturn(bUser2);

        this.userContactService.getUpdatedContactList(user.getPhoneGeneratedId());

        Assert.assertEquals(bUser1, briarUserList.get(0));
        Assert.assertEquals(bUser2, briarUserList.get(1));
    }

    @Test
    public void testContactNotInIdentityMapButInDB() throws ObjectDeletedException {
        Assert.assertEquals(user1, this.userContactService.readUserContact(user.getPhoneGeneratedId(), "second user 1"));
    }

    @Test
    public void testContactInIdentityMapAndInDB() throws ObjectDeletedException {
        UserContacts testUserContacts = new UserContacts();
        testUserContacts.addContact("second user 1", user1);

        this.userContactsIdentityMap.addUserContacts(user.getPhoneGeneratedId(), testUserContacts);

        Assert.assertEquals(user1, this.userContactService.readUserContact(user.getPhoneGeneratedId(), "second user 1"));
    }

    @Test
    public void testContactDosentExist() throws ObjectDeletedException {
        Assert.assertEquals(null, this.userContactService.readUserContact(user.getPhoneGeneratedId(), "Doesn't Exist"));
    }
}
