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

public class PlusTableActivity extends AppCompatActivity {

    private EditText et_no, et_people;
    private Button btn_confirm;

    private String input_no, input_people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_plus_table);

        et_no = (EditText) findViewById(R.id.et_table_no);
        et_people = (EditText) findViewById(R.id.et_table_people);
        btn_confirm = (Button) findViewById(R.id.btn_confirm_plus_table);

        Intent getIntent = getIntent();

        final int position_x = getIntent.getIntExtra("position_x", -1);
        final int position_y = getIntent.getIntExtra("position_y", -1);

        // Click : Confirm
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                input_no = et_no.getText().toString();
                input_people = et_people.getText().toString();

                if(input_no.length() == 0) return;
                if(input_people.length() == 0) return;

                // Transfer Data by Intent
                Intent intent = new Intent();
                intent.putExtra("table_no", input_no);
                intent.putExtra("table_people", input_people);
                intent.putExtra("position_x", position_x);
                intent.putExtra("position_y", position_y);

                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
}
