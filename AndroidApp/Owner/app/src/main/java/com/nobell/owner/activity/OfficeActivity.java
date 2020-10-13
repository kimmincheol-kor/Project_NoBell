package com.nobell.owner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nobell.owner.R;
import com.nobell.owner.activity.office.OwnerActivity;
import com.nobell.owner.activity.office.RestaurantActivity;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.RestaurantData;
import com.nobell.owner.model.UserData;

import org.json.JSONException;
import org.json.JSONObject;

public class OfficeActivity extends AppCompatActivity {

    int backButtonCount;

    private TextView tv_restaurant;
    private Button btn_owner_info, btn_manage, btn_event, btn_review, btn_static, btn_field, btn_logout, btn_state;

    private String result_rs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);

        final UserData user_data = MainActivity.user_data;
        final RestaurantData rs_data = MainActivity.rs_data;

        // Get View Object
        tv_restaurant = (TextView) findViewById(R.id.tv_restaurant);
        btn_owner_info = (Button) findViewById(R.id.btn_owner_info);
        btn_manage = (Button) findViewById(R.id.btn_manage);
        btn_event = (Button) findViewById(R.id.btn_event);
        btn_review = (Button) findViewById(R.id.btn_review);
        btn_static = (Button) findViewById(R.id.btn_static);
        btn_field = (Button) findViewById(R.id.btn_field);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_state = (Button) findViewById(R.id.btn_rs_state);

        Log.e("test", String.valueOf(user_data.UserRsid));

        // Check exist Restaurant
        if(user_data.UserRsid > 0) {
            // Exist
            Log.e("test", "2");

            // Connect Web Server to Get Restaurant Data.
            HttpConnector OfficeConnector = new HttpConnector();
            String param = "rs_id=" + user_data.UserRsid + "";
            result_rs = OfficeConnector.ConnectServer(param, "/restaurant/"+String.valueOf(user_data.UserRsid), "GET");

            // Parsing JSON
            try {
                JSONObject json_user = new JSONObject(result_rs);

                rs_data.rs_id = user_data.UserRsid;
                rs_data.name = json_user.getString("rs_name").toString().trim();
                rs_data.phone = json_user.getString("rs_phone").toString().trim();
                rs_data.address = json_user.getString("rs_address").toString().trim();
                rs_data.intro = json_user.getString("rs_intro").toString().trim();
                rs_data.open = json_user.getString("rs_open").toString().trim();
                rs_data.close = json_user.getString("rs_close").toString().trim();
                rs_data.state = json_user.getInt("rs_state");

                if(rs_data.state == 1){
                    btn_state.setText("OPEN");
                    btn_state.setBackgroundColor(getResources().getColor(R.color.colorSkyBlue));
                }
                else{
                    btn_state.setText("CLOSE");
                    btn_state.setBackgroundColor(getResources().getColor(R.color.colorRed));
                }

                btn_state.setVisibility(View.VISIBLE);
                btn_state.setClickable(true);
                tv_restaurant.setText(rs_data.name);
                btn_manage.setText("업체 수정");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            // Not Exist
            btn_state.setVisibility(View.INVISIBLE);
            btn_state.setClickable(false);
            tv_restaurant.setText("등록된 업체가 없습니다");
            btn_manage.setText("업체 등록");
        }

        // Click State Button
        btn_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String param;
                if(rs_data.state == 0){ // close -> open
                    param = "rs_id=" + user_data.UserRsid + "&state=1" + "";
                    rs_data.state = 1;
                    btn_state.setText("OPEN");
                    btn_state.setBackgroundColor(getResources().getColor(R.color.colorSkyBlue));
                }
                else{ // open -> close
                    param = "rs_id=" + user_data.UserRsid + "&state=0" + "";
                    rs_data.state = 0;
                    btn_state.setText("CLOSE");
                    btn_state.setBackgroundColor(getResources().getColor(R.color.colorRed));
                }

                // Connect Web Server to Get Restaurant Data.
                HttpConnector OfficeConnector = new HttpConnector();
                result_rs = OfficeConnector.ConnectServer(param, "/restaurant/change_state", "POST");
            }
        });

        // Click Event : Owner Info Button
        btn_owner_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Making Intent
                Intent intent;
                intent = new Intent(OfficeActivity.this, OwnerActivity.class); // (현재 액티비티, 이동할 액티비티)

                // Moving Activity
                finish();
                startActivity(intent);
            }
        });
        // End of Click

        // Click Event : Manage Button
        btn_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Making Intent
                Intent intent;
                intent = new Intent(OfficeActivity.this, RestaurantActivity.class); // (현재 액티비티, 이동할 액티비티)

                // Moving Activity
                finish();
                startActivity(intent);
            }
        });
        // End of Click

        // Click Event : Event Button
        btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Making Intent
                Intent intent;
                intent = new Intent(OfficeActivity.this, OwnerActivity.class); // (현재 액티비티, 이동할 액티비티)

                // Moving Activity
                finish();
                startActivity(intent);
            }
        });
        // End of Click

        // Click Event : Review Button
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Making Intent
                Intent intent;
                intent = new Intent(OfficeActivity.this, OwnerActivity.class); // (현재 액티비티, 이동할 액티비티)

                // Moving Activity
                finish();
                startActivity(intent);
            }
        });
        // End of Click

        // Click Event : Static Button
        btn_static.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Making Intent
                Intent intent;
                intent = new Intent(OfficeActivity.this, OwnerActivity.class); // (현재 액티비티, 이동할 액티비티)

                // Moving Activity
                finish();
                startActivity(intent);
            }
        });
        // End of Click

        // Click Event : Field Button
        btn_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Making Intent
                Intent intent;
                intent = new Intent(OfficeActivity.this, OwnerActivity.class); // (현재 액티비티, 이동할 액티비티)

                // Moving Activity
                finish();
                startActivity(intent);
            }
        });
        // End of Click

        // Click Event : Logout Button
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset SharedPreference
                SharedPreferences.Editor editor = MainActivity.editor;
                editor.putBoolean("AutoLogin", false);
                editor.apply();

                // Move to MainActivity
                Intent intent;
                intent = new Intent(OfficeActivity.this, com.nobell.owner.activity.MainActivity.class);

                finish();
                startActivity(intent);

            }
        });
        // End of Logout
    }

    // Exit App Function by Press Back
    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            finish();
            startActivity(intent);
        } else {
            Toast.makeText(this, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}