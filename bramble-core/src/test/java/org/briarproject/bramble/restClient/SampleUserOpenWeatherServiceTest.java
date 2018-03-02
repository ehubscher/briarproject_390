package org.briarproject.bramble.restClient;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Winterhart on 3/2/2018.
 * This Test class has been created show how to handle the threading of Retrofit2 packages
 */

public class SampleUserOpenWeatherServiceTest {
    SampleUserOpenWeatherService sampleUserOpenWeatherService = new SampleUserOpenWeatherService();
    /**
     * This TEST is built to test the retrofit2 lib. with a real API (Open Weather)
     * When the server will be ready, we will handle HTTP Request with Retrofit2
     */
    @Test
    public void executeTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                sampleUserOpenWeatherService.execute();
            }
        });
        try{
            // Let's wait 2 seconds for .execute() to be completed...
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        }catch (InterruptedException ee){ee.printStackTrace();}
        String resultFromAPI = sampleUserOpenWeatherService.resultFromQuery;
        Assert.assertTrue((resultFromAPI != null && !resultFromAPI.isEmpty()));
    }

}
