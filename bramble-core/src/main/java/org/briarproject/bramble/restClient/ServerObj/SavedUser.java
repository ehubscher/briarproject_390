package org.briarproject.bramble.restClient.ServerObj;

/**
 * Created by Winterhart on 3/10/2018.
 */

public class SavedUser {
    private String username;
    private String ipAddress;
    private int port;

    public SavedUser(String username, String ip, int port){
        this.username = username;
        this.ipAddress = ip;
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
