package com.ieti.myapplication.model;

public class LoginWrapper {
    private String email;
    private String password;

    public LoginWrapper(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginWrapper() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "LoginWrapper[email=%s, password='%s']",
                email, password);
    }
}
