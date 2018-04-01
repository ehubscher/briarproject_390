package org.briarproject.bramble.restClient;

import org.briarproject.bramble.restClient.ServerObj.ServerConfig;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Winterhart on 3/3/2018.
 * This class is used to test Briar API Implementation
 */

public interface BriarServerService {


    ServerConfig config = ServerConfig.getServerConfig();
    /**
     * This method is made to get info from a user
     * @param userID -> The user ID we want to know about
     * @param bodyJSON -> The password to access to API
     * @return A JSON body with IP address, PORT and username
     */
    @Headers("Content-Type: application/json")
    @POST("/users/{currentUser}/contacts/{targetUser}")
    Call<String> obtainUserData(@Path("currentUser") String userID , @Body String bodyJSON, @Path("targetUser") String targetUser);

    /**
     * This method is creating a new user
     * @param bodyJSON -> all data needed to create a user, in JSON format
     * @return a JSON body with information confirming the creation
     */
    @Headers("Content-Type: application/json")
    @POST("user")
    Call<String> createUser(@Body String bodyJSON);

    /**
     * Updating data stored on a user on Briar Server
     * @param userID , current userID (also UniqueID)
     * @param bodyJSON , ip, port, password in request body
     * @return A JSON Body with UserName (UniqueID/UserID), ip and port
     */
    @Headers("Content-Type: application/json")
    @PUT("users/{userID}")
    Call<String> updateUserTcpData(@Path("userID") String userID, @Body String bodyJSON);

    /**
     * Updating data stored on a user on Briar Server
     * @param userID , current userID (also UniqueID)
     * @param bodyJSON , statusID and avatarID
     * @return A JSON Body with UserName (UniqueID/UserID), ip and port
     */
    @Headers("Content-Type: application/json")
    @PUT("users/{userID}/profile")
    Call<String> updateUserSettings(@Path("userID") String userID, @Body String bodyJSON);

    @Headers("Content-Type: application/json")
    @GET("users/{userID}")
    Call<String> DoesItExists(@Path("userID") String userID);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(config.getServerAddress())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}