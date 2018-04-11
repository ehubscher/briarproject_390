package com.briar.server.model.request;

public class AddContactRequest {

    private String password;
    private String targetPhoneGeneratedId;

    public AddContactRequest() {
        // Default constructor
    }

    public AddContactRequest(String password, String targetPhoneGeneratedId) {
        this.password = password;
        this.targetPhoneGeneratedId = targetPhoneGeneratedId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTargetPhoneGeneratedId() {
        return targetPhoneGeneratedId;
    }

    public void setTargetPhoneGeneratedId(String targetPhoneGeneratedId) {
        this.targetPhoneGeneratedId = targetPhoneGeneratedId;
    }

    @Override
    public String toString() {
        return "AddContactRequest{" +
                "password='" + password + '\'' +
                ", targetPhoneGeneratedId='" + targetPhoneGeneratedId + '\'' +
                '}';
    }
}
