package com.nobell.owner.model;

import org.json.JSONException;
import org.json.JSONObject;

public class OwnerData {
    public static String OwnerEmail;
    public static String OwnerPw;
    public static String OwnerName;
    public static String OwnerPhone;
    public static String OwnerPin;
    public static int OwnerRsid = -1;
    public static Boolean OwnerAuto;

    public static void clear(){
        OwnerEmail = "";
        OwnerPw = "";
        OwnerName = "";
        OwnerPhone = "";
        OwnerPin = "";
        OwnerRsid = -1;
        OwnerAuto = false;
    }

    public static void updateInfo() {
        try {
            HttpConnector conn = new HttpConnector();
            conn.ConnectServer("", "/", "GET");

            String httpCode = conn.HttpResCode;
            if (httpCode.equals("200")) {
                String httpResult = conn.HttpResult;

                JSONObject json_user = new JSONObject(httpResult);

                OwnerEmail = json_user.getString("owner_email").toString().trim();
                OwnerPw = json_user.getString("owner_pw").toString().trim();
                OwnerName = json_user.getString("owner_name").toString().trim();
                OwnerPhone = json_user.getString("owner_phone").toString().trim();
                OwnerPin = json_user.getString("owner_pin").toString().trim();
                OwnerRsid = json_user.getInt("owner_rs_id");

                if (OwnerRsid > 0) {
                    RestaurantData.updateRs();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
