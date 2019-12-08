package com.example.pathasathi;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pathasathi.util.Config;

public class MySharedPrefarance {

    private static MySharedPrefarance mySharedPrefarance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private MySharedPrefarance(Context context) {
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREFARANCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

    }

    //Creating object of private constructor for SharedPrefarance ready to data stored!
    public static MySharedPrefarance getPrefarences(Context context) {
        if (mySharedPrefarance == null) {
            mySharedPrefarance = new MySharedPrefarance(context);
        }
        return mySharedPrefarance;
    }


    //-----------------------------Set value and get value form sharedPrefarance------------------------------//

    public void setUserEmail(String userEmail) {
        editor.putString(Config.USER_EMAIL, userEmail);
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(Config.USER_EMAIL, "UserName Not Found!");
    }

    public void setPassword(String password){
        editor.putString(Config.PASSWORD, password);
        editor.apply();
    }

    public String getPassword(){
        return sharedPreferences.getString(Config.PASSWORD, "password not found");
    }
}
