package com.briar.server.model.domainmodelclasses;

public class User {

    private long id;
    private String phoneGeneratedId;
    private String password;
    private String ip;
    private int port;

    public User(long id, String phoneGeneratedId, String password, String ip,
                int port) {
        this.id = id;
        this.phoneGeneratedId = phoneGeneratedId;
        this.password = password;
        this.ip = ip;
        this.port = port;
    }

    public User(String phoneGeneratedId, String password, String ip, int port) {
        this.phoneGeneratedId = phoneGeneratedId;
        this.password = password;
        this.ip = ip;
        this.port = port;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneGeneratedId() {
        return phoneGeneratedId;
    }

    public void setPhoneGeneratedId(String phoneGeneratedId) {
        this.phoneGeneratedId = phoneGeneratedId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }

        User user = (User) o;

        if (getId() != user.getId()) {
            return false;
        }
        if (!getPhoneGeneratedId().equals(user.getPhoneGeneratedId())) {
            return false;
        }
        if (!getPassword().equals(user.getPassword())) {
            return false;
        }
        if (getIp() != null ? !getIp().equals(user.getIp()) :
                user.getIp() != null) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + getPhoneGeneratedId().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + (getIp() != null ? getIp().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phoneGeneratedId='" + phoneGeneratedId + '\'' +
                ", password='" + password + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public User clone() {
        return new User(id, phoneGeneratedId, password, ip, port);
    }

    public void copy(User user) {
        this.id = user.getId();
        this.phoneGeneratedId = user.getPhoneGeneratedId();
        this.password = user.getPassword();
        this.ip = user.getIp();
        this.port = user.getPort();
    }
}
