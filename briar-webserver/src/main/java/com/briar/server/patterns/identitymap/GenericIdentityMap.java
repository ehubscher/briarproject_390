package com.briar.server.patterns.identitymap;

import com.briar.server.constants.Constants;
import com.briar.server.exception.ObjectDeletedException;
import lombok.NonNull;

import java.util.HashMap;


public class GenericIdentityMap<Keytype, Payload> {

    private HashMap<Keytype, ObjectWrapper<Payload>> identityMap;

    public GenericIdentityMap() {
        this.identityMap = new HashMap<Keytype, ObjectWrapper<Payload>>();
    }

    public boolean doesPayloadExists(@NonNull Keytype key) {
        return this.identityMap.containsKey(key);
    }

    public void addPayload(@NonNull Keytype key, @NonNull Payload payload) {
        ObjectWrapper<Payload> wrapper = new ObjectWrapper(payload);
        this.identityMap.put(key, wrapper);
    }

    public Payload getPayload(@NonNull Keytype key,
                              @NonNull Constants.Lock lock)
            throws ObjectDeletedException {
        if (!doesPayloadExists(key)) {
            throw new ObjectDeletedException();
        }
        ObjectWrapper<Payload> wrapper = this.identityMap.get(key);

        if (wrapper.isPayloadToBeDeleted()) {
            throw new ObjectDeletedException();
        }
        switch (lock) {
            case reading:
                return wrapper.startReadWriteDeleteAction(lock);
            case writing:
                return wrapper.startReadWriteDeleteAction(lock);
            case deleting:
                return startDeleting(key, wrapper, lock);
            default:
                return null;
        }
    }

    public void stopReading(@NonNull Keytype key) {
        ObjectWrapper<Payload> wrapper = this.identityMap.get(key);
        wrapper.stopReading();
    }

    public void stopWriting(@NonNull Keytype key) {
        ObjectWrapper<Payload> wrapper = this.identityMap.get(key);
        wrapper.stopWriting();
    }

    private Payload startDeleting(Keytype key, ObjectWrapper<Payload> wrapper,
                                  Constants.Lock lock) {
        Payload payload = wrapper.startReadWriteDeleteAction(lock);
        this.identityMap.remove(key);
        return payload;
    }
}
