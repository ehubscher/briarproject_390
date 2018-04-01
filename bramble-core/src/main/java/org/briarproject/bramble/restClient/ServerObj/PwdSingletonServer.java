package org.briarproject.bramble.restClient.ServerObj;

/**
 * Created by Winterhart on 3/31/2018.
 * Helper class to store the password
 */
//TODO: Find a better way to store the data
public class PwdSingletonServer {
    private static String password;
    public static void setPassword(String pwd){
        password = pwd;
    }
    public static String getPassword(){
        return password;
    }
}
