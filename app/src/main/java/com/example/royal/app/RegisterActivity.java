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
                    String User=user.getText().toString().trim();
                    String Password=password.getText().toString().trim();
                    if(User.equals("")||Password.equals("")){
                        Toast.makeText(RegisterActivity.this,"帳號或密碼不得為空值",Toast.LENGTH_SHORT).show();
                    }else{
                        new CheckActivity(RegisterActivity.this).execute(User,Password);
                        finish();
                    }
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

}
