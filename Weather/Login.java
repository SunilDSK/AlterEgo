package com.sunilkumar.alterego.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText userid,password;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userid = (EditText) findViewById(R.id.userid);
        password = (EditText) findViewById(R.id.passsword);

        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                String username = userid.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if(!username.isEmpty() && !pass.isEmpty()){

                }
                break;
        }
    }
}
