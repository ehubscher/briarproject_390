package com.briar.server.model.returnedtobriarclasses;

public class BriarUser {
    private String userName;
    private String ip;
    private int port;
    private int statusId;
    private int avatarId;

    public BriarUser(String userName, String ip, int port, int statusId,
                     int avatarId) {
        this.userName = userName;
        this.ip = ip;
        this.port = port;
        this.statusId = statusId;
        this.avatarId = avatarId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public int getAvatarId() {
        return avatarId;
    }

    @Override
    public String toString() {
        return "BriarUser{" +
                "userName='" + userName + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", statusId=" + statusId +
                ", avatarId=" + avatarId +
                '}';
    }
}
