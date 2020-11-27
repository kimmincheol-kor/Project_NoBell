package com.nobell.owner.activity.office;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nobell.owner.R;
import com.nobell.owner.activity.office.restaurant.EditMenuActivity;
import com.nobell.owner.activity.office.restaurant.MenuActivity;
import com.nobell.owner.model.HttpConnector;
import com.nobell.owner.model.RestaurantData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ReviewActivity extends AppCompatActivity {

    static Activity reviewSelf;
    LinearLayout layout_review;
    TextView rsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        reviewSelf = ReviewActivity.this;

        layout_review = (LinearLayout) findViewById(R.id.view_reviews);
        rsName = (TextView) findViewById(R.id.tv_review_rs);
        rsName.setText(RestaurantData.name);

        ////// Get Reviews From Server
        HttpConnector MenuConnector = new HttpConnector();
        MenuConnector.ConnectServer("", "/review", "GET");

        String httpCode = MenuConnector.HttpResCode;
        String httpResult = MenuConnector.HttpResult;

        if(httpCode.equals("200")) {
            // Parsing JSON
            try {
                JSONArray jArr = new JSONArray(httpResult);

                for(int i=0; i<jArr.length(); i++){
                    JSONObject jsonReview = jArr.getJSONObject(i);

                    /////////////////////////////////////////////

                    String reviewID = jsonReview.getString("review_id");
                    String reviewWriter = jsonReview.getString("review_customer");
                    String reviewContent = jsonReview.getString("review_content");
                    String reviewDate = jsonReview.getString("review_time");

                    TextView tv1 = new TextView(this);
                    tv1.setText("손님");
                    tv1.setTextSize(15);

                    TextView tv_review = new TextView(this);
                    tv_review.setTextSize(25);
                    tv_review.setText(reviewContent);

                    TextView tv_writer = new TextView(this);
                    tv_writer.setTextSize(15);
                    tv_writer.setText(reviewWriter);

                    TextView tv_rtime = new TextView(this);
                    tv_rtime.setTextSize(15);
                    tv_rtime.setText(reviewDate);

                    /////////////////////////////////////////////

                    String answerContent = jsonReview.getString("answer_content");
                    String answerDate = jsonReview.getString("answer_time");

                    TextView tv2 = new TextView(this);
                    tv2.setText("사장님");
                    tv2.setTextSize(15);
                    tv2.setGravity(Gravity.RIGHT);

                    TextView tv_answer = new TextView(this);
                    tv_answer.setText(answerContent);
                    tv_answer.setTextSize(25);
                    tv_answer.setHint(reviewID);
                    tv_answer.setGravity(Gravity.RIGHT);

                    TextView tv_atime = new TextView(this);
                    tv_atime.setText(answerDate);
                    tv_atime.setTextSize(15);
                    tv_atime.setGravity(Gravity.RIGHT);

                    /////////////////////////////////////////////

                    TextView tv_empty = new TextView(this);
                    tv_empty.setText("hidden");
                    tv_empty.setTextSize(10);
                    tv_empty.setVisibility(View.INVISIBLE);

                    layout_review.addView(tv1);
                    layout_review.addView(tv_review);
                    layout_review.addView(tv_writer);
                    layout_review.addView(tv_rtime);
                    if (jsonReview.getString("review_aid").equals("1")) {
                        tv_answer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TextView selAnswer = (TextView) view;
                                String reviewID = selAnswer.getHint().toString();
                                String content = selAnswer.getText().toString();

                                Intent intent = new Intent(ReviewActivity.this, AnswerActivity.class);
                                intent.putExtra("op", "1");
                                intent.putExtra("reviewID", reviewID);
                                intent.putExtra("content", content);
                                startActivity(intent);
                            }
                        });
                        layout_review.addView(tv_answer);
                    }
                    else {
                        tv_answer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TextView selAnswer = (TextView) view;
                                String reviewID = selAnswer.getHint().toString();
                                String content = selAnswer.getText().toString();

                                Intent intent = new Intent(ReviewActivity.this, AnswerActivity.class);
                                intent.putExtra("op", "2");
                                intent.putExtra("reviewID", reviewID);
                                intent.putExtra("content", content);
                                startActivity(intent);
                            }
                        });
                        layout_review.addView(tv2);
                        layout_review.addView(tv_answer);
                        layout_review.addView(tv_atime);
                    }
                    layout_review.addView(tv_empty);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
