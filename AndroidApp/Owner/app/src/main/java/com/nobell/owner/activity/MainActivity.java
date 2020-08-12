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
    private String loaded_id, loaded_pwd;
    private String result_login;

    private SharedPreferences appData;
    private SharedPreferences.Editor editor;
    private boolean loaded_autologin;

    public static Context context_main;

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

                // Making AsyncTask For Server
                LoginTask logintask = new LoginTask();

                // Execute LoginTask
                try {
                    result_login = logintask.execute().get().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Check Result of Login
                // Success
                if(result_login.equals("success"))
                {
                    Toast.makeText(MainActivity.this, "Success to Login", Toast.LENGTH_SHORT).show();

                    editor.putBoolean("logined", true);
                    editor.apply();

                    if(cb_autologin.isChecked())
                        save();
                    else
                        clear();

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

                Intent intent;
                intent = new Intent(MainActivity.this, SignupActivity.class); // (현재 액티비티, 이동할 액티비티)

                // Moving Activity !!
                startActivity(intent);

            }
        });
        // End of Signup Click

        // Load SharedPreference : appData
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        editor = appData.edit();

        // Reset Logined
        editor.putBoolean("logined", false);
        editor.apply();

        // Load Saved Data
        load();

        // if AutoLogin,
        if(loaded_autologin)
        {
            // Set Saved Data,
            et_id.setText(loaded_id);
            et_pwd.setText(loaded_pwd);
            cb_autologin.setChecked(true);

            // Auto Click Signin button
            btn_signin.callOnClick();
        }
    }

    // AsyncTask For Connect Server
    public class LoginTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "login_email=" + in_id + "&login_pwd=" + in_pw + "";
            try {
                /* 서버연결 */
                URL url = new URL("http://18.191.244.197:3000/owner/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;
                String data = "";

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                result_login = buff.toString();
                data = result_login.trim();
                Log.e("RECV DATA", data);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result_login;
        }

    }

    ///////////////////////////////////////////////////////////////////
    // Function For SharedPreference
    private void save() {
        // Save User Login Data
        editor.putBoolean("AutoLogin", cb_autologin.isChecked());
        editor.putString("ID", et_id.getText().toString().trim());
        editor.putString("PWD", et_pwd.getText().toString().trim());

        editor.apply();
    }

    private void load() {
        // Get Saved Login Data
        loaded_autologin = appData.getBoolean("AutoLogin", false);
        loaded_id = appData.getString("ID", "");
        loaded_pwd = appData.getString("PWD", "");
    }

    private void clear() {
        // Clear Saved Login Data
        editor.putBoolean("AutoLogin", false);
        editor.putString("ID", "");
        editor.putString("PWD", "");

        editor.apply();
    }
    ///////////////////////////////////////////////////////////////////
}
