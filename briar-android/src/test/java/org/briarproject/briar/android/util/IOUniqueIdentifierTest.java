package org.briarproject.briar.android.util;
import junit.framework.Assert;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Winterhart on 3/4/2018.
 * This class is made to test most method possible contained in IOUniqueIdentifier.java
 *  We cannot test all method since their relied on having a /Documents/briar folder on an android device...
 */

public class IOUniqueIdentifierTest {

    /**
     * In this test we are trying to verify if our unique ID is random enough to never get duplicate
     * id , even if it is possible , we can reduce the risk by testing the function random id...
     */
    @Test
    public void generateUniqueIDTest(){
        boolean added = false;
        Set<String> allGeneratedUniqueID = new HashSet<>();
        int numberOfUniqueIdToCreate = 0;
        int maxIDGenerated = 10000;
        while (numberOfUniqueIdToCreate < maxIDGenerated){
            added = allGeneratedUniqueID.add(IOUniqueIdentifier.getUniqueID());
            if(!added)break;
            numberOfUniqueIdToCreate++;
        }
        Assert.assertTrue(added);
    }
}
