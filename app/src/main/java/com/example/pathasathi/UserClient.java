package com.example.pathasathi;

import android.app.Application;

import com.example.pathasathi.model.Users;
import com.google.firebase.firestore.auth.User;

public class UserClient extends Application {

    private Users users = null;

    public Users getUser() {
        return users;
    }

    public void setUser(Users users) {
        this.users = users;
    }
}
