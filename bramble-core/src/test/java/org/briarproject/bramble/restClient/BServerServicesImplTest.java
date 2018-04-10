package org.briarproject.bramble.restClient;


import org.briarproject.bramble.plugin.tcp.UniqueIDSingleton;
import org.briarproject.bramble.restClient.ServerObj.PwdSingletonServer;
import org.briarproject.bramble.restClient.ServerObj.SavedUser;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import java.util.Random;

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
	@Ignore
	@Test
	public void obtainUserInfoTest(){
		BServerServicesImpl services = new BServerServicesImpl();
		// Default User
        PwdSingletonServer.setPassword("123456");
        UniqueIDSingleton.setUniqueID("Bob");
		SavedUser returnedUser = services.obtainUserInfo("ABADAR");
		Assert.assertTrue(returnedUser != null);
	}

	/**
	 * This test is going to create a user in the database...
	 * The method called is returning true if creation was successful...
	 */
	@Ignore
	@Test
	public void createNewUserTest(){
		BServerServicesImpl services = new BServerServicesImpl();
		String defaultPassword = "qwerty";
		SavedUser userToCreate = new SavedUser(generateFakeUserName(), generateFakeIp(), generateFakePort(), 1, 99);
		Assert.assertTrue(services.createNewUser(userToCreate, defaultPassword));

	}

	/**
	 * Combine the two methods to verify the server ,
	 * Create and Obtain info for a user...
	 */
	@Ignore
	@Test
	public void createAndObtainDataForUserTest(){
		BServerServicesImpl services = new BServerServicesImpl();
		String username = generateFakeUserName();
		String defaultPassword = "qwerty";
		SavedUser userToCreate = new SavedUser(username, generateFakeIp(), generateFakePort(), 1,99);
		if(!services.createNewUser(userToCreate, defaultPassword)){
			Assert.fail();
		}
		SavedUser returnedUser = services.obtainUserInfo(username);
		// Check if the returned user has the data previously passed
		Assert.assertTrue(returnedUser.getUsername().equals(username));
	}

    /**
     * Test to update Avartar and Status on Server with default user Bob
     */
	@Ignore
	@Test
	public void updateUserSettingsTest(){
		BServerServicesImpl services = new BServerServicesImpl();
		String defaultUser = "Bob";
		PwdSingletonServer.setPassword("AnotherPassword");
		String ipp = generateFakeIp();
		int fakePort = generateFakePort();
		SavedUser user = new SavedUser(defaultUser, ipp, fakePort, 2,33);
		boolean e = services.updateUserSettingInfo(user);
		Assert.assertTrue(e);
	}
    /**
     * Test to update TCP Connection details with default user Bob
     */
	@Ignore
	@Test
	public void updateUserTcpTest(){
		BServerServicesImpl services = new BServerServicesImpl();
		String defaultUser = "Bob";
		PwdSingletonServer.setPassword("AnotherPassword");
		String ipp = generateFakeIp();
		int fakePort = generateFakePort();
		SavedUser user = new SavedUser(defaultUser, ipp, fakePort, 2, 22);
		boolean e = services.updateUserNetworkInfo(user);
		Assert.assertTrue(e);
	}

	/**
	 * Test the function DoesUsernameExistsInDB with default user Bob
	 */
	@Ignore
	@Test
	public void doesUserExistsTest(){
		BServerServicesImpl services = new BServerServicesImpl();
		String defaultCreatedUser  = "Bob";
		boolean result  = services.doesUsernameExistsInDB(defaultCreatedUser);
		Assert.assertTrue(result);
	}

    /**
     * Test the functino connectWithContact trying to connect Bob with ABADAR
     * the two defaults contact
     */
	@Ignore
    @Test
    public void connectWithContactTest(){
        BServerServicesImpl services = new BServerServicesImpl();
        String defaultCreateUser = "Bob";
        UniqueIDSingleton.setUniqueID(defaultCreateUser);
        PwdSingletonServer.setPassword("AnotherPassword");
        boolean result = services.connectWithContact("ABADAR");
        Assert.assertTrue(result);

    }

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