package org.briarproject.bramble.restClient;

/**
 * Created by Winterhart on 3/2/2018.
 *  This class is made to test a possible implementation of REST API Client using
 * Retrofit2 packages...
 */



import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Query;

public interface OpenWeatherService {
    @GET("weather")
    Call<String> WeatherNow(@Query("zip") String LOCATION,
                            @Query("appid") String API_KEY);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
}
