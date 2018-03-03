package org.briarproject.briar.android.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;

import android.content.Context;
import android.os.Environment;

/**
 * Created by Winterhart on 3/3/2018.
 * Save a String to a file
 *
 */

public class IOUniqueIdentifier {

    /**
     * This methode shall only create a file if the file doesn't already exist...
     * @param uniqueIdentifier
     */
    private static final Logger LOG =
            Logger.getLogger(IOUniqueIdentifier.class.getName());
    private final File path =
            Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS + "/briar/"
            );
    private File fileToCreate = new File(path, "config.txt");


    public void WriteUniqueIdToFile(String uniqueIdentifier) throws IOException {
        boolean SuccessCreateDir;
        if(path.exists()){
            SuccessCreateDir = true;
            if(fileToCreate.exists()){
                LOG.info("***WARNING *** Unique ID , File is already created");
                return;
            }
        }else{
            SuccessCreateDir = path.mkdir();
            LOG.info("CREATING THE FOLDER briar in Documents");
        }


        if(SuccessCreateDir){
            FileOutputStream fileOutputStream = null;
            OutputStreamWriter outputStreamWriter = null;

            try{
                boolean SuccessCreateFile = fileToCreate.createNewFile();
                if(!SuccessCreateFile){
                    LOG.info("*** WARNING *** CAN'T Create the file, config.txt in Documents/briar folder");
                    return;
                }
                fileOutputStream = new FileOutputStream(fileToCreate);
                outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                outputStreamWriter.append(uniqueIdentifier);

            }catch (IOException ee){
                LOG.info(ee.getMessage());
            }
            finally {
                if(outputStreamWriter != null)outputStreamWriter.close();
                if(fileOutputStream != null){
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }

        }

    }

    /**
     * Can be used by the test or upon removing briar
     */
    public void RemoveConfigFile(){
        if(path.exists() & fileToCreate.exists()){
            path.delete();
            LOG.info("*** WARNING *** Directory Documents/briar has been deleted");
        }
    }
}
