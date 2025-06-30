package com.example.demo1.BuyerView;

public class User {
    private String username;
    private String asuId;
    private String password;
    private String role;

    public User(String username, String asuId, String password, String role) {
        this.username = username;
        this.asuId = asuId;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAsuId() {
        return asuId;
    }

    public void setAsuId(String asuId) {
        this.asuId = asuId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
