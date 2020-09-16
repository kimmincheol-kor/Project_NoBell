package com.nobell.owner.activity.office.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.nobell.owner.R;

public class PlusMenuActivity extends AppCompatActivity {

    private EditText et_name, et_price;
    private Button btn_confirm;

    private String input_name, input_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_plus_menu);

        et_name = (EditText) findViewById(R.id.et_menu_name);
        et_price = (EditText) findViewById(R.id.et_menu_price);
        btn_confirm = (Button) findViewById(R.id.btn_confirm_plus_menu);

        // Click : Confirm
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                input_name = et_name.getText().toString();
                input_price = et_price.getText().toString();

                if(input_name.length() == 0) return;
                if(input_price.length() == 0) return;

                // Transfer Data by Intent
                Intent intent = new Intent();
                intent.putExtra("menu_name", input_name);
                intent.putExtra("menu_price", input_price);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
}
