package com.nobell.owner.activity.office;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nobell.owner.R;
import com.nobell.owner.activity.MainActivity;
import com.nobell.owner.activity.OfficeActivity;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.OwnerData;

public class OwnerActivity extends AppCompatActivity {

    private TextView owner_name, owner_email, owner_phone;
    private EditText owner_pwd, owner_check, owner_pin;
    private Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        // Get View Object
        owner_name = (TextView) findViewById(R.id.owner_name);
        owner_email = (TextView) findViewById(R.id.owner_email);
        owner_pwd = (EditText) findViewById(R.id.owner_pwd);
        owner_check = (EditText) findViewById(R.id.owner_check);
        owner_phone = (TextView) findViewById(R.id.owner_phone);
        owner_pin = (EditText) findViewById(R.id.owner_new_pin);

        btn_confirm = (Button) findViewById(R.id.btn_owner);

        owner_name.setText(OwnerData.OwnerName);
        owner_email.setText(OwnerData.OwnerEmail);
        owner_phone.setText(OwnerData.OwnerPhone);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get data from EditText View
                String input_pwd = owner_pwd.getText().toString();
                String input_re_pwd = owner_check.getText().toString();
                String input_pin = owner_pin.getText().toString();

                // Check blank
                if(input_pwd.length() == 0) return;
                if(input_re_pwd.length() == 0) return;
                if(input_pin.length() == 0) return;

                // If Passwords are Different
                if (!input_pwd.equals(input_re_pwd)) {
                    Toast.makeText(OwnerActivity.this, "Two Passwords are Different", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Request Edit Owner to Server
                HttpConnector OwnerConnector = new HttpConnector();
                String param = "new_pw=" + input_pwd + "&new_pin=" + input_pin + "";
                OwnerConnector.ConnectServer(param, "/info", "POST");

                String httpCode = OwnerConnector.HttpResCode;

                if(httpCode.equals("200")){
                    Toast.makeText(OwnerActivity.this, "개인 정보 수정 완료", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(OwnerActivity.this, OfficeActivity.class);
                    finish();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(OwnerActivity.this, "개인 정보 수정 실패 : " + httpCode, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
