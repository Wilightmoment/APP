package com.example.royal.app;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Royal on 2017/7/23.
 */

public class HttpHandle {
    //取得類別名稱
    private static final String TAG=HttpHandle.class.getSimpleName();
    public HttpHandle(){}

    public String makeServiceCall(String reqUrl){
        String respons=null;
        try {
            URL url=new URL(reqUrl);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(con.getInputStream());
            respons = converStreamToString(in);
        }catch (Exception e){
            Log.e(TAG,"Exception:"+e.getMessage());
        }
        return respons;
    }

    private String converStreamToString(InputStream in){
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        try{
            while((line=reader.readLine())!=null){
                sb.append(line).append('\n');
            }
        }catch (Exception e){
            e.getMessage();
        }
        return sb.toString();
    }
}
