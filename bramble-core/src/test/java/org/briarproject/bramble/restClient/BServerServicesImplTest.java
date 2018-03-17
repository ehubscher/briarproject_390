package org.briarproject.bramble.restClient;


import org.briarproject.bramble.restClient.ServerObj.SavedUser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by Winterhart on 3/10/2018.
 * Based on previous work, this method will test all class of our current
 * Briar Server implementation
 */

public class BServerServicesImplTest {

//    /**
//     * This TEST is built to test the retrofit2 lib. with Briar SERVER API
//     * All the threading manager as been transfered to BServerServicesImpl...
//     */
//    @Test
//    public void obtainUserInfoTest(){
//        BServerServicesImpl services = new BServerServicesImpl();
//        SavedUser returnedUser = services.obtainUserInfo("1233345");
//        Assert.assertTrue(returnedUser != null);
//    }
//
//    /**
//     * This test is going to create a user in the database...
//     * The method called is returning true if creation was successful...
//     */
//    @Test
//    public void createNewUserTest(){
//        BServerServicesImpl services = new BServerServicesImpl();
//        SavedUser userToCreate = new SavedUser(generateFakeUserName(), generateFakeIp(), generateFakePort());
//        Assert.assertTrue(services.createNewUser(userToCreate));
//
//    }
//
//    /**
//     * Combine the two methods to verify the server ,
//     * Create and Obtain info for a user...
//     */
//    @Test
//    public void createAndObtainDataForUser(){
//        BServerServicesImpl services = new BServerServicesImpl();
//        String username = generateFakeUserName();
//        SavedUser userToCreate = new SavedUser(username, generateFakeIp(), generateFakePort());
//        if(!services.createNewUser(userToCreate)){
//            Assert.fail();
//        }
//        SavedUser returnedUser = services.obtainUserInfo(username);
//        // Check if the returned user has the data previously passed
//        Assert.assertTrue(returnedUser.getUsername().equals(username));
//    }
//    @Test
//    public void updateUserInfo(){
//        // This method is not yet implemented on the briar server
//    }

    private String generateFakeUserName(){
        String fakeUserName = "";
        String allPossibleChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder uniqueBuilder = new StringBuilder();
        Random ran = new Random();
        while (uniqueBuilder.length() < 20){
            int i = (int) (ran.nextFloat() * allPossibleChar.length());
            uniqueBuilder.append(allPossibleChar.charAt(i));
        }
        fakeUserName = uniqueBuilder.toString();
        return fakeUserName;
    }

    private String generateFakeIp(){
        String fakeIp  = "";
        String allPossibleNumber = "123456789";
        StringBuilder makingFakeIp = new StringBuilder();
        Random ran = new Random();
        for(int j = 0; j < 15; j++){
            if(j == 3 || j == 7 || j == 11){
                makingFakeIp.append('.');
            }else{
                int i = (int) (ran.nextFloat() * allPossibleNumber.length());
                makingFakeIp.append(allPossibleNumber.charAt(i));
            }

        }
        fakeIp = makingFakeIp.toString();
        return fakeIp;
    }

    private int generateFakePort(){
        int Port = 0000;
        String allPossibleNumber = "123456789";
        StringBuilder makingPort = new StringBuilder();
        Random ran = new Random();
        for(int j = 0; j < 4; j++){
            int i = (int) (ran.nextFloat() * allPossibleNumber.length());
            makingPort.append(allPossibleNumber.charAt(i));
        }
        Port = Integer.parseInt(makingPort.toString());
        return Port;
    }
}
