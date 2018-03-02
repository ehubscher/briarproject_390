package org.briarproject.bramble.restClient;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



import static org.briarproject.bramble.restClient.OpenWeatherService.retrofit;

/**
 * Created by Winterhart on 3/2/2018.
 * This class is made to test a possible implementation of REST API Client using
 * Retrofit2 packages...
 */

public class SampleUserOpenWeatherService {

    // Include Basic LOG Function
    private static final Logger LOG =
            Logger.getLogger(SampleUserOpenWeatherService.class.getName());
    // This is the current API KEY for our API OpenWeather
    private String API_K = "d69d59e8368405b7199d52047e088431";
    private String location = "94040,us";
    public  volatile static  String resultFromQuery = "";


    OpenWeatherService service = retrofit.create(OpenWeatherService.class);

    /**
     * This method is getting data from the OpenWeather API
     * The method is running in a non-synchronous threading way
     * Therefore, resultFromQuery value is set after the method is executed...
     */
    public void execute() {
        service.WeatherNow(location, API_K).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Let's try to write the response from API in a LOG
                if(response.body() != null) resultFromQuery = response.body().toString();
                LOG.info("FROM OPEN WEATHER :   " + resultFromQuery);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                resultFromQuery = "FAILURE";
            }
        });




    }


}
