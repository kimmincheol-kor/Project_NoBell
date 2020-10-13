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

public class EditTableActivity extends AppCompatActivity {

    private TextView tv_no;
    private EditText et_people;
    private Button btn_confirm, btn_delete;

    int table_no, table_x, table_y;
    private String input_people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_table);

        tv_no = (TextView) findViewById(R.id.tv_table_no);
        et_people = (EditText) findViewById(R.id.et_edit_people);
        btn_confirm = (Button) findViewById(R.id.btn_confirm_edit_table);
        btn_delete = (Button) findViewById(R.id.btn_delete_edit_table);

        Intent myIntent = getIntent();
        final String table_no = myIntent.getStringExtra("table_no");

        tv_no.setText(table_no);

        // Click : Edit
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                input_people = et_people.getText().toString();
                if(input_people.length() == 0) return;

                // Transfer Data by Intent
                Intent intent = new Intent();
                intent.putExtra("table_no", table_no);
                intent.putExtra("table_people", input_people);

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
                intent.putExtra("table_no", table_no);

                // Delete Code : 20
                setResult(20, intent);

                finish();
            }
        });
    }
}