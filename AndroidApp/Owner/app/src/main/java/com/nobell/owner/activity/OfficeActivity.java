package com.nobell.owner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nobell.owner.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OfficeActivity extends AppCompatActivity {

    int backButtonCount;

    private TextView tv_restaurant;
    private Button btn_owner_info, btn_manage, btn_event, btn_review, btn_static, btn_field, btn_logout;

    private SharedPreferences appData;
    private SharedPreferences.Editor editor;

    private String result_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);

        // Get View Object
        tv_restaurant = (TextView)findViewById(R.id.tv_restaurant);
        btn_owner_info = (Button)findViewById(R.id.btn_owner_info);
        btn_manage = (Button)findViewById(R.id.btn_manage);
        btn_event = (Button)findViewById(R.id.btn_event);
        btn_review = (Button)findViewById(R.id.btn_review);
        btn_static = (Button)findViewById(R.id.btn_static);
        btn_field = (Button)findViewById(R.id.btn_field);
        btn_logout = (Button)findViewById(R.id.btn_logout);

        // Access to SharedPreference
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        editor = appData.edit();

        // Click Event : Owner Info Button
        btn_owner_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        // End of Click

        // Click Event : Manage Button
        btn_owner_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        // End of Click

        // Click Event : Event Button
        btn_owner_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        // End of Click

        // Click Event : Review Button
        btn_owner_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        // End of Click

        // Click Event : Static Button
        btn_owner_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        // End of Click

        // Click Event : Field Button
        btn_owner_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        // End of Click

        // Click Event : Logout Button
        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Reset SharedPreference
                editor.putBoolean("AutoLogin", false);
                editor.apply();

                // Move to MainActivity
                Intent intent;
                intent = new Intent(OfficeActivity.this, com.nobell.owner.activity.MainActivity.class);
                startActivity(intent);

            }
        });
        // End of Logout
    }

    // Exit App Function by Press Back
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    // AsyncTask For Connect Server
    public class OfficeTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "";//"reg_name=" + name + "&reg_email=" + email + "&reg_pwd=" + pwd + "&reg_phone=" + phone + "";
            try {
                /* 서버연결 */
                URL url = new URL("http://18.191.244.197:3000/owner/office");
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
                result_register = buff.toString();
                data = result_register.trim();
                Log.e("RECV DATA", data);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result_register;
        }

    }
}
