package com.nobell.owner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.nobell.owner.R;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.RestaurantData;
import com.nobell.owner.model.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btn_signin;
    private Button btn_signup;
    private EditText et_id;
    private EditText et_pwd;
    private CheckBox cb_autologin;

    private String in_id, in_pw;
    private String result_login;

    public static Context context_main;
    public static UserData user_data = new UserData();
    public static RestaurantData rs_data = new RestaurantData();

    // Load SharedPreference : appData
    public static SharedPreferences appData;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context_main = this;

        // Get View Object
        btn_signin = (Button)findViewById(R.id.btn_signin);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        et_id = (EditText) findViewById(R.id.et_id);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        cb_autologin = (CheckBox) findViewById(R.id.cb_autologin);

        // Click Event : Signin Button
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Data From EditText View
                in_id = et_id.getText().toString();
                in_pw = et_pwd.getText().toString();

                // if Empty Text
                if(in_id.length() == 0 || in_pw.length() == 0)
                    return;

                // Connect Web Server to Login.
                HttpConnector MainConnector = new HttpConnector();
                String param = "login_email=" + in_id + "&login_pwd=" + in_pw + "";
                result_login = MainConnector.ConnectServer(param, "/login", "POST");

                // Parsing JSON
                try {
                    JSONObject json_user = new JSONObject(result_login);

                    user_data.UserEmail = json_user.getString("owner_email").toString().trim();
                    user_data.UserPwd = json_user.getString("owner_pwd").toString().trim();
                    user_data.UserName = json_user.getString("owner_name").toString().trim();
                    user_data.UserPhone = json_user.getString("owner_phone").toString().trim();
                    user_data.UserRsid = json_user.getInt("owner_rs_id");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Check Result of Login
                // Success
                if(!result_login.equals("fail:500"))
                {
                    Toast.makeText(MainActivity.this, "Success to Login", Toast.LENGTH_SHORT).show();

                    editor.putBoolean("AutoLogin", cb_autologin.isChecked());
                    editor.putBoolean("UserLogined", user_data.UserLogined);
                    editor.putString("UserEmail", user_data.UserEmail);
                    editor.putString("UserPwd", user_data.UserPwd);
                    editor.apply();

                    // Move
                    Intent intent;
                    intent = new Intent(MainActivity.this, OfficeActivity.class); // (현재 액티비티, 이동할 액티비티)

                    finish();
                    startActivity(intent);
                }
                // Fail
                else
                {
                    if(result_login.equals("fail:500")) {
                        Toast.makeText(MainActivity.this, "Fail to Login : Input Data is Incorrect", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Fail to Login : SERVER ERROR", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        // End of Siginin Click

        // Click Event : Signup Button
        btn_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // Making Intent
                Intent intent;
                intent = new Intent(MainActivity.this, SignupActivity.class);

                // Moving Activity
                startActivity(intent);

            }
        });
        // End of Signup Click

        // Set SharedPreference : user_data
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        editor = appData.edit();

        // Load Saved Data
        // Get Saved Login Data
        user_data.UserAuto = appData.getBoolean("AutoLogin", false);
        user_data.UserLogined = appData.getBoolean("UserLogined", false);
        user_data.UserEmail = appData.getString("UserEmail", "");
        user_data.UserPwd = appData.getString("UserPwd", "");

        Log.e("user : ", user_data.UserAuto.toString());

        // if AutoLogin,
        if(user_data.UserAuto)
        {
            // Set Saved Data,
            et_id.setText(user_data.UserEmail);
            et_pwd.setText(user_data.UserPwd);
            cb_autologin.setChecked(user_data.UserAuto);

            // Auto Click Signin button
            btn_signin.callOnClick();
        }
        else{
            // Clear Saved Login Data
            editor.putBoolean("AutoLogin", false);
            editor.putBoolean("UserLogined", false);
            editor.putString("UserEmail", "");
            editor.putString("UserPwd", "");

            user_data.clear_user();

            editor.apply();
        }
    }
}
