package com.nobell.owner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nobell.owner.R;
import com.nobell.owner.activity.field.ManageTableActivity;
import com.nobell.owner.activity.field.ReserveActivity;
import com.nobell.owner.activity.field.ReserveListActivity;
import com.nobell.owner.activity.field.VisitActivity;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.RestaurantData;
import com.nobell.owner.model.UserData;

import org.json.JSONException;
import org.json.JSONObject;

public class FieldActivity extends AppCompatActivity {

    public static UserData user_data;
    public static RestaurantData rs_data;

    private TextView tv_restaurant_field;
    private Button btn_manageOrder, btn_manageTable, btn_visit, btn_reserve, btn_rsvList, btn_toOwner;
    private EditText et_pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        user_data = MainActivity.user_data;
        rs_data = MainActivity.rs_data;

        // Get View Object
        tv_restaurant_field = (TextView) findViewById(R.id.tv_restaurant_field);
        btn_manageOrder = (Button) findViewById(R.id.btn_manageOrder);
        btn_manageTable = (Button) findViewById(R.id.btn_manageTable);
        btn_visit = (Button) findViewById(R.id.btn_visit);
        btn_reserve = (Button) findViewById(R.id.btn_reserve);
        btn_rsvList = (Button) findViewById(R.id.btn_rsvList);
        btn_toOwner = (Button) findViewById(R.id.btn_toOwner);
        et_pin = (EditText) findViewById(R.id.et_pin);

        // Check exist Restaurant
        if(user_data.UserRsid > 0) {
            // Exist
            Log.e("test", "2");

            // Connect Web Server to Get Restaurant Data.
            HttpConnector FieldConnector = new HttpConnector();
            String param = "rs_id=" + user_data.UserRsid + "";
            String httpResult = FieldConnector.ConnectServer(param, "/restaurant/"+String.valueOf(user_data.UserRsid), "GET");
            String httpCode = FieldConnector.HttpResCode;

            // Parsing JSON
            try {
                JSONObject json_user = new JSONObject(httpResult);

                rs_data.rs_id = user_data.UserRsid;
                rs_data.name = json_user.getString("rs_name").toString().trim();
                rs_data.phone = json_user.getString("rs_phone").toString().trim();
                rs_data.address = json_user.getString("rs_address").toString().trim();
                rs_data.intro = json_user.getString("rs_intro").toString().trim();
                rs_data.open = json_user.getString("rs_open").toString().trim();
                rs_data.close = json_user.getString("rs_close").toString().trim();
                rs_data.state = json_user.getInt("rs_state");

                tv_restaurant_field.setText(rs_data.name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        btn_manageOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_manageTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FieldActivity.this, ManageTableActivity.class);
                startActivity(intent);
            }
        });

        btn_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FieldActivity.this, VisitActivity.class);
                startActivity(intent);
            }
        });

        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FieldActivity.this, ReserveActivity.class);
                startActivity(intent);
            }
        });

        btn_rsvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FieldActivity.this, ReserveListActivity.class);
                startActivity(intent);
            }
        });

        btn_toOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputPin = et_pin.getText().toString();

                if (inputPin.equals(user_data.UserPin)) {
                    Intent intent = new Intent(FieldActivity.this, OfficeActivity.class); // (현재 액티비티, 이동할 액티비티)
                    finish();
                    startActivity(intent);
                }
                else {
                    return;
                }
            }
        });



    }
}
