package org.briarproject.bramble.restClient;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.*;
import java.util.logging.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.briarproject.bramble.restClient.BriarServerService.retrofit;

/**
 * Created by Winterhart on 3/3/2018.
 * This class is made to test the current Briar Server's API
 * with Retrofit2...
 */

public class SampleUserBriarServerService {

    // Include Basic LOG Function
    private static final Logger LOG =
            Logger.getLogger(SampleUserBriarServerService.class.getName());

    public  volatile static  String ResultFromBriar = "";
    public volatile static JsonElement ObtainUserJSON = null;

    //TODO: Find a better way to hide the API Password
    private final String pass = "qwerty";



    /**
     * This method is getting data from the Briar Server API
     * The method is running in a non-synchronous threading way
     * Therefore, resultFromQuery value is set after the method is executed...
     */
    public void ObtainInfoForUserID(String userID) {
        BriarServerService service = retrofit.create(BriarServerService.class);
        JSONObject paramObj = new JSONObject();
        paramObj.put("password", pass);
        service.ObtainUserInfo(userID, paramObj.toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Let's try to write the response from API in a LOG
                if(response.body() != null){
                    ResultFromBriar = response.body().toString();
                    JsonParser parser = new JsonParser();
                    ObtainUserJSON = parser.parse(ResultFromBriar);

                }
                LOG.info("FROM BRIAR API :   " + ResultFromBriar);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LOG.info("FROM BRIAR API: FAILURE REASON:" + t.getMessage());
            }
        });
    }

    /**
     * This method is creating a new User on briar server based on :
     * @param userID Unique String created and saved on device
     * @param IP current public IP
     * @param port current available port
     */
    public void CreateNewUser(String userID, String IP, int port){
        BriarServerService service = retrofit.create(BriarServerService.class);
        JSONObject paramObj = new JSONObject();
        paramObj.put("ip", IP);
        paramObj.put("phoneGeneratedId", userID);
        paramObj.put("password", pass);
        paramObj.put("port", port);

        service.CreateUser(paramObj.toString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body() != null)ResultFromBriar = response.body().toString();
                LOG.info("FROM BRIAR API : " + ResultFromBriar);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LOG.info("FROM BRIAR API: FAILURE REASON:" + t.getMessage());
            }
        });
    }
}
