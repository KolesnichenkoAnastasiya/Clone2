package com.geekbrains.cloud.DataBase;

public class User {
    private String name_user;
    private String login_user;
    private String pass_user;

    public User(String name_user, String login_user, String pass_user) {
        this.name_user = name_user;
        this.login_user = login_user;
        this.pass_user = pass_user;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getLogin_user() {
        return login_user;
    }

    public void setLogin_user(String login_user) {
        this.login_user = login_user;
    }

    public String getPass_user() {
        return pass_user;
    }

    public void setPass_user(String pass_user) {
        this.pass_user = pass_user;
    }
}
