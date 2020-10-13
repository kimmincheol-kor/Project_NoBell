package com.nobell.owner.activity.office;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.nobell.owner.R;
import com.nobell.owner.activity.MainActivity;
import com.nobell.owner.activity.OfficeActivity;
import com.nobell.owner.activity.office.restaurant.MenuActivity;
import com.nobell.owner.activity.office.restaurant.TableActivity;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.RestaurantData;
import com.nobell.owner.model.UserData;

public class RestaurantActivity extends AppCompatActivity {

    private EditText rs_name, rs_open, rs_close, rs_intro, rs_phone, rs_address;
    private ImageButton rs_img;
    private Button rs_confirm, rs_menu, rs_table;

    private int rs_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        final UserData user_data = MainActivity.user_data;
        final RestaurantData rs_data = MainActivity.rs_data;
        rs_id = rs_data.rs_id;

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
        if (rs_data.rs_id > 0) {
            // set exist information
            rs_name.setText(rs_data.name);
            rs_phone.setText(rs_data.phone);
            rs_address.setText(rs_data.address);
            rs_intro.setText(rs_data.intro);
            rs_open.setText(rs_data.open);
            rs_close.setText(rs_data.close);
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
                HttpConnector MainConnector = new HttpConnector();
                String param = "user_email=" + user_data.UserEmail + "&rs_id=" + user_data.UserRsid + "&name=" + input_name + "&phone=" + input_phone + "&address=" + input_address + "&intro=" + input_intro + "&open=" + input_open + "&close=" + input_close + "";
                String result_rs = MainConnector.ConnectServer(param, "/restaurant/update", "POST");

                // Making Intent
                Intent intent;
                intent = new Intent(RestaurantActivity.this, OfficeActivity.class); // (현재 액티비티, 이동할 액티비티)

                if(!result_rs.contains("fail")){
                    //  change to input
                    user_data.UserRsid = Integer.parseInt(result_rs);
                    rs_data.rs_id = user_data.UserRsid;
                    rs_data.name = input_name;
                    rs_data.phone = input_phone;
                    rs_data.address = input_address;
                    rs_data.intro = input_intro;
                    rs_data.open = input_open;
                    rs_data.close = input_close;
                }

                // Moving Activity
                finish();
                startActivity(intent);
            }
        });

        // click edit menu btn
        rs_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_data.UserRsid < 1) return;

                // Making Intent
                Intent intent;
                intent = new Intent(RestaurantActivity.this, MenuActivity.class); // (현재 액티비티, 이동할 액티비티)

                // Moving Activity
                startActivity(intent);
            }
        });

        // click edit table btn
        rs_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_data.UserRsid < 1) return;

                // Making Intent
                Intent intent;
                intent = new Intent(RestaurantActivity.this, TableActivity.class); // (현재 액티비티, 이동할 액티비티)

                // Moving Activity
                startActivity(intent);
            }
        });
    }

    // Back to OfficeActivity
    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(RestaurantActivity.this, OfficeActivity.class); // (현재 액티비티, 이동할 액티비티)

        finish();
        startActivity(intent);
    }
}
