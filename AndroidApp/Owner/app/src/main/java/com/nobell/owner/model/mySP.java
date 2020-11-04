package com.nobell.owner.model;

import android.content.SharedPreferences;

public class mySP {
    public static SharedPreferences appData;
    public static SharedPreferences.Editor editor;

    public static void init() {
        editor = appData.edit();

        OwnerData.OwnerAuto = appData.getBoolean("AutoLogin", false);
        OwnerData.OwnerEmail = appData.getString("OwnerEmail", "");
        OwnerData.OwnerPw = appData.getString("OwnerPw", "");
    }

    public static  void clear() {
        // Clear Saved Login Data
        mySP.editor.putBoolean("AutoLogin", false);
        mySP.editor.putString("OwnerEmail", "");
        mySP.editor.putString("OwnerPw", "");

        mySP.editor.apply();
    }
}
