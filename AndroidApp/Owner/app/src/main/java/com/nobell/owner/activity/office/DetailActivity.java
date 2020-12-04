package com.nobell.owner.activity.office;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nobell.owner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    LinearLayout layout_detail;
    TextView tv_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        layout_detail = (LinearLayout) findViewById(R.id.layout_detail);
        tv_detail = (TextView) findViewById(R.id.tv_detail);

        Intent myIntent = getIntent();
        final String type = myIntent.getStringExtra("type");
        final String data = myIntent.getStringExtra("data");
        
        if(type.equals("time")) tv_detail.setText("시간별 방문자 비율");
        else tv_detail.setText("나이별 방문자 비율");

        LinearLayout.LayoutParams layoutparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams param1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        param1.weight = 1;
        TableRow.LayoutParams param2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        param2.weight = 9;

        try {
            JSONArray jArr = new JSONArray(data);

            for(int i=0; i<jArr.length(); i++){
                JSONObject jObj = jArr.getJSONObject(i);

                 LinearLayout layout = new LinearLayout(this);
                 layout.setOrientation(LinearLayout.HORIZONTAL);

                 TextView title = new TextView(this);
                 title.setBackground(getResources().getDrawable(R.drawable.boarder));
                 title.setGravity(Gravity.CENTER);
                 title.setTextSize(20);
                 title.setText(jObj.getString("title"));

                 layout.addView(title, param1);

                 TextView content = new TextView(this);
                 content.setBackground(getResources().getDrawable(R.drawable.boarder));
                 content.setGravity(Gravity.CENTER);
                 content.setTextSize(20);
                 content.setText(jObj.getString("ratio"));

                 layout.addView(content, param2);

                 layout_detail.addView(layout, layoutparam);
             }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}