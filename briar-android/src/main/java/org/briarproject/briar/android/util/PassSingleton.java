package org.briarproject.briar.android.util;

/**
 * Created by Winterhart on 3/31/2018.
 * Temporaly store password do to transaction
 */

public class PassSingleton {
    private static String password;
    public static void setPassword(String pass){
        password = pass;
    }
    public static String getPassword(){
        return password;
    }
}
