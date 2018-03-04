package org.briarproject.bramble.restClient;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Winterhart on 3/3/2018.
 * Testing the current implementation of Briar Server with Retrofit2
 */

public class SampleUserBriarServerServiceTest {

    SampleUserBriarServerService sampleUserBriarServerService = new SampleUserBriarServerService();
    /**
     * This TEST is built to test the retrofit2 lib. with Briar SERVER API
     */
    @Test
    public void ObtainInfoForUserIDTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                sampleUserBriarServerService.ObtainInfoForUserID("1233345");
            }
        });
        try{
            // Let's wait 2 seconds for .execute() to be completed...
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        }catch (InterruptedException ee){ee.printStackTrace();}
        String resultFromAPI =  sampleUserBriarServerService.ResultFromBriar;
        Assert.assertTrue((resultFromAPI != null && !resultFromAPI.isEmpty()));
    }

    @Test
    public void CreateNewUserTest(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                sampleUserBriarServerService.CreateNewUser("ee28dj83rhh3qa2n3d84" ,"123.345.657.123", 2232);
            }
        });
        try{
            // Let's wait 2 seconds for .execute() to be completed...
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        }catch (InterruptedException ee){ee.printStackTrace();}
        String resultFromAPI =  sampleUserBriarServerService.ResultFromBriar;
        Assert.assertTrue((resultFromAPI != null && !resultFromAPI.isEmpty()));
    }
}
