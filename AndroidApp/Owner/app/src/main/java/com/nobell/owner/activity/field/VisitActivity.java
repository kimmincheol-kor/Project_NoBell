package com.nobell.owner.activity.field;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.nobell.owner.R;
import com.nobell.owner.activity.MainActivity;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VisitActivity extends AppCompatActivity {

    private TableLayout layout_visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        UserData user_data = MainActivity.user_data;

        layout_visit = (TableLayout) findViewById(R.id.layout_visit);

        ////// Get Visits From Server
        HttpConnector VisitConnector = new HttpConnector();
        VisitConnector.ConnectServer("", "/visit/" + String.valueOf(user_data.UserRsid), "GET");

        String httpCode = VisitConnector.HttpResCode;
        String httpResult = VisitConnector.HttpResult;

        if(httpCode.equals("200")) {
            // Parsing JSON
            try {
                JSONArray jArr = new JSONArray(httpResult);

                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                params.weight = 1;

                for(int i=0; i<jArr.length(); i++){
                    JSONObject jsonMenu = jArr.getJSONObject(i);

                    TableRow newRow = new TableRow(this);

                    newRow.setMinimumHeight(90);

                    int visitID = jsonMenu.getInt("visit_id");
                    int tableNo = jsonMenu.getInt("visit_table");
                    String customer = jsonMenu.getString("visit_customer");
                    int headcount = jsonMenu.getInt("visit_headcount");
                    String visitTime = jsonMenu.getString("visit_time");

                    // 2. add View to Layout.
                    TextView tv_table = new TextView(this);
                    TextView tv_customer = new TextView(this);
                    TextView tv_time = new TextView(this);

                    tv_table.setTextSize(12);
                    tv_table.setWidth(70);
                    tv_table.setGravity(Gravity.CENTER);
                    tv_table.setText(tableNo+"");

                    tv_customer.setTextSize(12);
                    tv_table.setWidth(70);
                    tv_customer.setGravity(Gravity.CENTER);
                    tv_customer.setText(headcount+"");

                    tv_time.setTextSize(12);
                    tv_table.setWidth(120);
                    tv_time.setGravity(Gravity.CENTER);
                    tv_time.setText(visitTime.substring(0, 10)+"-"+visitTime.substring(11, 19));

                    newRow.addView(tv_table);
                    newRow.addView(tv_customer);
                    newRow.addView(tv_time);

                    TableRow btnRow = new TableRow(this);

                    Button btn_confirm = new Button(this);
                    Button btn_reject = new Button(this);

                    btn_confirm.setText("승인");
                    btn_confirm.setTextSize(10);
                    btn_confirm.setGravity(Gravity.CENTER);
                    btn_confirm.setHint(visitID+"");
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Button me = (Button) view;
                            String visitID = me.getHint().toString();

                            HttpConnector VisitConnector = new HttpConnector();
                            VisitConnector.ConnectServer("visit_id=" + visitID + "", "/visit/confirm", "POST");

                            String httpCode = VisitConnector.HttpResCode;
                            if (httpCode.equals("200")) {
                                Toast.makeText(VisitActivity.this, "처리 성공", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                            else {
                                Toast.makeText(VisitActivity.this, "처리 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    btn_reject.setText("거절");
                    btn_reject.setTextSize(10);
                    btn_reject.setGravity(Gravity.CENTER);
                    btn_reject.setHint(visitID+"");
                    btn_reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Button me = (Button) view;
                            String visitID = me.getHint().toString();

                            HttpConnector VisitConnector = new HttpConnector();
                            VisitConnector.ConnectServer("visit_id=" + visitID + "", "/visit/reject", "POST");

                            String httpCode = VisitConnector.HttpResCode;
                            if (httpCode.equals("200")) {
                                Toast.makeText(VisitActivity.this, "처리 성공", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                            else {
                                Toast.makeText(VisitActivity.this, "처리 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    btnRow.addView(btn_confirm, params);
                    btnRow.addView(btn_reject, params);
                    newRow.addView(btnRow, params);

                    layout_visit.addView(newRow);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
