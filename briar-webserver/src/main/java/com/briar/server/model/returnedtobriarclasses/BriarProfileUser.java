package com.briar.server.model.returnedtobriarclasses;

import com.briar.server.model.domainmodelclasses.User;

public class BriarProfileUser {

    private String userName;
    private int statusId;
    private int avatarId;

    public BriarProfileUser(User user) {
        this.userName = user.getPhoneGeneratedId();
        this.statusId = user.getStatusId();
        this.avatarId = user.getAvatarId();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
