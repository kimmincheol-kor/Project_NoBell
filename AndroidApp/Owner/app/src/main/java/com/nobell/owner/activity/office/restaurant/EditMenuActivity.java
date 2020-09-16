package com.nobell.owner.activity.office.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nobell.owner.R;

public class EditMenuActivity extends AppCompatActivity {

    private TextView tv_name;
    private EditText et_price;
    private Button btn_confirm, btn_delete;

    String menu_name;
    private String input_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_menu);

        tv_name = (TextView) findViewById(R.id.tv_menu_name);
        et_price = (EditText) findViewById(R.id.et_edit_price);
        btn_confirm = (Button) findViewById(R.id.btn_confirm_edit_menu);
        btn_delete = (Button) findViewById(R.id.btn_delete_edit_menu);

        Intent getIntent = getIntent();
        menu_name = getIntent.getStringExtra("menu_name");
        tv_name.setText(menu_name);

        // Click : Edit
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                input_price = et_price.getText().toString();
                if(input_price.length() == 0) return;

                // Transfer Data by Intent
                Intent intent = new Intent();
                intent.putExtra("menu_name", menu_name);
                intent.putExtra("menu_price", input_price);

                // Edit Code : 10
                setResult(10, intent);

                finish();
            }
        });

        // Click : Delete
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("menu_name", menu_name);

                // Delete Code : 20
                setResult(20, intent);

                finish();
            }
        });
    }
}