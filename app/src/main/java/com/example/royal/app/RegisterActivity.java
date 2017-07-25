package com.example.royal.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RegisterActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private EditText user,password;
    private Button register;
    private ProgressDialog pDialog;

    private static String url="http://192.168.0.2/check.php";
    ArrayList<HashMap<String,String>> contactList=new ArrayList<HashMap<String,String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();
        new GetContacts().execute();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(int i=0;i<contactList.size();i++){
                    if(user.getText().toString().equals(contactList.get(i).get("user"))){
                        count++;
                    }
                }
                if(count!=0){
                    Toast.makeText(RegisterActivity.this,"帳號已被註冊",Toast.LENGTH_SHORT).show();
                }else{
                    String str=doregister();
                    Toast.makeText(RegisterActivity.this,str,Toast.LENGTH_SHORT).show();
                    if(str!=null){
                        try{
                            JSONObject jsb=new JSONObject(str);
                            String result= jsb.getString("query_result");
                            if(result.equals("SUCCESS")){
                                Toast.makeText(RegisterActivity.this,"註冊成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterActivity.this,"註冊失敗",Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this,"網頁空值",Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(RegisterActivity.this,"註冊成功",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findView(){
        user=(EditText)findViewById(R.id.et_user);
        password=(EditText)findViewById(R.id.et_password);
        register=(Button)findViewById(R.id.btn_register);
    }

    private class GetContacts extends AsyncTask<Void,Void,Void>{

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... args){
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    private String doregister(){
        String data,link;
        String User=user.getText().toString();
        String Password=password.getText().toString();
        BufferedReader bufferedReader;

        try {
            data = "?user="+URLEncoder.encode(User,"utf8");
            data += "&password="+URLEncoder.encode(Password,"utf8");
            link = "http://192.168.0.2/insert.php"+data;
            HttpHandle Sh = new HttpHandle();
            String result = Sh.makeServiceCall(link);
            return result;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }
}
