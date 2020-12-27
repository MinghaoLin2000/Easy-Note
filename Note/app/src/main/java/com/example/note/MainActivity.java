package com.example.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.view.MenuItemCompat.getActionProvider;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public long uid;
    private NoteDatabase dbHelper;
    private Context context=this;
    private static final String TAG = "YenKoc";
    //浮动球
    FloatingActionButton  btn;
    TextView tv;
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private ListView lv;
    private NoteAdapter adapter;
    private List<Note> noteList=new ArrayList<>();
    private Toolbar myToolbar;
    private Button cancel;
    private Button delect_select;
    //是否在列表项中显示选择按钮
    public static boolean selectMode=false;
    //选择删除或者退出
    private View layout;
    //选择按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SysApplication.getInstance().addActivity(this);
        uid=getIntent().getLongExtra("uid",-1);
        Log.d(TAG, "uid:"+uid);
        btn=(FloatingActionButton)findViewById(R.id.fab);
        cancel=findViewById(R.id.cancel);
        delect_select=findViewById(R.id.delete_select);
        lv=findViewById(R.id.lv);
        layout=findViewById(R.id.select_bar);
        layout.setVisibility(View.GONE);
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
        //toolbar的返回按钮设置一波点击事件
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSearchAutoComplete.isShown())
                {
                    try{
                        mSearchAutoComplete.setText("");
                        Method method=mSearchView.getClass().getDeclaredMethod("onCloseClicked");
                        method.setAccessible(true);
                        method.invoke(mSearchView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    SysApplication.getInstance().exit();
                }

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note=(Note)adapter.getItem(i);
                note.checked=true;
                goSelectMode(true);

                return true;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectCancel(view);
            }
        });
        delect_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Note note:noteList)
                {
                    if(note.checked)
                    {
                        CRUD crud=new CRUD(context);
                        crud.open();
                        crud.removeNote(note);
                    }
                }
                onSelectCancel(view);
                refreshListView();
            }
        });


    }
    public void goSelectMode(boolean yes)
    {
        //进入选择模式时，显示操作按钮
        //退出时，隐藏按钮
        View layout=findViewById(R.id.select_bar);
        if(yes)
        {
            layout.setVisibility(View.VISIBLE); //出现
        }else
        {
            layout.setVisibility(View.GONE); //隐藏
        }
         //列表项要不要显示选择框
        this.selectMode=yes;
        adapter.notifyDataSetChanged();
    }
    public void onSelectCancel(View view)
    {
        //退出选择模式时，将所有的checked都设置成false
        for(Note note: noteList){
            note.checked=false;
        }
        goSelectMode(false);
    }
    public void secondRefreshListView(Cursor cursor) {
        List<Note> notes=new ArrayList<>();
        if(cursor!=null)
        {
            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    Note note=new Note();
                    note.setId(cursor.getLong(cursor.getColumnIndex(NoteDatabase.ID)));
                    note.setContent(cursor.getString(cursor.getColumnIndex(NoteDatabase.CONTENT)));
                    note.setTime(cursor.getString(cursor.getColumnIndex(NoteDatabase.TIME)));
                    note.setTag(cursor.getInt(cursor.getColumnIndex(NoteDatabase.MODE)));
                    notes.add(note);
                }
            }
            CRUD op=new CRUD(context);
            op.open();
            if (noteList.size() > 0) noteList.clear();
            noteList.addAll(notes);

            op.close();
            adapter.notifyDataSetChanged();
        }else
        {
            refreshListView();
        }


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
                    note.setUserid(uid);
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
        noteList.addAll(op.getAllNotesByUserid(uid));

        op.close();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv:
                if(selectMode)
                {
                    Note curNote = (Note) parent.getItemAtPosition(position);
                    curNote.checked=!curNote.checked;
                    adapter.notifyDataSetChanged();

                }else {
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
    //顶部菜单栏的显示以及改变

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view,menu);
        MenuItem searchItem=menu.findItem(R.id.menu_search);
        //菜单注销用户
        MenuItem zhuxiaouser=menu.findItem(R.id.scan_local_music);
        zhuxiaouser.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                return true;
            }
        });
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //通过id得到搜索框控件
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);

        //通过代码方式修改提示文字内容
        mSearchView.setQueryHint("搜索笔记");
        //设置搜索框有字时显示叉叉，无字时隐藏叉叉
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(true);
        //修改搜索框控件间的间隔（这样只是为了在细节上更加接近网易云音乐的搜索框）
        LinearLayout search_edit_frame = (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) search_edit_frame.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        search_edit_frame.setLayoutParams(params);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                CRUD crud=new CRUD(context);
                crud.open();
                Cursor cursor= TextUtils.isEmpty(newText)?null: crud.queryData(newText);
                secondRefreshListView(cursor);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(menu!=null)
        {
            if(menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder"))
            {
                try {
                    Method method=menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu,true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
}