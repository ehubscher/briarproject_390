package com.briar.server.patterns.identitymap;

import com.briar.server.constants.Constants;
import com.briar.server.exception.ObjectDeletedException;
import com.briar.server.model.domainmodelclasses.User;
import lombok.NonNull;

public class UserIdentityMap {

    /////////////////////////////////////SETUP VARIABLES AND SINGLETON ////////////////////////////////

    private static volatile UserIdentityMap instance;
    private static Object mutex = new Object();
    private GenericIdentityMap<String, User> userMap;

    private UserIdentityMap() {
        this.userMap = new GenericIdentityMap();
    }

    public static UserIdentityMap getInstance() {
        UserIdentityMap result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    instance = new UserIdentityMap();
                }
                result = instance;
            }
        }
        return result;
    }

    public static UserIdentityMap getTestInstance() {
        return new UserIdentityMap();
    }

    //////////////////////////FORWARDING CALLS WITH APPROPRIATE LEVEL OF ABSTRACTION/////////////////////

    public boolean doesUserExists(@NonNull String userName) {
        return this.userMap.doesPayloadExists(userName);
    }

    public void addUser(@NonNull String userName, @NonNull User user) {
        this.userMap.addPayload(userName, user);
    }

    public User getUser(@NonNull String userName, @NonNull Constants.Lock lock)
            throws ObjectDeletedException {
        User user = this.userMap.getPayload(userName, lock);
        if (lock == Constants.Lock.reading) {
            user = user.clone();
            stopReading(userName);
        }
        return user;
    }

    private void stopReading(@NonNull String userName) {
        this.userMap.stopReading(userName);
    }

    public void stopWriting(@NonNull String userName) {
        this.userMap.stopWriting(userName);
    }
}
