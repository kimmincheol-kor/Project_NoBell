package com.nobell.owner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nobell.owner.R;
import com.nobell.owner.activity.field.ManageTableActivity;
import com.nobell.owner.activity.field.ReserveActivity;
import com.nobell.owner.activity.field.ReserveListActivity;
import com.nobell.owner.activity.field.VisitActivity;
import com.nobell.owner.model.RestaurantData;
import com.nobell.owner.model.OwnerData;

public class FieldActivity extends AppCompatActivity {

    int backButtonCount = 0;

    private TextView tv_restaurant_field;
    private Button btn_manageOrder, btn_manageTable, btn_visit, btn_reserve, btn_rsvList, btn_toOwner;
    private EditText et_pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        // Get View Object
        tv_restaurant_field = (TextView) findViewById(R.id.tv_restaurant_field);
        btn_manageOrder = (Button) findViewById(R.id.btn_manageOrder);
        btn_manageTable = (Button) findViewById(R.id.btn_manageTable);
        btn_visit = (Button) findViewById(R.id.btn_visit);
        btn_reserve = (Button) findViewById(R.id.btn_reserve);
        btn_rsvList = (Button) findViewById(R.id.btn_rsvList);
        btn_toOwner = (Button) findViewById(R.id.btn_toOwner);
        et_pin = (EditText) findViewById(R.id.et_pin);

        OwnerData.updateInfo();

        // Check exist Restaurant
        if(OwnerData.OwnerRsid > 0 && RestaurantData.rs_id > 0) {
            tv_restaurant_field.setText(RestaurantData.name);
        }

        btn_manageOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_manageTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FieldActivity.this, ManageTableActivity.class);
                startActivity(intent);
            }
        });

        btn_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FieldActivity.this, VisitActivity.class);
                startActivity(intent);
            }
        });

        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FieldActivity.this, ReserveActivity.class);
                startActivity(intent);
            }
        });

        btn_rsvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FieldActivity.this, ReserveListActivity.class);
                startActivity(intent);
            }
        });

        btn_toOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputPin = et_pin.getText().toString();

                if (inputPin.equals(OwnerData.OwnerPin)) {
                    Toast.makeText(FieldActivity.this, "사장 모드 전환 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FieldActivity.this, OfficeActivity.class);
                    finish();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(FieldActivity.this, "핀 번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    // Exit App Function by Press Back
    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            finish();
            startActivity(intent);
        } else {
            Toast.makeText(this, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
