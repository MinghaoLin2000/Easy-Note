package com.example.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends BaseActivity {
    EditText et;
    private String old_content="";
    private String old_time="";
    private int old_Tag=1;
    private long id=0;
    private int openMode=0;
    private int tag=1;
    private Intent intent=new Intent();
    private Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        myToolbar=findViewById(R.id.myToolBar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et=findViewById(R.id.et);
        Intent getIntent=getIntent();
        openMode=getIntent.getIntExtra("mode",0);
        if(openMode==3)  //打开已有的笔记
        {
            id=getIntent.getLongExtra("id",0);
            old_content=getIntent.getStringExtra("content");
            old_time=getIntent.getStringExtra("time");
            old_Tag=getIntent.getIntExtra("tag",1);
            et.setText(old_content);
            et.setSelection(old_content.length());

        }

    }
    //当按下底下的三个按钮
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //主机键
        if(keyCode==KeyEvent.KEYCODE_HOME) {
            return true;
            //返回键
        }else if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            autoSetMessage();

            setResult(RESULT_OK,intent);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode,event);

    }


    public void autoSetMessage() {
        if(openMode==4) //当是一个新的笔记时
        {
            if(et.getText().toString().length()==0)
            {
                intent.putExtra("mode",-1); //没有任何事发生
            }else
            {
                intent.putExtra("content",et.getText().toString());
                intent.putExtra("time",dateToStr());
                intent.putExtra("mode",0);
            }
        }else
        {
            if(et.getText().toString().equals(old_content))
            {
                intent.putExtra("mode",-1); //没有动静
             }else
            {
                intent.putExtra("content",et.getText().toString());
                intent.putExtra("time",dateToStr());
                intent.putExtra("mode",1); //在原有的基础上进行修改
                intent.putExtra("id",id);
            }
        }


    }

    public String dateToStr(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

}