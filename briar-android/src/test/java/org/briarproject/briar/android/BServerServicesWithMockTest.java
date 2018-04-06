package org.briarproject.briar.android;

import com.google.common.base.Verify;

import org.briarproject.bramble.restClient.BServerServicesImpl;
import org.briarproject.bramble.restClient.ServerObj.SavedUser;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Winterhart on 3/25/2018.
 * This class is made to test the server using Mockito
 * and by mocking the server
 */

public class BServerServicesWithMockTest {
    /**
     * This test is testing obtaining a user, with mocking the server
     */
    @Test
    public void mockObtainUserInfoTest(){
        BServerServicesImpl mockingServer = mock(BServerServicesImpl.class);
        String username = "Robert";
        String ip = "123.123.123.123";
        int port = 2222;
        int avatar = 11;
        int status = 2;
        SavedUser user = new SavedUser(username, ip, port, status, avatar);
        // mocking what server would returned
        when(mockingServer.obtainUserInfo("Robert")).thenReturn(user);
        SavedUser mockUser = mockingServer.obtainUserInfo(username);
        Assert.assertTrue(mockUser.equals(user) && mockUser.getUsername().equals(user.getUsername()) );



    }

    /**
     * This test is testing updating Tcp, with mocking the server
     */
    @Test
    public void mockUpdateUserTcpTest(){

        BServerServicesImpl mockingServer = mock(BServerServicesImpl.class);
        String username = "Robert";
        String ip = "123.123.123.123";
        int port = 2222;
        int avatar = 11;
        int status = 2;
        SavedUser user = new SavedUser(username, ip, port, status, avatar);
        // mocking what server would returned
        when(mockingServer.obtainUserInfo("Robert")).thenReturn(user);

        SavedUser userUpdated = mockingServer.obtainUserInfo("Robert");
        userUpdated.setIpAddress("123.122.122.122");
        userUpdated.setPort(2233);

        // mocking what server would returned
        when(mockingServer.updateUserNetworkInfo(userUpdated)).thenReturn(true);
        Assert.assertTrue(mockingServer.updateUserNetworkInfo(userUpdated));
    }

    /**
     * This test is testing updating settings, with mocking the server
     */
    @Test
    public void mockUpdateUserSettingsTest(){
        BServerServicesImpl mockingServer = mock(BServerServicesImpl.class);
        String username = "Robert";
        String ip = "123.123.123.123";
        int port = 2222;
        int avatar = 11;
        int status = 2;
        SavedUser user = new SavedUser(username, ip, port, status, avatar);
        // mocking what server would returned
        when(mockingServer.obtainUserInfo("Robert")).thenReturn(user);

        SavedUser userUpdated = mockingServer.obtainUserInfo("Robert");
        userUpdated.setAvatarId(22);
        userUpdated.setStatusId(3);

        // mocking what server would returned
        when(mockingServer.updateUserSettingInfo(userUpdated)).thenReturn(true);
        Assert.assertTrue(mockingServer.updateUserSettingInfo(userUpdated));
    }

    /**
     * This test is testing creating user, with mocking the server
     */
    @Test
    public void mockCreateNewUserTest(){

        BServerServicesImpl mockingServer = mock(BServerServicesImpl.class);
        String username = "Robert";
        String ip = "123.123.123.123";
        int port = 2222;
        int avatar = 11;
        int status = 2;
        SavedUser user = new SavedUser(username, ip, port, status, avatar);
        // mocking what server would returned
        when(mockingServer.createNewUser(user)).thenReturn(true);

        Assert.assertTrue(mockingServer.createNewUser(user));

    }
}
