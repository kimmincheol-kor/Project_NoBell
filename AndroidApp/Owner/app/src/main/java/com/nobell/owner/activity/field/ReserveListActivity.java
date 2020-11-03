package com.nobell.owner.activity.field;

import androidx.appcompat.app.AppCompatActivity;

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

public class ReserveListActivity extends AppCompatActivity {

    private TableLayout layout_rsvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_list);

        UserData user_data = MainActivity.user_data;

        layout_rsvList = (TableLayout) findViewById(R.id.layout_rsvlist);

        ////// Get Visits From Server
        HttpConnector ReserveConnector = new HttpConnector();
        ReserveConnector.ConnectServer("", "/reserve/accepted/" + String.valueOf(user_data.UserRsid), "GET");

        String httpCode = ReserveConnector.HttpResCode;
        String httpResult = ReserveConnector.HttpResult;

        if(httpCode.equals("200")) {
            // Parsing JSON
            try {
                JSONArray jArr = new JSONArray(httpResult);

                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                TableRow.LayoutParams wparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                wparams.weight = 1;

                for(int i=0; i<jArr.length(); i++){
                    JSONObject jsonMenu = jArr.getJSONObject(i);

                    TableRow newRow = new TableRow(this);

                    newRow.setMinimumHeight(90);

                    int rsvID = jsonMenu.getInt("arsv_id");
                    int tableNo = jsonMenu.getInt("arsv_table");
                    String customer = jsonMenu.getString("arsv_customer");
                    int headcount = jsonMenu.getInt("arsv_headcount");
                    String visitTime = jsonMenu.getString("arsv_target");

                    // 2. add View to Layout.
                    TextView tv_table = new TextView(this);
                    TextView tv_headcount = new TextView(this);
                    TextView tv_customer = new TextView(this);
                    TextView tv_time = new TextView(this);

                    tv_table.setTextSize(12);
                    tv_table.setWidth(60);
                    tv_table.setGravity(Gravity.CENTER);
                    tv_table.setText(tableNo+"");

                    tv_headcount.setTextSize(12);
                    tv_headcount.setWidth(50);
                    tv_headcount.setGravity(Gravity.CENTER);
                    tv_headcount.setText(headcount+"");

                    tv_customer.setTextSize(12);
                    tv_customer.setWidth(50);
                    tv_customer.setGravity(Gravity.CENTER);
                    tv_customer.setText(customer+"");

                    tv_time.setTextSize(12);
                    tv_time.setGravity(Gravity.CENTER);
                    tv_time.setText(visitTime.substring(0, 10)+"&"+visitTime.substring(11, 19));

                    newRow.addView(tv_table, params);
                    newRow.addView(tv_headcount, params);
                    newRow.addView(tv_customer, params);
                    newRow.addView(tv_time, wparams);

                    TableRow btnRow = new TableRow(this);

                    Button btn_reject = new Button(this);

                    btn_reject.setText("취소");
                    btn_reject.setWidth(50);
                    btn_reject.setTextSize(10);
                    btn_reject.setGravity(Gravity.CENTER);
                    btn_reject.setHint(rsvID+"");
                    btn_reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Button me = (Button) view;
                            String visitID = me.getHint().toString();

                            HttpConnector ReserveConnector = new HttpConnector();
                            ReserveConnector.ConnectServer("rsv_id=" + visitID + "", "/reserve/accepted", "POST");

                            String httpCode = ReserveConnector.HttpResCode;
                            if (httpCode.equals("200")) {
                                Toast.makeText(ReserveListActivity.this, "처리 성공", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                            else {
                                Toast.makeText(ReserveListActivity.this, "처리 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    btnRow.addView(btn_reject, params);
                    newRow.addView(btnRow, params);

                    layout_rsvList.addView(newRow);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}