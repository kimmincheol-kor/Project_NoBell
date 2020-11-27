package com.nobell.owner.activity.office;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nobell.owner.R;
import com.nobell.owner.model.HttpConnector;

public class AnswerActivity extends AppCompatActivity {

    static EditText et_content;
    LinearLayout layout_answer_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_answer);

        layout_answer_btn = (LinearLayout) findViewById(R.id.layout_answer_btn);
        et_content = (EditText) findViewById(R.id.et_answer);

        Intent myIntent = getIntent();
        final String operation = myIntent.getStringExtra("op");
        final String reviewID = myIntent.getStringExtra("reviewID");
        final String content = myIntent.getStringExtra("content");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
        params.weight = 1;

        if (operation.equals("1")) {
            Button btn_reg = new Button(this);
            btn_reg.setText("등록");
            btn_reg.setBackgroundColor(getResources().getColor(R.color.colorSkyBlue));
            btn_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String contents = AnswerActivity.et_content.getText().toString();

                    if (contents.length() > 0) {
                        HttpConnector AnswerConnector = new HttpConnector();
                        AnswerConnector.ConnectServer("reviewID=" + reviewID + "&content=" + contents, "/review", "POST");
                        ReviewActivity.reviewSelf.recreate();
                        finish();
                    }
                    else {

                    }
                }
            });

            layout_answer_btn.addView(btn_reg, params);
        } else {
            et_content.setText(content);

            Button btn_del = new Button(this);
            btn_del.setText("삭제");
            btn_del.setBackgroundColor(getResources().getColor(R.color.colorSkyBlue));
            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HttpConnector AnswerConnector = new HttpConnector();
                    AnswerConnector.ConnectServer("reviewID=" + reviewID, "/review", "DELETE");
                    ReviewActivity.reviewSelf.recreate();
                    finish();
                }
            });

            Button btn_edit = new Button(this);
            btn_edit.setText("수정");
            btn_edit.setBackgroundColor(getResources().getColor(R.color.colorSkyBlue));
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String contents = AnswerActivity.et_content.getText().toString();

                    if (contents.length() > 0) {
                        HttpConnector AnswerConnector = new HttpConnector();
                        AnswerConnector.ConnectServer("reviewID=" + reviewID + "&content=" + contents, "/review", "PUT");
                        ReviewActivity.reviewSelf.recreate();
                        finish();
                    }
                    else {

                    }
                }
            });

            layout_answer_btn.addView(btn_del, params);
            layout_answer_btn.addView(btn_edit, params);
        }
    }
}
