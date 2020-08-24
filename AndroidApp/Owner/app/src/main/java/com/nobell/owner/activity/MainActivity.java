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
import com.nobell.owner.model.UserData;

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

                String param = "login_email=" + in_id + "&login_pwd=" + in_pw + "";

                HttpConnector MainConnector = new HttpConnector();
                result_login = MainConnector.ConnectServer(param, "/login", "POST");

                // Check Result of Login
                // Success
                if(result_login.equals("success"))
                {
                    Toast.makeText(MainActivity.this, "Success to Login", Toast.LENGTH_SHORT).show();

                    user_data.UserLogined = true;
                    user_data.UserAuto = cb_autologin.isChecked();
                    user_data.UserEmail = et_id.getText().toString().trim();
                    user_data.UserPwd = et_pwd.getText().toString().trim();

                    user_data.save();

                    // Move
                    Intent intent;
                    intent = new Intent(MainActivity.this, OfficeActivity.class); // (현재 액티비티, 이동할 액티비티)

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

        // Load SharedPreference : appData
        user_data.appData = getSharedPreferences("appData", MODE_PRIVATE);
        user_data.editor = user_data.appData.edit();

        // Load Saved Data
        user_data.load();

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
        else
            user_data.clear();
    }
}
