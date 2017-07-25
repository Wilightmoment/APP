package com.example.royal.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText user,password;
    private Button login,register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        Listen();
    }
    private void findView(){
        user=(EditText)findViewById(R.id.et_user);
        password=(EditText)findViewById(R.id.et_password);
        login=(Button)findViewById(R.id.btn_login);
        register=(Button)findViewById(R.id.btn_register);
    }

    private void Listen(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register= new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(register);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
