package com.example.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.note.crud.UserCRUD;
import com.example.note.db.UserDatabase;
import com.example.note.domain.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private UserDatabase dbHelper;
    private EditText et_username;
    private EditText et_password;
    private EditText et_confirm_password;
    private Button bt_register_submit;
    private Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        operation();

    }
    public void initView()
    {
        et_username=findViewById(R.id.et_register_username);
        et_password=findViewById(R.id.et_register_password);
        et_confirm_password=findViewById(R.id.et_register_comfirm_password);
        bt_register_submit=findViewById(R.id.bt_register_submit);
    }
    public void operation()
    {
        bt_register_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_register_submit:
                String password=et_password.getText().toString();
                String confirm_password=et_confirm_password.getText().toString();
                String username=et_username.getText().toString();
                if(!password.equals(confirm_password))
                {
                    Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }else
                {
                    UserCRUD crud=new UserCRUD(context);
                    User user=new User(username,password);
                    crud.open();
                    crud.addNote(user);
                    crud.close();
                 Toast.makeText(this, "注册成功,快去登陆", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    finish();
                }
        }
    }
}