package com.nobell.owner.activity.office.restaurant;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nobell.owner.R;
import com.nobell.owner.activity.MainActivity;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TableActivity extends AppCompatActivity {

    private TableLayout layout_tables;
    private UserData user_data;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        layout_tables = (TableLayout) findViewById(R.id.layout_table);
        user_data = MainActivity.user_data;

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        params.weight = 1;

        ////// Get Tables From Server
        // 1. get Datas.
        // Connect Web Server to Read Menus. : GET
        HttpConnector TableConnector = new HttpConnector();
        String json_table = TableConnector.ConnectServer("", "/table/" + String.valueOf(user_data.UserRsid), "GET");

        String httpCode = TableConnector.HttpResCode;
        if (httpCode.equals("200")) {
            int cnt = 0;
            try {
                JSONArray jArr = new JSONArray(json_table);

                JSONObject jsonTable = jArr.getJSONObject(cnt++);
                int table_no = jsonTable.getInt("table_no");
                int table_x = jsonTable.getInt("table_position_x");
                int table_y = jsonTable.getInt("table_position_y");

                for(int i=0; i<10; i++){
                    TableRow newRow = new TableRow(this);
                    newRow.setWeightSum(10);

                    for(int j=0; j<10; j++){
                        // Menu name View
                        Button rs_table = new Button(this);
                        rs_table.setGravity(Gravity.CENTER);
                        rs_table.setHeight(20);
                        rs_table.setHint((j+1)+","+(i+1));

                        // If Exist Table
                        if (table_x == (j+1) && table_y == (i+1)) {
                            rs_table.setText(table_no + "");
                            rs_table.setBackgroundColor(getResources().getColor(R.color.colorSkyBlue));
                            rs_table.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Button selectTable = (Button) view;

                                    String no = selectTable.getText().toString();

                                    Intent intent = new Intent(TableActivity.this, EditTableActivity.class);
                                    intent.putExtra("table_no", no);
                                    startActivityForResult(intent, 2);
                                }
                            });

                            if(cnt < jArr.length()){
                                jsonTable = jArr.getJSONObject(cnt++);
                            }
                            table_no = jsonTable.getInt("table_no");
                            table_x = jsonTable.getInt("table_position_x");
                            table_y = jsonTable.getInt("table_position_y");

                            Log.e("table : ", table_no+"");
                        }
                        // If Empty Table
                        else {
                            rs_table.setText(" ");
                            rs_table.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Button selectTable = (Button) view;

                                    String position = selectTable.getHint().toString();
                                    Log.e("BLAH", position);
                                    int idx = position.indexOf(',');
                                    int position_x = Integer.parseInt(position.substring(0, idx));
                                    int position_y = Integer.parseInt(position.substring(idx+1, position.length()));

                                    Intent intent = new Intent(TableActivity.this, PlusTableActivity.class);
                                    intent.putExtra("position_x", position_x);
                                    intent.putExtra("position_y", position_y);

                                    startActivityForResult(intent, 1);
                                }
                            });
                        }
                        newRow.addView(rs_table, params);
                    }
                    layout_tables.addView(newRow);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            for(int i=0; i<10; i++){
                TableRow newRow = new TableRow(this);
                newRow.setWeightSum(10);

                for(int j=0; j<10; j++){
                    // Menu name View
                    Button rs_table = new Button(this);
                    rs_table.setGravity(Gravity.CENTER);
                    rs_table.setHeight(20);
                    rs_table.setHint((j+1) + "," + (i+1));
                    Log.e("BLAH", (i+1) + "," + (j+1));

                    // If Empty Table
                    if (true) {
                        rs_table.setText(" ");
                        rs_table.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Button selectTable = (Button) view;

                                String position = selectTable.getHint().toString();
                                Log.e("BLAH", position);
                                int idx = position.indexOf(',');
                                int position_x = Integer.parseInt(position.substring(0, idx));
                                int position_y = Integer.parseInt(position.substring(idx+1, position.length()));

                                Intent intent = new Intent(TableActivity.this, PlusTableActivity.class);
                                intent.putExtra("position_x", position_x);
                                intent.putExtra("position_y", position_y);

                                startActivityForResult(intent, 1);
                            }
                        });
                    }
                    newRow.addView(rs_table, params);
                }
                layout_tables.addView(newRow);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Register New Table Process
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Get Data From Pop-Up Activity
                String new_no = data.getStringExtra("table_no");
                String new_people = data.getStringExtra("table_people");
                int position_x = data.getIntExtra("position_x", 0);
                int position_y = data.getIntExtra("position_y", 0);

                // Send to Server : Menu Data
                // Connect Web Server to Insert Menu Data.
                HttpConnector MenuConnector = new HttpConnector();
                String param = "rs_id=" + user_data.UserRsid + "&table_no=" + new_no + "&table_x=" + position_x + "&table_y=" + position_y + "&table_headcount=" + new_people + "&operation=create" + "";
                String result_menu = MenuConnector.ConnectServer(param, "/table/", "POST");

                recreate();
            }
        }

        // Edit Table Process
        else if(requestCode == 2){
            if (resultCode == 10){
                // UPDATE
                // Get Data From Pop-Up Activity
                String new_no = data.getStringExtra("table_no");
                String new_people = data.getStringExtra("table_people");

                // Send to Server : Menu Data
                // Connect Web Server to Insert Menu Data.
                HttpConnector MenuConnector = new HttpConnector();
                String param = "rs_id=" + user_data.UserRsid + "&table_no=" + new_no + "&table_headcount=" + new_people + "&operation=update" + "";
                String result_menu = MenuConnector.ConnectServer(param, "/table/", "POST");

                recreate();
            }

            // Delete Menu Process
            else if(resultCode == 20){
                // DELETE
                String new_no = data.getStringExtra("table_no");

                // Connect Web Server to Insert Menu Data.
                HttpConnector MenuConnector = new HttpConnector();
                String param = "rs_id=" + user_data.UserRsid + "&table_no=" + new_no + "&operation=delete" + "";
                String result_menu = MenuConnector.ConnectServer(param, "/table/", "POST");

                recreate();
            }
        }
    }
}
