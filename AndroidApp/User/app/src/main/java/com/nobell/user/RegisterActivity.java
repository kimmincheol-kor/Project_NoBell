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

public class RegisterActivity extends AppCompatActivity {

    private EditText et_email, et_name ,et_pwd, et_check, et_phone;
    private Button btn_register;
    private String name ;
    private String email;
    private String pwd;
    private String pwdcheck;
    private String phone;
    private String result_register;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //액티비티 시작시 처음으로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //아이디값 찾기

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        et_check = findViewById(R.id.et_check);
        et_phone = findViewById(R.id.et_phone);

        //회원가빙 버튼 클릭시 수행
        btn_register = findViewById(R.id.btn_register);


        //Click Event Listener : Register
        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                // get data from EditText View
                name = et_name.getText().toString();
                email = et_email.getText().toString();
                pwd = et_pwd.getText().toString();
                pwdcheck = et_check.getText().toString();
                phone = et_phone.getText().toString();
                // If Passwords are Different
                if(!pwd.equals(pwdcheck)) {
                    Toast.makeText(RegisterActivity.this, "Two Passwords are Different", Toast.LENGTH_SHORT).show();
                    return;
                }

                RegisterActivity.RegisterTask registerTask = new RegisterActivity.RegisterTask();

                try {
                    data = registerTask.execute().get().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if(data.equals("RegOK"))
                {
                    Toast.makeText(RegisterActivity.this, "회원가입에 성공하였습니다", Toast.LENGTH_SHORT).show();

                    Intent intent;
                    intent = new Intent(RegisterActivity.this, MainActivity.class); // (현재 액티비티, 이동할 액티비티)


                    startActivity(intent);
                }

                else
                {
                    if(data.equals("Exist")) {
                        Toast.makeText(RegisterActivity.this, "이미 가입된 회원입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "오류가 발생하였습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    public class RegisterTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... unused) {


            String param = "reg_name=" + name + "&reg_email=" + email + "&reg_pwd=" + pwd + "&reg_phone=" + phone ;

            try {

                URL url = new URL("http://172.20.10.2:3000/user/register");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();


                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();


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
                result_register = buff.toString();
                data = result_register.trim();
                Log.e("RECV DATA", data);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

    }
}