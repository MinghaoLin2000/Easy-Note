package com.example.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button bt_login;
    TextView et_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hideStatsuBar();
        fullScreen();
        initView();
        opeartion();
    }
    public void initView()
    {
        bt_login=findViewById(R.id.bt_login);
        et_register=findViewById(R.id.et_register);
    }
    public void opeartion()
    {
        bt_login.setOnClickListener(this);
        et_register.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_login:
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.et_register:
                Intent intent1=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent1);
                break;

        }

    }
    /**
     * 隐藏虚拟按键并全屏
     */
    protected void fullScreen()
    {
        //设置界面全屏
        //获取decorView
        View decorView=getWindow().getDecorView();
        //判断版本
        if(Build.VERSION.SDK_INT>11&&Build.VERSION.SDK_INT<19)
        {
            //11-18
            decorView.setSystemUiVisibility(View.GONE);
        }else if(Build.VERSION.SDK_INT>=19)
        {
            //19以及以上版本
            //SYSTEM_UI_FLAG_HIDE_NAVIGATION:隐藏导航栏
            //SYSTEM_UI_FLAG_IMMERSIVE_STICKY:从状态栏下拉会半透明悬浮显示一会儿状态栏和导航栏
            //SYSTEM_UI_FALG_FUULLSCREEN:全屏
            int options=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            //设置到控件
            decorView.setSystemUiVisibility(options);
        }
    }
    /**
     * 隐藏状态栏
     *
     */
    protected void hideStatsuBar()
    {
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}