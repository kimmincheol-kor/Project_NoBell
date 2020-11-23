package com.nobell.owner.activity.field;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nobell.owner.R;
import com.nobell.owner.activity.MainActivity;
import com.nobell.owner.activity.office.restaurant.EditTableActivity;
import com.nobell.owner.activity.office.restaurant.PlusTableActivity;
import com.nobell.owner.activity.office.restaurant.TableActivity;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.OwnerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageTableActivity extends AppCompatActivity {

    public static Activity mta;
    private TextView tv_titleTable;
    private TableLayout layout_tables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_table);

        mta = ManageTableActivity.this;

        tv_titleTable = (TextView) findViewById(R.id.tv_titleTable);
        layout_tables = (TableLayout) findViewById(R.id.layout_tableManager);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        params.weight = 1;

        ////// Get Tables From Server
        // 1. get Datas.
        // Connect Web Server to Read Menus. : GET
        HttpConnector TableConnector = new HttpConnector();
        String json_table = TableConnector.ConnectServer("", "/table", "GET");

        String httpCode = TableConnector.HttpResCode;
        if (httpCode.equals("200")) {
            int cnt = 0;
            try {
                JSONArray jArr = new JSONArray(json_table);

                JSONObject jsonTable = jArr.getJSONObject(cnt++);
                int table_no = jsonTable.getInt("table_no");
                int table_x = jsonTable.getInt("table_position_x");
                int table_y = jsonTable.getInt("table_position_y");
                int table_state = jsonTable.getInt("table_state");

                for(int i=0; i<10; i++){
                    TableRow newRow = new TableRow(this);
                    newRow.setWeightSum(10);

                    for(int j=0; j<10; j++){
                        // Menu name View
                        Button rs_table = new Button(this);
                        rs_table.setGravity(Gravity.CENTER);
                        rs_table.setHeight(20);
                        rs_table.setHint((i+1) + "," + (j+1));

                        // If Exist Table
                        if (table_x == (j+1) && table_y == (i+1)) {
                            rs_table.setText(table_no + "");
                            Log.e("TABLE", table_no+"-"+table_state+"");
                            if (table_state == 0) {
                                rs_table.setBackgroundColor(getResources().getColor(R.color.colorSkyBlue));
                                rs_table.setClickable(false);
                            }
                            else if(table_state == 1) { // 여태 주문 내역 + 빈 테이블로 이동
                                rs_table.setBackgroundColor(getResources().getColor(R.color.colorRed));
                                rs_table.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Button selectTable = (Button) view;
                                        String no = selectTable.getText().toString();

                                        Intent intent = new Intent(ManageTableActivity.this, HandleTableActivity.class);
                                        intent.putExtra("table", no);
                                        intent.putExtra("state", 1);

                                        startActivity(intent);
                                    }
                                });
                            }
                            else if(table_state == 2) { // 여태 주문 내역 + 계산 처리
                                rs_table.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                                rs_table.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Button selectTable = (Button) view;
                                        String no = selectTable.getText().toString();

                                        Intent intent = new Intent(ManageTableActivity.this, HandleTableActivity.class);
                                        intent.putExtra("table", no);
                                        intent.putExtra("state", 2);

                                        startActivity(intent);
                                    }
                                });
                            }

                            if(cnt < jArr.length()){
                                jsonTable = jArr.getJSONObject(cnt++);
                            }
                            table_no = jsonTable.getInt("table_no");
                            table_x = jsonTable.getInt("table_position_x");
                            table_y = jsonTable.getInt("table_position_y");
                            table_state = jsonTable.getInt("table_state");

                            Log.e("table : ", table_no+"");
                        }
                        // If Empty Table
                        else {
                            rs_table.setText(" ");
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
            tv_titleTable.setText("오류");
        }
    }
}
