package org.briarproject.briar.android.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;


/**
 * Created by Winterhart on 3/3/2018.
 * Save a String to a file
 *
 */

public class IOUniqueIdentifier {
    private static final Logger LOG =
            Logger.getLogger(IOUniqueIdentifier.class.getName());
    //TODO: Find a better place to store the config file...
    private final File path =
            Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
            );
    private File fileToCreate = new File(path, "UniqueID.txt");
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * The constructor is handling the construction of the file and unique ID in it...
     */
    public IOUniqueIdentifier(){

    }
    /**
     * This method is writing the UniqueID into a file inside Android/Documents/config.txt
     * @param uniqueIdentifier A unique String ID received by the method
     * @throws IOException : may be throw
     */
    public void writeUniqueIdToFile(String uniqueIdentifier, Activity activity) throws IOException {
        // If the uniqueID doesn't exist build it
        // Prevent any too small string of being inserted...
        if(uniqueIdentifier.isEmpty() | uniqueIdentifier.length() < 9)return;
        if(!doesFileExists()){
            verifyStoragePermissions(activity);
            FileWriter fileWriter;
            try{
                fileWriter = new FileWriter(fileToCreate);
                fileWriter.append(uniqueIdentifier);
                fileWriter.close();
            }catch (IOException ee){
                LOG.info("FROM IOUniqueIdentifier " + ee.getMessage());
            }
        }
    }

    /**
     * Can be used by the test or upon removing briar (will be used in further dev.)
     */
    public void removeConfigFile(){
        if(path.exists() & fileToCreate.exists()){
            path.delete();
            LOG.info("*** WARNING *** Directory Documents/briar has been deleted");
        }
    }

    /**
     * Helper function, Check if the file exist
     * @return True is it's exist
     */
    private Boolean doesFileExists(){
        if(path.exists() & fileToCreate.exists()){
            return true;
        }
        return false;
    }

    /**
     * Getting the String inside the text file...
     * @return
     */
    public String GetUniqueID(){
        String uniqueID = "";
        BufferedReader reader;
        if(doesFileExists()){
            try{
                reader = new BufferedReader(new FileReader(fileToCreate));
                StringBuilder dataOut = new StringBuilder();
                String buffer = "";

                while ( (buffer = reader.readLine()) != null){
                    dataOut.append(buffer);
                }

                uniqueID = dataOut.toString();
                reader.close();

            }catch (IOException ee){
                LOG.info(ee.getMessage());
            }
        }
        return uniqueID;
    }

    /**
     * This method is commonly used to created unique String
     * it is based on: https://dzone.com/articles/generate-random-alpha-numeric
     * @return
     */
    public static String generateUniqueID(){
        String unique = "";
        String allPossibleChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder uniqueBuilder = new StringBuilder();
        Random ran = new Random();
        while (uniqueBuilder.length() < 20){
            int i = (int) (ran.nextFloat() * allPossibleChar.length());
            uniqueBuilder.append(allPossibleChar.charAt(i));
        }
        unique = uniqueBuilder.toString();
        return unique;

    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
