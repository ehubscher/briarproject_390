package org.briarproject.bramble.restClient;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * Created by Winterhart on 3/15/2018.
 * This class is testing the Ipify Services
 */

public class IpifyServicesTest {

    /**
     * This test is checking if the API is returning a String (IP address) in correct format
     */
    @Test
    public void testIpifyServices(){
        // This pattern has been taken from https://www.regextester.com
        String pattern = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        String returnedIp = IpifyServices.getPublicIpOfDevice();
        boolean result = returnedIp.matches(pattern);
        // Let's change this test so we are sure it is failing,
        // Then we will modify Travis to Send info on the failing test
        Assert.assertTrue(false);
    }
}
