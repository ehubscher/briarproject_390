package org.briarproject.bramble.plugin.tcp;

import org.briarproject.bramble.api.contact.Contact;
import org.briarproject.bramble.restClient.ServerObj.SavedUser;

import java.security.Key;
import java.util.HashMap;

/**
 * Created by Workstation on 3/19/2018.
 * This contact hash will be used to store information about current joinable user...
 */

public class ContactHash {
    private static HashMap<String, SavedUser> allCurrentContacts;
    // Block contructor
    private ContactHash(){};
    // Obtain contact hash
    public static HashMap<String, SavedUser> getAllCurrentContacts(){
        if(allCurrentContacts == null){
            allCurrentContacts = new HashMap<>();
        }
        return allCurrentContacts;
    }

}
