package com.example.a6175.ncukeeper.entity;

public class Data {
    private String action;
    private String username;
    private String password;
    private String ac_id;
    private String user_ip;
    private String nas_ip;
    private String user_mac;
    private String save_me;
    private String ajax;

    public Data()
    {
        action="login";
        ac_id="1";
        user_ip="";
        nas_ip="";
        user_mac="";
        save_me="";
        ajax="1";
        username="";
        password="";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
