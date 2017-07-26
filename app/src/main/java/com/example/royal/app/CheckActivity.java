package com.example.royal.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by user on 2017/7/25.
 */

public class CheckActivity extends AsyncTask<String,Void,String> {
    private Context context;

    public CheckActivity(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {}
    protected String doInBackground(String... arg0){
        String user = arg0[0];
        String password = arg0[1];
        String link;
        String data;
        BufferedReader bufferedReader;
        String result;
        try {
            data = "?user="+ URLEncoder.encode(user,"utf8");
            data += "&password="+URLEncoder.encode(password,"utf8");
            link = "http://192.168.0.2/insert.php"+data;
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            result = bufferedReader.readLine();
        } catch (Exception e){
            return new String("Exception: " + e.getMessage());
        }
        return result;
    }
    protected void onPostExecute(String result){
        String jsonStr = result;
        Log.d("result=",result);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String query_result = jsonObj.getString("query_result");
                if (query_result.equals("SUCCESS")) {
                    Toast.makeText(context, "會員記錄建檔成功", Toast.LENGTH_SHORT).show();
                }else if (query_result.equals("FAILURE")) {
                    Toast.makeText(context, "會員記錄建檔失敗", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "會員記錄建檔成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }
    }
}

