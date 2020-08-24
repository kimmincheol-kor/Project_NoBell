package com.nobell.owner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class SignupActivity extends AppCompatActivity {

    private EditText reg_name, reg_email, reg_pwd, reg_check, reg_phone;
    private Button btn_register;

    private String name, email, pwd, re_pwd, phone;
    private String result_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Get View Object
        reg_name = (EditText) findViewById(R.id.reg_name);
        reg_email = (EditText) findViewById(R.id.reg_email);
        reg_pwd = (EditText) findViewById(R.id.reg_pwd);
        reg_check = (EditText) findViewById(R.id.reg_check);
        reg_phone = (EditText) findViewById(R.id.reg_phone);

        btn_register = (Button) findViewById(R.id.btn_register);

        // Click Event Listener : Register
        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // get data from EditText View
                name = reg_name.getText().toString();
                email = reg_email.getText().toString();
                pwd = reg_pwd.getText().toString();
                re_pwd = reg_check.getText().toString();
                phone = reg_phone.getText().toString();

                // If Passwords are Different
                if (!pwd.equals(re_pwd)) {
                    Toast.makeText(SignupActivity.this, "Two Passwords are Different", Toast.LENGTH_SHORT).show();
                    return;
                }

                HttpConnector RegisterConnector = new HttpConnector();
                String param = "reg_name=" + name + "&reg_email=" + email + "&reg_pwd=" + pwd + "&reg_phone=" + phone + "";
                result_register = RegisterConnector.ConnectServer(param, "/register", "POST");

                // if Success To Register.
                if (result_register.equals("success")) {
                    Toast.makeText(SignupActivity.this, "Success to Register", Toast.LENGTH_SHORT).show();

                    Intent intent;
                    intent = new Intent(SignupActivity.this, MainActivity.class); // (현재 액티비티, 이동할 액티비티)

                    // Moving Activity -> Main
                    startActivity(intent);
                }
                // if Fail To Register.
                else {
                    if (result_register.equals("fail:500")) {
                        Toast.makeText(SignupActivity.this, "Fail to Register : Exist Member", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignupActivity.this, "Fail to Register : ERROR IN SERVER", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // End of Register
    }
}