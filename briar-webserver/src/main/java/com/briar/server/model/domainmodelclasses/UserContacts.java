package com.briar.server.model.domainmodelclasses;

import com.briar.server.exception.UserContactDoesntExistsException;

import java.util.ArrayList;
import java.util.HashMap;

public class UserContacts {

    private HashMap<String, UserContact> contacts;

    public UserContacts() {
        this.contacts = new HashMap<String, UserContact>();
    }

    public boolean isEmpty() {
        return this.contacts.isEmpty();
    }

    public boolean contactExists(String userName) {
        return this.contacts.containsKey(userName);
    }

    public void addContact(String userName, UserContact userContact) {
        this.contacts.put(userName, userContact);
    }

    public UserContact getUserContact(String userName) throws UserContactDoesntExistsException {
        if (!this.contacts.containsKey(userName)) {
            throw new UserContactDoesntExistsException();
        }
        return this.contacts.get(userName);
    }

    public UserContact removeContact(String userName) throws UserContactDoesntExistsException {
        if (!this.contacts.containsKey(userName)) {
            throw new UserContactDoesntExistsException();
        }
        return this.contacts.remove(userName);
    }

    public ArrayList<String> getAllValidContacts(String userName) throws UserContactDoesntExistsException {
        if (!this.contacts.containsKey(userName)) {
            throw new UserContactDoesntExistsException();
        }
        ArrayList<String> userList = new ArrayList<String>();
        this.contacts.forEach((userAsKey, userContactAsValue) -> {
            if (userContactAsValue.isContactBilateral()) {
                userList.add(userAsKey);
            }
        });
        return userList;
    }

    public ArrayList<String> getAllContacts() {
        ArrayList<String> userList = new ArrayList<String>();
        this.contacts.forEach((userAsKey, userContactAsValue) -> {
            userList.add(userAsKey);
        });
        return userList;
    }
}
