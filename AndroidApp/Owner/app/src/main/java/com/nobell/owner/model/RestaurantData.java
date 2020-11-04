package com.nobell.owner.model;

import org.json.JSONException;
import org.json.JSONObject;

public class RestaurantData {

    public static int rs_id;
    public static String name;
    public static String phone;
    public static String address;

    public static String intro;

    public static String open;
    public static String close;

    public static int state = 0;

    public static void clearRs() {
        rs_id = -1;
        name = "";
        phone = "";
        address = "";
        intro = "";
        open = "";
        close = "";
        state = 0;
    }

    public static void updateRs() {
        try {
            HttpConnector conn = new HttpConnector();
            conn.ConnectServer("", "/restaurant", "GET");

            String httpCode = conn.HttpResCode;
            if (httpCode.equals("200")) {
                String httpResult = conn.HttpResult;

                JSONObject json_user = new JSONObject(httpResult);

                rs_id = json_user.getInt("rs_id");
                name = json_user.getString("rs_name").toString().trim();
                phone = json_user.getString("rs_phone").toString().trim();
                address = json_user.getString("rs_address").toString().trim();
                intro = json_user.getString("rs_intro").toString().trim();
                open = json_user.getString("rs_open").toString().trim();
                close = json_user.getString("rs_close").toString().trim();
                state = json_user.getInt("rs_state");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
