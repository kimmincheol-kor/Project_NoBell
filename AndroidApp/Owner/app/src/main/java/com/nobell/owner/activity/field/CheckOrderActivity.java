package com.nobell.owner.activity.field;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nobell.owner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class CheckOrderActivity extends AppCompatActivity {

    TableLayout layout_check_order;
    Button btn_check_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_check_order);

        layout_check_order = (TableLayout) findViewById(R.id.layout_check_order);
        btn_check_order = (Button) findViewById(R.id.btn_check_order);

        Intent intent = getIntent();
        String orderJSON = intent.getStringExtra("order");

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.weight = 1;

        try {
            JSONObject jsonOrder = new JSONObject(orderJSON);

            Iterator i = jsonOrder.keys();
            while(i.hasNext())
            {
                String menu = i.next().toString();
                int num = jsonOrder.getInt(menu);

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

                layout_check_order.addView(newRow);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_check_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
