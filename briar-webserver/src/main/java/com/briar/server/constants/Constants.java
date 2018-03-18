package com.briar.server.constants;


public class Constants {

    public enum Lock {
        reading,
        writing,
        deleting
    }

    public enum LastCommitActionSuccessful {
        database,
        identityMap
    }
}
