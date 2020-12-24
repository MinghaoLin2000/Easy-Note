package com.example.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private NoteDatabase dbHelper;
    private Context context=this;
    private static final String TAG = "YenKoc";
    //浮动球
    FloatingActionButton  btn;
    TextView tv;
    private ListView lv;
    private NoteAdapter adapter;
    private List<Note> noteList=new ArrayList<>();
    private Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(FloatingActionButton)findViewById(R.id.fab);
        lv=findViewById(R.id.lv);
        myToolbar=findViewById(R.id.myToolBar);
        adapter=new NoteAdapter(getApplicationContext(),noteList);
        refreshListView();
        lv.setAdapter(adapter);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv.setOnItemClickListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("mode",4);
                startActivityForResult(intent,0);
                Log.d(TAG, "onClick: ");
            }
        });
    }


    // 接受startActivityForResult的结果
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,requestCode,data);
        String content=data.getStringExtra("content");
        String time=data.getStringExtra("time");
        int returnMode=data.getExtras().getInt("mode");
        if(returnMode==0)
        {
            Note note=new Note(content,time,1);
            CRUD op=new CRUD(context);
            op.open();
            op.addNote(note);
            op.close();
        }else if(returnMode==1)
        {
            long id=data.getExtras().getLong("id");
            Note note=new Note(content,time,1);
            note.setId(id);
            CRUD op=new CRUD(context);
            op.open();
            op.updateNote(note);
            op.close();
        }

        refreshListView();
    }
    private void refreshListView() {
        CRUD op=new CRUD(context);
        op.open();
        if (noteList.size() > 0) noteList.clear();
        noteList.addAll(op.getAllNotes());

        op.close();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv:
            Note curNote = (Note) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra("content", curNote.getContent());
            intent.putExtra("id", curNote.getId());
            intent.putExtra("time", curNote.getTime());
            intent.putExtra("mode",3);
            intent.putExtra("tag", curNote.getTag());
            startActivityForResult(intent, 1);
            break;
        }
    }
}