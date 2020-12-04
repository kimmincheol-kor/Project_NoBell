package com.nobell.owner.activity.office;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nobell.owner.R;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.Statistics;

import org.json.JSONException;

public class StatActivity extends AppCompatActivity {

    Statistics myStat;

    TextView stat_age, stat_time, stat_rotate, stat_again;
    LinearLayout layout_age, layout_time, layout_rotate, layout_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        stat_age = (TextView) findViewById(R.id.stat_age);
        stat_time = (TextView) findViewById(R.id.stat_time);
        stat_rotate = (TextView) findViewById(R.id.stat_rotate);
        stat_again = (TextView) findViewById(R.id.stat_again);

        layout_age = (LinearLayout) findViewById(R.id.layout_age);
        layout_time = (LinearLayout) findViewById(R.id.layout_time);
        layout_rotate = (LinearLayout) findViewById(R.id.layout_rotate);
        layout_again = (LinearLayout) findViewById(R.id.layout_again);

        /////////////// Get Statistics ///////////////
        HttpConnector StatConnector = new HttpConnector();
        StatConnector.ConnectServer("", "/stat", "GET");
        try {
            myStat = new Statistics(StatConnector.HttpResult);

            stat_age.setText(myStat.mostAge);
            stat_time.setText(myStat.mostTime) ;
            stat_rotate.setText(myStat.mostRotate);
            stat_again.setText(myStat.mostAgain);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ///////////////////////////////////////////

        /////////////// Set OnClick ///////////////
        layout_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatActivity.this, DetailActivity.class);
                intent.putExtra("type", "age");
                intent.putExtra("data", myStat.detailAge);
                startActivity(intent);
            }
        });

        layout_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatActivity.this, DetailActivity.class);
                intent.putExtra("type", "time");
                intent.putExtra("data", myStat.detailTime);
                startActivity(intent);
            }
        });

//        layout_rotate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(StatActivity.this, DetailActivity.class);
//                intent.putExtra("type", "rotate");
//                intent.putExtra("data", myStat.detailRotate);
//                startActivity(intent);
//            }
//        });
//
//        layout_again.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(StatActivity.this, DetailActivity.class);
//                intent.putExtra("type", "again");
//                intent.putExtra("data", myStat.detailAgain);
//                startActivity(intent);
//            }
//        });
        ///////////////////////////////////////////

    }
}