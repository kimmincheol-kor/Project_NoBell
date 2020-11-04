package com.nobell.owner.activity.office;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.nobell.owner.R;
import com.nobell.owner.activity.OfficeActivity;
import com.nobell.owner.activity.office.restaurant.MenuActivity;
import com.nobell.owner.activity.office.restaurant.TableActivity;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.RestaurantData;
import com.nobell.owner.model.OwnerData;

public class RestaurantActivity extends AppCompatActivity {

    private EditText rs_name, rs_open, rs_close, rs_intro, rs_phone, rs_address;
    private ImageButton rs_img;
    private Button rs_confirm, rs_menu, rs_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // Get View Object
        rs_name = (EditText) findViewById(R.id.rs_name);
        rs_phone = (EditText) findViewById(R.id.rs_phone);
        rs_address = (EditText) findViewById(R.id.rs_address);

        rs_intro = (EditText) findViewById(R.id.rs_intro);

        rs_open = (EditText) findViewById(R.id.rs_open);
        rs_close = (EditText) findViewById(R.id.rs_close);

        rs_img = (ImageButton) findViewById(R.id.img_rs);

        rs_confirm = (Button) findViewById(R.id.btn_rs);
        rs_menu = (Button) findViewById(R.id.btn_menu);
        rs_table = (Button) findViewById(R.id.btn_table);

        // if exist restaurant,
        if (RestaurantData.rs_id > 0) {
            // set exist information
            rs_name.setText(RestaurantData.name);
            rs_phone.setText(RestaurantData.phone);
            rs_address.setText(RestaurantData.address);
            rs_intro.setText(RestaurantData.intro);
            rs_open.setText(RestaurantData.open);
            rs_close.setText(RestaurantData.close);
        }

        // click confirm btn
        rs_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get Input Text
                String input_name = rs_name.getText().toString();
                String input_phone = rs_phone.getText().toString();
                String input_address = rs_address.getText().toString();
                String input_intro = rs_intro.getText().toString();
                String input_open = rs_open.getText().toString();
                String input_close = rs_close.getText().toString();

                // check Empty
                if (input_name.length() == 0) return;
                if (input_phone.length() == 0) return;
                if (input_address.length() == 0) return;
                if (input_intro.length() == 0) return;
                if (input_open.length() == 0) return;
                if (input_close.length() == 0) return;

                // Connect Web Server to Update Restaurant Data.
                HttpConnector RsConnector = new HttpConnector();
                String param = "rs_name=" + input_name + "&rs_phone=" + input_phone + "&rs_address=" + input_address + "&rs_intro=" + input_intro + "&rs_open=" + input_open + "&rs_close=" + input_close + "";
                String result_rs = RsConnector.ConnectServer(param, "/restaurant", "POST");

                Intent intent = new Intent(RestaurantActivity.this, OfficeActivity.class);
                finish();
                startActivity(intent);
            }
        });

        // click edit menu btn
        rs_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OwnerData.OwnerRsid < 1) return;

                Intent intent = new Intent(RestaurantActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        // click edit table btn
        rs_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OwnerData.OwnerRsid < 1) return;

                Intent intent = new Intent(RestaurantActivity.this, TableActivity.class);
                startActivity(intent);
            }
        });
    }

    // Back to OfficeActivity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RestaurantActivity.this, OfficeActivity.class);
        finish();
        startActivity(intent);
    }
}
