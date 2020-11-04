package com.nobell.owner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nobell.owner.R;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.OwnerData;
import com.nobell.owner.model.mySP;

public class MainActivity extends AppCompatActivity {

    private Button btn_signin;
    private Button btn_signup;
    private EditText et_id;
    private EditText et_pwd;
    private CheckBox cb_autologin;

    private String in_id, in_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreference
        mySP.appData = getSharedPreferences("appData", MODE_PRIVATE);
        mySP.init();

        // Get View Object
        btn_signin = (Button)findViewById(R.id.btn_signin);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        et_id = (EditText) findViewById(R.id.et_id);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        cb_autologin = (CheckBox) findViewById(R.id.cb_autologin);

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
                String param = "signin_email=" + in_id + "&signin_pw=" + in_pw + "";
                MainConnector.ConnectServer(param, "/signin", "POST");

                String httpCode = MainConnector.HttpResCode;

                if(httpCode.equals("200")) {
                    OwnerData.updateInfo(); //

                    Toast.makeText(MainActivity.this, "Success to Login", Toast.LENGTH_SHORT).show();

                    if(cb_autologin.isChecked()) {
                        mySP.editor.putBoolean("AutoLogin", cb_autologin.isChecked());
                        mySP.editor.putString("OwnerEmail", OwnerData.OwnerEmail);
                        mySP.editor.putString("OwnerPw", OwnerData.OwnerPw);
                        mySP.editor.apply();
                    }

                    // Move to Field
                    Intent intent = new Intent(MainActivity.this, FieldActivity.class); // (현재 액티비티, 이동할 액티비티)
                    finish();
                    startActivity(intent);
                }
                else if (httpCode.equals("404")) {
                    String httpResult = MainConnector.HttpResult;
                    Toast.makeText(MainActivity.this, httpResult, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Fail to Login : SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        if(OwnerData.OwnerAuto)
        {
            et_id.setText(OwnerData.OwnerEmail);
            et_pwd.setText(OwnerData.OwnerPw);
            cb_autologin.setChecked(OwnerData.OwnerAuto);

            // Auto Click
            btn_signin.callOnClick();
        }
    }
}
