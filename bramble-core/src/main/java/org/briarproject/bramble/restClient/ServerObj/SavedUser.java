package org.briarproject.bramble.restClient.ServerObj;

/**
 * Created by Winterhart on 3/10/2018.
 */

public class SavedUser {
    private String Username;
    private String IpAddress;
    private int Port;

    public SavedUser(String username, String ip, int port){
        this.Username = username;
        this.IpAddress = ip;
        this.Port = port;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }
}
