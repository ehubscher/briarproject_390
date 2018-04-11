package com.briar.server.patterns.identitymap;

import com.briar.server.constants.Constants;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.model.domainmodelclasses.UserContacts;
import lombok.NonNull;

public class UserContactsIdentityMap {

    /////////////////////////////////////SETUP VARIABLES AND SINGLETON ////////////////////////////////

    private GenericIdentityMap<String, UserContacts> userContactsMap;

    private static volatile UserContactsIdentityMap instance;
    private static Object mutex = new Object();

    public static UserContactsIdentityMap getInstance() {
        UserContactsIdentityMap result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = new UserContactsIdentityMap();
                    result = instance;
            }
        }
        return result;
    }

    public static UserContactsIdentityMap getTestInstance() {
        return new UserContactsIdentityMap();
    }

    private UserContactsIdentityMap() {
        this.userContactsMap = new GenericIdentityMap();
    }

    //////////////////////////FORWARDING CALLS WITH APPROPRIATE LEVEL OF ABSTRACTION/////////////////////

    public boolean doesUserContactsExists(@NonNull String userName) {
        return this.userContactsMap.doesPayloadExists(userName);
    }

    public void addUserContacts(@NonNull String userName, @NonNull UserContacts userContacts) {
        this.userContactsMap.addPayload(userName, userContacts);
    }

    public UserContacts getUserContacts(@NonNull String userName, @NonNull Constants.Lock lock) throws ObjectDeletedException {
        if (lock == Constants.Lock.reading) {
            boolean something = true;
        }
        return this.userContactsMap.getPayload(userName, lock);
    }

    public void stopReading(@NonNull String userName) {
        this.userContactsMap.stopReading(userName);
    }

    public void stopWriting(@NonNull String userName) {
        this.userContactsMap.stopWriting(userName);
    }
}
