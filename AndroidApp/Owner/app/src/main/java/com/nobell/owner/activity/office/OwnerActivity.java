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
import com.nobell.owner.model.UserData;

public class OwnerActivity extends AppCompatActivity {

    private TextView owner_name, owner_email, owner_phone;
    private EditText owner_pwd, owner_check, owner_pin;
    private Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        final UserData user_data = MainActivity.user_data;

        // Get View Object
        owner_name = (TextView) findViewById(R.id.owner_name);
        owner_email = (TextView) findViewById(R.id.owner_email);
        owner_pwd = (EditText) findViewById(R.id.owner_pwd);
        owner_check = (EditText) findViewById(R.id.owner_check);
        owner_phone = (TextView) findViewById(R.id.owner_phone);
        owner_pin = (EditText) findViewById(R.id.owner_new_pin);

        btn_confirm = (Button) findViewById(R.id.btn_owner);

        owner_name.setText(user_data.UserName);
        owner_email.setText(user_data.UserEmail);
        owner_phone.setText(user_data.UserPhone);

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
                String param = "owner_email=" + user_data.UserEmail + "&owner_pw=" + input_pwd + "&owner_pin=" + input_pin + "";
                String result_owner = OwnerConnector.ConnectServer(param, "/info", "POST");

                String httpCode = OwnerConnector.HttpResCode;

                if(httpCode.equals("200")){
                    user_data.UserPwd = input_pwd;
                    user_data.UserPin = input_pin;
                    returnToBack();
                }
                else {
                    return;
                }
            }
        });
    }

    // Back to OfficeActivity
    @Override
    public void onBackPressed() {
        returnToBack();
    }

    public void returnToBack() {
        Intent intent;
        intent = new Intent(OwnerActivity.this, OfficeActivity.class); // (현재 액티비티, 이동할 액티비티)

        finish();
        startActivity(intent);
    }
}
