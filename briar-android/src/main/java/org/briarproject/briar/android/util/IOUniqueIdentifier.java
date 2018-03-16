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
    public String getUniqueID(){
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

}
