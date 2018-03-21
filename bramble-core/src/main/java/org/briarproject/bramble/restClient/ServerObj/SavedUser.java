package org.briarproject.bramble.restClient.ServerObj;

/**
 * Created by Winterhart on 3/10/2018.
 * This class is an Object class shared in Briar to Unify the
 * structure of a SavedUser on the server
 */

public class SavedUser {
    private String username;
    private String ipAddress;
    private int port = 0000;



    private int statusId = 1; // Default Value
    private int avatarId = 99; // Default Value

    public SavedUser(String username, String ip, int port, int statusId, int avatarId){
        this.username = username;
        this.ipAddress = ip;
        this.port = port;
        // This replicate the optional constructor with simplicity
        if(statusId != 1)this.statusId = statusId;
        if(avatarId != 99)this.avatarId = avatarId;
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
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }
}
