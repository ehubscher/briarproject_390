package org.briarproject.bramble.restClient.ServerObj;

import org.briarproject.bramble.restClient.BriarServerService;
import static org.briarproject.bramble.restClient.BriarServerService.retrofit;

/**
 * Created by Winterhart on 3/10/2018.
 * Simple class to store info of server...
 */

public class ServerConfig {

    private static ServerConfig Config;
    private static BriarServerService serverService = null;
    private final String serverSecret = "qwerty";
    private final String baseUrl = "http://159.203.34.239:8080/";

    private ServerConfig(){}
    public String getServerPassword() {
        return serverSecret;
    }

    public static synchronized ServerConfig getServerConfig(){
        if(Config == null){
            Config = new ServerConfig();
        }
        return Config;
    }

    public static BriarServerService getServerService(){
        if(serverService == null){
            serverService = retrofit.create(BriarServerService.class);
        }
        return serverService;
    }

    public String getServerSecret(){
        return serverSecret;
    }

    public String getServerAddress(){
        return baseUrl;
    }
}
