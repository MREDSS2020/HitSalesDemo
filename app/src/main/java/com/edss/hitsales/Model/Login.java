package com.edss.hitsales.Model;

public class Login {
    String Username , Password,AppName;

    public Login(){}

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public Login(String username, String password, String appName) {
        Username = username;
        Password = password;
        AppName = appName;
    }
}
