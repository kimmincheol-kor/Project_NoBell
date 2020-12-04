package com.nobell.owner.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Statistics {

    public String mostAge, mostTime, mostRotate, mostAgain;
    public String detailAge, detailTime, detailRotate, detailAgain;

    public Statistics(String http) throws JSONException {

        JSONObject jObj = new JSONObject(http);

        JSONArray jAge = jObj.getJSONArray("age");
        JSONArray jTime = jObj.getJSONArray("time");
        JSONArray jRotate = jObj.getJSONArray("rotate");
        JSONArray jAgain = jObj.getJSONArray("again");

        JSONArray detailAge = new JSONArray();
        JSONArray detailTime = new JSONArray();
        // JSONObject detailRotate = new JSONObject();
        // JSONObject detailAgain = new JSONObject();

        // Total DATA
        int total_visit = 0;
        int total_user = 0;
        for(int i=0; i<jAgain.length(); i++){
            JSONObject jsonData = jAgain.getJSONObject(i);
            total_user ++;
            total_visit += jsonData.getInt("cnt");
        }

        // Age DATA
        int max_age = 0;
        int max_visit = 0;
        for(int i=0; i<jAge.length(); i++) {
            JSONObject jsonData = jAge.getJSONObject(i);

            int age = jsonData.getInt("age");
            int cnt = jsonData.getInt("cnt");

            if (cnt > max_visit) {
                max_age = age;
                max_visit = cnt;
            }

            JSONObject dage = new JSONObject();
            dage.put("title", age+"대");
            dage.put("ratio", (int)((float)cnt / total_user * 100) + "%" );
            detailAge.put(dage);
        }

        mostAge = max_age + "대";

        // Time
        int max_time = 0;
        max_visit = 0;

        for(int i=0; i<jTime.length(); i++) {
            JSONObject jsonData = jTime.getJSONObject(i);

            int time = jsonData.getInt("time");
            int cnt = jsonData.getInt("cnt");

            if (cnt > max_visit) {
                max_time = time;
                max_visit = cnt;
            }

            JSONObject dtime = new JSONObject();
            dtime.put("title", time + "시");
            dtime.put("ratio", (int)((float)cnt / total_visit * 100) + "%");
            detailTime.put(dtime);
        }

        mostTime = max_time + "시";

        // Rotate
        int totalSec = jRotate.getJSONObject(0).getInt("rotate");
        int min = totalSec / 60;
        int sec = totalSec % 60;
        mostRotate = min+"분 "+sec+"초";

        // Again
        int cntAgain = 0;

        for(int i=0; i<jAgain.length(); i++) {
            JSONObject jsonData = jAgain.getJSONObject(i);

            int cnt = jsonData.getInt("cnt");

            if (cnt > 1) {
                cntAgain++;
            }
        }

        mostAgain = (int)(((float)cntAgain / total_user) * 100) + "%";

        this.detailAge = detailAge.toString();
        this.detailTime = detailTime.toString();
        // this.detailRotate = detailRotate.toString();
        // this.detailAgain = detailAgain.toString();
    }
}
