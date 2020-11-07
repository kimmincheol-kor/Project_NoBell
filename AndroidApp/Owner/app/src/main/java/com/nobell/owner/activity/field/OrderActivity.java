package com.nobell.owner.activity.field;

import androidx.appcompat.app.AppCompatActivity;

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
import com.nobell.owner.model.HttpConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderActivity extends AppCompatActivity {

    TableLayout layout_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        layout_order = (TableLayout) findViewById(R.id.layout_order);

        ////// Get Visits From Server
        HttpConnector OrderConnector = new HttpConnector();
        OrderConnector.ConnectServer("", "/order", "GET");

        String httpCode = OrderConnector.HttpResCode;
        String httpResult = OrderConnector.HttpResult;

        if (httpCode.equals("200")){
            try {
                JSONArray jArr = new JSONArray(httpResult);

                TableRow.LayoutParams params = new TableRow.LayoutParams();
                params.weight = 1;

                for(int i=0; i<jArr.length(); i++){
                    JSONObject jsonOrder = jArr.getJSONObject(i);

                    TableRow newRow = new TableRow(this);

                    int orderID = jsonOrder.getInt("order_id");
                    int orderTable = jsonOrder.getInt("order_table");
                    final String orderJson = jsonOrder.getString("order_json");
                    String orderTime = jsonOrder.getString("order_time");
                    int orderState = jsonOrder.getInt("order_state");

                    Log.e("TEST", orderID+"");

                    // 2. add View to Layout.
                    TextView tv_table = new TextView(this);
                    TextView tv_time = new TextView(this);
                    Button btn_check = new Button(this);
                    Button btn_process = new Button(this);

                    tv_table.setText(orderTable+"");
                    tv_table.setTextSize(18);
                    tv_table.setHeight(80);
                    tv_table.setGravity(Gravity.CENTER);

                    tv_time.setText(orderTime);
                    tv_time.setTextSize(9);
                    tv_time.setHeight(80);
                    tv_time.setGravity(Gravity.CENTER);

                    btn_check.setWidth(50);
                    btn_check.setHeight(80);
                    btn_check.setText("확인");
                    btn_check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(OrderActivity.this, CheckOrderActivity.class);
                            intent.putExtra("order", orderJson);
                            startActivity(intent);
                        }
                    });

                    btn_process.setWidth(50);
                    btn_process.setHeight(80);
                    btn_process.setHint(""+orderID);

                    if (orderState == 0) { // 접수 대기중
                        btn_process.setText("접수처리");
                        btn_process.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Button me = (Button) view;
                                String id = me.getHint().toString();

                                HttpConnector OrderConnector = new HttpConnector();
                                OrderConnector.ConnectServer("order_id=" + id, "/order/receive", "PUT");

                                recreate();
                            }
                        });
                    }
                    else if (orderState == 1) { // 완료 대기중
                        btn_process.setText("완료처리");
                        btn_process.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Button me = (Button) view;
                                String id = me.getHint().toString();

                                HttpConnector OrderConnector = new HttpConnector();
                                OrderConnector.ConnectServer("order_id=" + id, "/order/complete", "PUT");

                                recreate();
                            }
                        });
                    }
                    else if (orderState == 2) {
                        btn_process.setText("완료");
                        btn_process.setClickable(false);
                    }

                    newRow.addView(tv_table, params);
                    newRow.addView(tv_time, params);
                    newRow.addView(btn_check, params);
                    newRow.addView(btn_process);

                    layout_order.addView(newRow);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
