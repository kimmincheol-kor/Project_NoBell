package com.nobell.user;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class RestaurantView extends AppCompatActivity{

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_ADD = "address";
    private static final String TAG_INTRO = "intro";
    private static final String TAG_OPEN = "open";
    private static final String TAG_CLOSE = "close";
    private static final String TAG_OWNER = "owner";

    JSONArray ResView = null;

    ArrayList<HashMap<String, String>> ResList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);
        list = (ListView) findViewById(R.id.listView);
        ResList = new ArrayList<HashMap<String, String>>();
        getData("http://172.20.10.2/PHP_connection.php"); //수정 필요
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            ResView = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < ResView.length(); i++) {
                JSONObject c = ResView.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String phone = c.getString(TAG_PHONE);
                String address = c.getString(TAG_ADD);
                String intro = c.getString(TAG_INTRO);
                String open = c.getString(TAG_OPEN);
                String close = c.getString(TAG_CLOSE);
                String owner = c.getString(TAG_OWNER);
                HashMap<String, String> Res = new HashMap<String, String>();

                Res.put(TAG_ID, id);
                Res.put(TAG_NAME, name);
                Res.put(TAG_PHONE, phone);
                Res.put(TAG_ADD, address);
                Res.put(TAG_INTRO, intro);
                Res.put(TAG_OPEN, open);
                Res.put(TAG_CLOSE, close);
                Res.put(TAG_OWNER, owner);
                ResList.add(Res);
            }

            ListAdapter adapter = new SimpleAdapter(
                    RestaurantView.this, ResList, R.layout.activity_item_list,
                    new String[]{TAG_ID, TAG_NAME, TAG_PHONE,TAG_ADD,TAG_INTRO,TAG_OPEN,TAG_CLOSE,TAG_OWNER},
                    new int[]{R.id.id, R.id.name, R.id.phone ,R.id.address,R.id.intro,R.id.open,R.id.close,R.id.owner}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

}
