package com.wap.wapor.dto;
import com.wap.wapor.domain.UserType;

public class EmailLoginRequest {
    private UserType userType;
    private String password;
    private String identifier;

    public UserType getUserType() {
        return userType;
    }
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
