package com.example.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button bt_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        opeartion();
    }
    public void initView()
    {
        bt_login=findViewById(R.id.bt_login);
    }
    public void opeartion()
    {
        bt_login.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_login:
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
        }

    }
}