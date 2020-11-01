package com.nobell.owner.model;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnector {

    public String HttpIP = "http://192.168.219.102:3000/owner";

    public String HttpParam;
    public String HttpURL;
    public String HttpMethod;

    public String HttpResult;
    public String HttpResCode;

    public String ConnectServer(String param, String url, String method) {
        this.HttpParam = param;
        this.HttpURL = url;
        this.HttpMethod = method;

        // Making AsyncTask For Server
        ConnectTask connecttask = new ConnectTask();

        // Execute LoginTask
        try {
            HttpResult = connecttask.execute().get().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return HttpResult;
    }

    // AsyncTask For Connect Server
    public class ConnectTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = HttpParam;
            try {
                /* 서버연결 */
                URL url = new URL(HttpIP + HttpURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod(HttpMethod);
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                if(HttpMethod.equals("POST")) {
                    OutputStream outs = conn.getOutputStream();
                    outs.write(param.getBytes("UTF-8"));
                    outs.flush();
                    outs.close();
                }

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;
                String data = "";

                HttpResCode = Integer.toString(conn.getResponseCode());
                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                HttpResult = buff.toString();
                data = HttpResult.trim();
                Log.e("RECV CODE", HttpResCode);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return HttpResult;
        }
    }
}
