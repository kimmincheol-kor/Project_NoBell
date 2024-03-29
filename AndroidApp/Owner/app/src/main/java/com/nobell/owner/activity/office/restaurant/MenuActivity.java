package com.nobell.owner.activity.office.restaurant;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nobell.owner.R;
import com.nobell.owner.activity.MainActivity;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.OwnerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import static com.nobell.owner.activity.MainActivity.OwnerData;

public class MenuActivity extends AppCompatActivity {

    private Button btn_plus_menu;

    private LinearLayout view_menus;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btn_plus_menu = (Button) findViewById(R.id.btn_plus_menu);
        view_menus = (LinearLayout) findViewById(R.id.view_menus);

        ////// Get Menus From Server
        // 1. get Datas.
        // Connect Web Server to Read Menus. : GET
        HttpConnector MenuConnector = new HttpConnector();
        MenuConnector.ConnectServer("", "/menu", "GET");

        String httpCode = MenuConnector.HttpResCode;
        String httpResult = MenuConnector.HttpResult;

        if(httpCode.equals("200")) {
            // Parsing JSON
            try {
                JSONArray jArr = new JSONArray(httpResult);

                for(int i=0; i<jArr.length(); i++){
                    JSONObject jsonMenu = jArr.getJSONObject(i);

                    String menuName = jsonMenu.getString("menu_name");
                    String menuPrice = jsonMenu.getString("menu_price");

                    // 2. add View to Layout.
                    // Menu name View
                    TextView tv_name = new TextView(this);
                    tv_name.setWidth(300);
                    tv_name.setHeight(120);
                    tv_name.setTextSize(30);
                    tv_name.setText(menuName);
                    tv_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView selectMenu = (TextView) view;

                            String menuName = selectMenu.getText().toString();

                            Intent intent = new Intent(MenuActivity.this, EditMenuActivity.class);
                            intent.putExtra("menu_name", menuName);

                            startActivityForResult(intent, 2);
                        }
                    });
                    tv_name.setBackground(getDrawable(R.drawable.boarder));

                    // Menu Price View
                    TextView tv_price = new TextView(this);
                    tv_price.setWidth(300);
                    tv_price.setHeight(60);
                    tv_price.setGravity(Gravity.RIGHT);
                    tv_price.setTextSize(15);
                    tv_price.setText(menuPrice + "원");

                    view_menus.addView(tv_name);
                    view_menus.addView(tv_price);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Click : Plus Menu Btn.
        btn_plus_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, PlusMenuActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Register New Menu Process
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Get Data From Pop-Up Activity
                String new_name = data.getStringExtra("menu_name");
                String new_price = data.getStringExtra("menu_price");

                // Send to Server : Menu Data
                // Connect Web Server to Insert Menu Data.
                HttpConnector MenuConnector = new HttpConnector();
                String param  = "menu_name=" + new_name + "&menu_price=" + new_price + "";
                MenuConnector.ConnectServer(param, "/menu", "POST");

                String httpCode = MenuConnector.HttpResCode;
                if (httpCode.equals("200")) {
                    Toast.makeText(MenuActivity.this, "성공", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MenuActivity.this, "실패", Toast.LENGTH_SHORT).show();
                }

                recreate();
            }
        }

        // Edit Menu Price Process
        else if(requestCode == 2){
            if (resultCode == 10){
                // UPDATE
                String edit_name = data.getStringExtra("menu_name");
                String edit_price = data.getStringExtra("menu_price");

                // Connect Web Server to Update Menu Data.
                HttpConnector MenuConnector = new HttpConnector();
                String param = "menu_name=" + edit_name + "&menu_price=" + edit_price + "";
                MenuConnector.ConnectServer(param, "/menu/", "PUT");

                String httpCode = MenuConnector.HttpResCode;
                if (httpCode.equals("200")) {
                    Toast.makeText(MenuActivity.this, "성공", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MenuActivity.this, "실패", Toast.LENGTH_SHORT).show();
                }

                recreate();
            }

            // Delete Menu Process
            else if(resultCode == 20){
                // DELETE
                String del_name = data.getStringExtra("menu_name");

                // Connect Web Server to Delete Menu Data.
                HttpConnector MenuConnector = new HttpConnector();
                String param = "menu_name=" + del_name + "";
                MenuConnector.ConnectServer(param, "/menu/", "DELETE");

                String httpCode = MenuConnector.HttpResCode;
                if (httpCode.equals("200")) {
                    Toast.makeText(MenuActivity.this, "성공", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MenuActivity.this, "실패", Toast.LENGTH_SHORT).show();
                }

                recreate();
            }
        }
    }
}
