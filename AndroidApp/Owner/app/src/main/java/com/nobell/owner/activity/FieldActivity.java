package com.nobell.owner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nobell.owner.R;
import com.nobell.owner.model.UserData;

public class FieldActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        UserData user_data = MainActivity.user_data;

    }
}
