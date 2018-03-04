package org.briarproject.bramble.restClient;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Winterhart on 3/3/2018.
 */

public interface BriarServerService {
    String BASE_URL = "http://159.203.34.239:8080/";

    /**
     * This method is made to get info from a user
     * @param userID -> The user ID we want to know about
     * @param bodyJSON -> The password to access to API
     * @return A JSON body with IP address, PORT and username
     */
    @Headers("Content-Type: application/json")
    @POST("users/{userID}")
    Call<String> ObtainUserInfo(@Path("userID") String userID ,  @Body String bodyJSON);

    @Headers("Content-Type: application/json")
    @POST("users")
    Call<String> CreateUser(@Body String bodyJSON);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
