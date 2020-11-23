package com.nobell.owner.activity.field;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nobell.owner.R;
import com.nobell.owner.model.HttpConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HandleTableActivity extends AppCompatActivity {

    TextView tv_handle_table, tv_handle_table_total;
    TableLayout layout_handle_table;
    LinearLayout layout_handle_table_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_handle_table);

        tv_handle_table = (TextView) findViewById(R.id.tv_handle_table);
        tv_handle_table_total = (TextView) findViewById(R.id.tv_handle_table_total);
        layout_handle_table = (TableLayout) findViewById(R.id.layout_handle_table);
        layout_handle_table_btn = (LinearLayout) findViewById(R.id.layout_handle_table_btn);

        Intent intent = getIntent();
        String table_no = intent.getStringExtra("table");
        int table_state = intent.getIntExtra("state", 0);

        tv_handle_table.setText(table_no);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;

        Button btn_handle_table = new Button(this);
        btn_handle_table.setTextSize(15);
        btn_handle_table.setBackgroundColor(getResources().getColor(R.color.colorSkyBlue));
        btn_handle_table.setTextColor(getResources().getColor(R.color.colorWhite));
        btn_handle_table.setGravity(Gravity.CENTER);
        btn_handle_table.setHint(table_no);
        btn_handle_table.setPadding(16,16,16,16);

        if (table_state == 1) {
            EditText new_table = new EditText(this);
            new_table.setHint("이동할 테이블");
            new_table.setTextSize(15);
            new_table.setGravity(Gravity.CENTER);
            new_table.setBackgroundColor(getResources().getColor(R.color.colorSkyBlue));

            btn_handle_table.setText("테이블 이동");
            btn_handle_table.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView me = (TextView) view;
                    String no = me.getHint().toString();

//                    ////// Get Visits From Server
//                    HttpConnector HandlerConnector = new HttpConnector();
//                    HandlerConnector.ConnectServer("table=" + no, "/order/move", "PUT");
//
//                    if (HandlerConnector.HttpResCode.equals("200")) finish();
                    finish();
                }
            });

            layout_handle_table_btn.addView(new_table, params);
            layout_handle_table_btn.addView(btn_handle_table, params);
        }
        else if (table_state == 2) {
            btn_handle_table.setText("계산 처리");
            btn_handle_table.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView me = (TextView) view;
                    TextView tv_total = (TextView) findViewById(R.id.tv_handle_table_total);
                    String no = me.getHint().toString();
                    String total = tv_total.getHint().toString();

                    ////// Get Visits From Server
                    HttpConnector HandlerConnector = new HttpConnector();
                    HandlerConnector.ConnectServer("table=" + no + "&total=" + total, "/order/pay", "POST");

                    if (HandlerConnector.HttpResCode.equals("200")) {
                        ManageTableActivity.mta.recreate();
                        finish();
                    }
                }
            });

            layout_handle_table_btn.addView(btn_handle_table, params);
        }

        ////// Get Visits From Server
        HttpConnector OrderConnector = new HttpConnector();
        OrderConnector.ConnectServer("", "/order/" + table_no, "GET");

        String httpCode = OrderConnector.HttpResCode;
        String httpResult = OrderConnector.HttpResult;

        if (httpCode.equals("200")) {
            try {
                int total = 0;
                Map<String, Integer> orderMap = new HashMap<>();
                Map<String, Integer> menuMap = new HashMap<>();
                JSONArray jArr = new JSONArray(httpResult);

                for (int a = 0; a < jArr.length(); a++) {
                    JSONObject jsonOrder = jArr.getJSONObject(a);
                    JSONObject jsonDetail = new JSONObject(jsonOrder.getString("order_json"));

                    Iterator i = jsonDetail.keys();
                    while (i.hasNext()) {
                        String menu = i.next().toString();
                        int num = jsonDetail.getInt(menu);

                        if (orderMap.containsKey(menu)) {
                            orderMap.put(menu, orderMap.get(menu) + num);
                        } else {
                            orderMap.put(menu, num);
                        }
                    }
                }

                OrderConnector.ConnectServer("", "/menu", "GET");
                String httpResult2 = OrderConnector.HttpResult;
                JSONArray jArr2 = new JSONArray(httpResult2);

                for(int i=0; i<jArr2.length(); i++) {
                    JSONObject jsonMenu = jArr2.getJSONObject(i);

                    String menuName = jsonMenu.getString("menu_name");
                    String menuPrice = jsonMenu.getString("menu_price");

                    Log.e("Menu : ", menuName + " " + menuPrice);
                    menuMap.put(menuName, Integer.parseInt(menuPrice));
                }

                for(String menu:orderMap.keySet()) {

                    int num = orderMap.get(menu);
                    int price = menuMap.get(menu);

                    total += num*price;

                    TableRow newRow = new TableRow(this);

                    TextView tv_menu = new TextView(this);
                    TextView tv_price = new TextView(this);

                    tv_menu.setText(menu);
                    tv_menu.setTextColor(getResources().getColor(R.color.colorBlack));
                    tv_price.setText(num + "개");
                    tv_price.setGravity(Gravity.RIGHT);
                    tv_price.setTextColor(getResources().getColor(R.color.colorBlack));

                    newRow.addView(tv_menu, params);
                    newRow.addView(tv_price, params);

                    layout_handle_table.addView(newRow);
                }

                tv_handle_table_total.setText("총합 : " + total + "원");
                tv_handle_table_total.setHint(total + "");

            } catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}
