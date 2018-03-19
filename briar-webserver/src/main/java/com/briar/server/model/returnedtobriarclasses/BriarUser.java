package com.briar.server.model.returnedtobriarclasses;

public class BriarUser {
    private String userName;
    private String ip;
    private int port;

    public BriarUser(String userName, String ip, int port) {
        this.userName = userName;
        this.ip = ip;
        this.port = port;
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

    @Override
    public String toString() {
        return "BriarUser{" +
                "userName='" + userName + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
