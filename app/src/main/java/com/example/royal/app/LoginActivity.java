package com.example.royal.app;

/**
 * Created by Royal on 2017/7/26.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AsyncTask<String,Void,String> {
    ArrayList<HashMap<String,String>> contactList=new ArrayList<HashMap<String,String>>();
    private static String url="http://192.168.0.2/check.php";
    private String TAG = MainActivity.class.getSimpleName();
    String user="",password="";

    private Context context;
    public LoginActivity(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... arg0){

        user = arg0[0];
        password = arg0[1];
        HttpHandle sh = new HttpHandle();
        String jsonstr = sh.makeServiceCall(url);
        if(jsonstr!=null){
            try{
                JSONArray ary=new JSONArray(jsonstr);
                for(int i=0 ;i<ary.length();i++){
                    JSONObject c=ary.getJSONObject(i);
                    String user = c.getString("user");
                    String password=c.getString("password");
                    HashMap<String,String>contact =new HashMap<>();
                    contact.put("user",user);
                    contact.put("password",password);
                    contactList.add(contact);
                }
            }catch (Exception e){
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }else{
            Log.e(TAG, "Couldn't get json from server.");
        }
        return null;
    }
    protected void onPostExecute(String result){
        int count=0;
        for(int i=0;i<contactList.size();i++){
            if(user.equals(contactList.get(i).get("user"))&&password.equals(contactList.get(i).get("password"))){
                Toast.makeText(context, "登入成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(context,GCMActivity.class);
                context.startActivity(intent);
            }else  count++;
        }
        if(count==contactList.size())
            Toast.makeText(context, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
    }
}

