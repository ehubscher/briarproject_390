package org.briarproject.bramble.restClient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.briarproject.bramble.restClient.ServerObj.SavedUser;
import org.briarproject.bramble.restClient.ServerObj.ServerConfig;
import org.json.JSONObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Winterhart on 3/10/2018.
 * This class is the official class that will be used by the application to send/received
 * data between Briar Server and Briar App
 */

public class BServerServicesImpl implements BServerServices{

    ServerConfig config = ServerConfig.getServerConfig();
    // These variable are used to exchange data between the threads...
    private volatile static SavedUser CreatedUser = null;
    private volatile static String resultFromQuery = null;
    private static final Logger LOG =
            Logger.getLogger(BServerServicesImpl.class.getName());
    protected BServerServicesImpl(){}
    @Override
    public SavedUser ObtainUserInfo(String userID) {

        BriarServerService service = ServerConfig.getServerService();
        JSONObject parameters =  new JSONObject();
        parameters.put("password", config.getServerPassword());
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                service.ObtainUserInfo(userID, parameters.toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        CreatedUser = null;
                        if(response.body() != null && !response.body().isEmpty()){
                            JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(response.body());
                            if(!element.isJsonNull()){
                                JsonObject obj;
                                try{
                                    obj = element.getAsJsonObject();
                                    JsonElement username = obj.get("userName");
                                    JsonElement ip = obj.get("ip");
                                    JsonElement port  = obj.get("port");

                                    String convertedUsername = username.getAsString();
                                    String convertedIP = ip.getAsString();
                                    int convertedPort = port.getAsInt();
                                    // Store info in a SavedUser object...
                                    CreatedUser = new SavedUser(convertedUsername, convertedIP, convertedPort);

                                }catch (Exception ee){
                                    LOG.info("PROBLEM WHILE EXECUTING ObtainUserInfo : " + ee.getMessage());
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        LOG.info(" BRIAR SERVER : Failure to get user Exception: " + t.getMessage());
                    }
                });
            }
        });

        // Wait for the call to server to be done...
        try{
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        }catch (InterruptedException ee){
            LOG.info(ee.getMessage());
        }

        return CreatedUser;
    }

    @Override
    public boolean CreateNewUser(SavedUser savedUser) {
        BriarServerService serv = ServerConfig.getServerService();
        resultFromQuery = null;

        JSONObject parameters = new JSONObject();
        parameters.put("ip", savedUser.getIpAddress());
        parameters.put("phoneGeneratedId", savedUser.getUsername());
        parameters.put("password", config.getServerPassword());
        parameters.put("port", savedUser.getPort());

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                serv.CreateUser(parameters.toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        resultFromQuery = response.body();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
        // Wait for the call to server to be done...
        try{
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        }catch (InterruptedException ee){
            LOG.info(ee.getMessage());
        }

        boolean DidItWorked = (resultFromQuery != null & !resultFromQuery.isEmpty());
        return DidItWorked;
    }

    @Override
    public boolean UpdateUserInfo(SavedUser savedUser) {
        return false;
    }
}
