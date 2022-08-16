package com.example.pathasathi;

import android.app.Application;

import com.example.pathasathi.model.Users;

public class UserClient extends Application {

    private Users users = null;

    public Users getUser() {
        return users;
    }

    public void setUser(Users users) {
        this.users = users;
    }
}
