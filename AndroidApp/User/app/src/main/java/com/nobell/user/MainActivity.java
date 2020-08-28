package com.nobell.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText et_id;
    private EditText et_pwd;
    private Button btn_login, btn_register;
    private String lo_id , lo_pw;
    private String result_login;
    String data_l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        et_id = findViewById(R.id.et_id);
        et_pwd = findViewById(R.id.et_pwd);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lo_id = et_id.getText().toString();
                lo_pw = et_pwd.getText().toString();

                // Making AsyncTask For Server
                LoginTask logintask = new LoginTask();

                // Execute LoginTask
                try {
                    data_l = logintask.execute().get().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (data_l.equals("LogOK")) {
                    Toast.makeText(MainActivity.this, "로그인에 성공하였습니다", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ItemList.class);
                    startActivity(intent);
                }
                // Fail
                else {
                    if (data_l.equals("Incorrect")) {
                        Toast.makeText(MainActivity.this, "가입된 정보와 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }



    public class LoginTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... unused) {


            String param = "&login_email=" + lo_id+ "&login_pwd=" + lo_pw  + "";
            try {

                URL url = new URL("http://172.20.10.2:3000/user/login");
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


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                result_login = buff.toString();
                data_l = result_login.trim();
                Log.e("RECV DATA", data_l);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data_l;
        }

    }
}