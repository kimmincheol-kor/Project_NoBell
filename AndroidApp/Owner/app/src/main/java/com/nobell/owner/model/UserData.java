package com.nobell.owner.model;

import android.content.SharedPreferences;

public class UserData {
    public String UserEmail;
    public String UserPwd;
    public String UserName;
    public String UserPhone;
    public int UserRsid = -1;
    public Boolean UserLogined;
    public Boolean UserAuto;

//    // Load SharedPreference : appData
//    public SharedPreferences appData;
//    public SharedPreferences.Editor editor;
//
//    // Function For SharedPreference
//    public void save() {
//        // Save User Login Data
//        this.editor.putBoolean("AutoLogin", UserAuto);
//        this.editor.putBoolean("UserLogined", UserLogined);
//        this.editor.putString("UserEmail", UserEmail);
//        this.editor.putString("UserPwd", UserPwd);
//
//        this.editor.apply();
//    }
//
//    public void load() {
//
//        clear_user();
//
//        // Get Saved Login Data
//        this.UserAuto = appData.getBoolean("AutoLogin", false);
//        this.UserLogined = appData.getBoolean("UserLogined", false);
//        this.UserEmail = appData.getString("UserEmail", "");
//        this.UserPwd = appData.getString("UserPwd", "");
//    }
//
//    public void clear() {
//        // Clear Saved Login Data
//        this.editor.putBoolean("AutoLogin", false);
//        this.editor.putBoolean("UserLogined", false);
//        this.editor.putString("UserEmail", "");
//        this.editor.putString("UserPwd", "");
//
//        clear_user();
//
//        this.editor.apply();
//    }

    public void clear_user(){
        this.UserEmail = "";
        this.UserPwd = "";
        this.UserName = "";
        this.UserPhone = "";
        this.UserRsid = -1;
        this.UserLogined = false;
        this.UserAuto = false;
    }
    ///////////////////////////////////////////////////////////////////
}
