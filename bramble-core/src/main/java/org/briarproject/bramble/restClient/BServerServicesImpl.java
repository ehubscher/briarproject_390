package org.briarproject.bramble.restClient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.briarproject.bramble.plugin.tcp.UniqueIDSingleton;
import org.briarproject.bramble.restClient.ServerObj.PwdSingletonServer;
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
    private volatile static SavedUser createdUser = null;
    private volatile static String resultFromQueryCreateUser = null;
    private volatile static String resultFromQueryUpdateUser = null;
    private volatile static Boolean resultFromQueryExists = false;
    private int TIME_WAITING = 1;
    private static final Logger LOG =
            Logger.getLogger(BServerServicesImpl.class.getName());
    public BServerServicesImpl(){}
    @Override
    public SavedUser obtainUserInfo(String targetUserID) {

        BriarServerService service = ServerConfig.getServerService();
        JSONObject parameters =  new JSONObject();
        parameters.put("password", PwdSingletonServer.getPassword());
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                service.obtainUserData(UniqueIDSingleton.getUniqueID(), parameters.toString(), targetUserID).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        createdUser = null;
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
                                    JsonElement statusId = obj.get("statusId");
                                    JsonElement avatarId = obj.get("avatarId");

                                    String convertedUsername = username.getAsString();
                                    String convertedIP = ip.getAsString();
                                    int convertedPort = port.getAsInt();
                                    int convertedStatusId = statusId.getAsInt();
                                    int convertedAvatarId = statusId.getAsInt();
                                    // Store info in a SavedUser object...
                                    createdUser = new SavedUser(convertedUsername, convertedIP, convertedPort, convertedStatusId, convertedAvatarId);

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
            executorService.awaitTermination(TIME_WAITING, TimeUnit.SECONDS);
        }catch (InterruptedException ee){
            LOG.info(ee.getMessage());
        }

        return createdUser;
    }

    @Override
    public boolean createNewUser(SavedUser savedUser, String password) {
        BriarServerService serv = ServerConfig.getServerService();
        resultFromQueryCreateUser = null;

        JSONObject parameters = new JSONObject();
        parameters.put("port", savedUser.getPort());
        parameters.put("ip", savedUser.getIpAddress());
        parameters.put("phoneGeneratedId", savedUser.getUsername());
        parameters.put("password", password);
        parameters.put("statusId", savedUser.getStatusId());
        parameters.put("avatarId", savedUser.getAvatarId());


        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                serv.createUser(parameters.toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        resultFromQueryCreateUser = response.body();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        LOG.info(" BRIAR SERVER : Failure to create user Exception: " + t.getMessage());
                    }
                });
            }
        });
        // Wait for the call to server to be done...
        try{
            executorService.awaitTermination(TIME_WAITING, TimeUnit.SECONDS);
        }catch (Exception ee){
            LOG.info("FROM CREATE NEW USER : " +  ee.getMessage());
        }
        return (resultFromQueryCreateUser != null && !resultFromQueryCreateUser.isEmpty());
    }

    @Override
    public boolean updateUserNetworkInfo(SavedUser savedUser) {
        BriarServerService serv = ServerConfig.getServerService();
        resultFromQueryUpdateUser = null;

        JSONObject parameters = new JSONObject();
        parameters.put("port", savedUser.getPort());
        parameters.put("ip", savedUser.getIpAddress());
        parameters.put("password", PwdSingletonServer.getPassword());
        // prevent unexpected input
        if(savedUser.getUsername() == null | savedUser.getUsername().length() < 2){
            return false;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                serv.updateUserTcpData(savedUser.getUsername() ,parameters.toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        resultFromQueryUpdateUser = response.body();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        // On failure resultFromQuery will be empty...
                        LOG.info(" BRIAR SERVER : Failure to create user Exception: " + t.getMessage());
                    }
                });
            }
        });
        // Wait for the call to server to be done...
        try{
            executorService.awaitTermination(TIME_WAITING, TimeUnit.SECONDS);
        }catch (Exception ee){
            LOG.info("FROM CREATE NEW USER : " +  ee.getMessage());
        }
        return (resultFromQueryUpdateUser != null && !resultFromQueryUpdateUser.isEmpty());
    }
    @Override
    public boolean updateUserSettingInfo(SavedUser savedUser) {
        BriarServerService serv = ServerConfig.getServerService();
        resultFromQueryUpdateUser = null;

        JSONObject parameters = new JSONObject();
        parameters.put("statusId", savedUser.getStatusId());
        parameters.put("avatarId", savedUser.getAvatarId());
        parameters.put("password", PwdSingletonServer.getPassword());
        // prevent unexpected input
        if(savedUser.getUsername() == null | savedUser.getUsername().length() < 2){
            return false;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                serv.updateUserSettings(savedUser.getUsername() ,parameters.toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        resultFromQueryUpdateUser = response.body();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        // On failure resultFromQuery will be empty...
                        LOG.info(" BRIAR SERVER : Failure to create user Exception: " + t.getMessage());
                    }
                });
            }
        });
        // Wait for the call to server to be done...
        try{
            executorService.awaitTermination(TIME_WAITING, TimeUnit.SECONDS);
        }catch (Exception ee){
            LOG.info("FROM CREATE NEW USER : " +  ee.getMessage());
        }
        return (resultFromQueryUpdateUser != null && !resultFromQueryUpdateUser.isEmpty());
    }

    @Override
    public boolean DoesUsernameExistsInDB(String username) {
        BriarServerService serv = ServerConfig.getServerService();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                    serv.DoesItExists(username).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.body().toString().equals("true")){
                                resultFromQueryExists = true;
                            }else{
                                resultFromQueryExists = false;
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            resultFromQueryExists =  false;
                            LOG.info("BRIAR SERVER : IS DISABLE, RETURN FALSE DEFAULT");
                        }
                    });
            }
        });
        // Wait for the call to server to be done...
        try{
            executorService.awaitTermination(TIME_WAITING, TimeUnit.SECONDS);
        }catch (Exception ee){
            LOG.info("FROM CREATE NEW USER : " +  ee.getMessage());
        }
        return resultFromQueryExists;
    }

}