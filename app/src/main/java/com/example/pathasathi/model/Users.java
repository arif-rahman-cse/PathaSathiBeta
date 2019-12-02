package com.example.pathasathi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {

    private String name;
    private String email;
    private String user_id;
    private String username;
    private String avatar;

    public Users(String name, String email, String user_id, String username, String avatar) {

        this.name = name;
        this.email = email;
        this.user_id = user_id;
        this.username = username;
        this.avatar = avatar;
    }

    public Users() {
    }

    protected Users(Parcel in) {
        name = in.readString();
        email = in.readString();
        user_id = in.readString();
        username = in.readString();
        avatar = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static Creator<Users> getCREATOR() {
        return CREATOR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Users{" +
                "email='" + email + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(avatar);
    }
}

