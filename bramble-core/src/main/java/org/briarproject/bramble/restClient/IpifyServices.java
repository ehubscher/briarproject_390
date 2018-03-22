package org.briarproject.bramble.restClient;

import java.util.logging.Logger;

/**
 * Created by Winterhart on 3/15/2018.
 * This class contains methods that are calling the IpifyServices
 */

public class IpifyServices {

    private static final Logger LOG =
            Logger.getLogger(IpifyServices.class.getName());
    /**
     * This function has been added to get the public IP of a device
     * This method has been taken from: https://www.ipify.org/
     * @return public ip of device
     */
    public static String getPublicIpOfDevice(){
        String publicIpV4 = "";
        try{
            java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A");
            publicIpV4 = s.next();
        }catch (Exception ee){
            LOG.info("PROBLEM WHILE getPublicIpOfDevice  "  + ee.getMessage());
        }finally {
            return publicIpV4;
        }
    }
}