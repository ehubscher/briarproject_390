package org.briarproject.bramble.plugin.tcp;

/**
 * Created by Winterhart on 3/18/2018.
 * This class is an helper class, made to store the UniqueID of the current device
 */

public class UniqueIDSingleton {
    private static String singletonUniqueID;
    public static void setUniqueID(String uniqueID){
        singletonUniqueID = uniqueID;

}
    public static String getUniqueID(){
        return singletonUniqueID;
    }





}
