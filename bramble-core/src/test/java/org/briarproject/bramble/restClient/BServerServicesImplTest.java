package org.briarproject.bramble.restClient;


import org.briarproject.bramble.restClient.ServerObj.SavedUser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Winterhart on 3/10/2018.
 * Based on previous work, this method will test all class of our current
 * Briar Server implementation
 */

public class BServerServicesImplTest {

    /**
     * This TEST is built to test the retrofit2 lib. with Briar SERVER API
     * All the threading manager as been transfered to BServerServicesImpl...
     */
    @Test
    public void  ObtainUserInfoTest(){
        BServerServicesImpl services = new
                BServerServicesImpl();
        SavedUser returnedUser = services.ObtainUserInfo("1233345");
        Assert.assertTrue(returnedUser != null);
    }

    @Test
    public void CreateNewUserTest(){

    }
    @Test
    public void UpdateUserInfo(){

    }


}
