package com.example.note;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.security.acl.NotOwnerException;
import java.util.ArrayList;
import java.util.List;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;


public class NoteAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    Drawable selected,unselected;
    private List<Note> backList; //原始数据
    private List<Note> noteList;//可以改变的数据
    private MyFilter mFilter;
    public NoteAdapter(Context mContext, List<Note> noteList)
    {
        this.mContext=mContext;
        this.noteList=noteList;
        backList=noteList;
    }


    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int i) {
        return noteList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mContext.setTheme(R.style.DayTheme);
        View v = View.inflate(mContext, R.layout.note_layout, null);
        TextView tv_content = (TextView)v.findViewById(R.id.tv_content);
        TextView tv_time = (TextView)v.findViewById(R.id.tv_time);

        //Set text for TextView
        String content = noteList.get(position).getContent();

        //if (sharedPreferences.getBoolean("noteTitle" ,true))
            //tv_content.setText(allText.split("\n")[0]);
        //else tv_content.setText(allText);
        tv_content.setText(content);
        tv_time.setText(noteList.get(position).getTime());

        //Save note id to tag
        v.setTag(noteList.get(position).getId());
        ImageView check=(ImageView)v.findViewById(R.id.select_checked);
        if(MainActivity.selectMode)
        {
            check.setVisibility(View.VISIBLE);
        }else
        {
            check.setVisibility(View.GONE);
        }

        if(noteList.get(position).checked)
        {
            Drawable drawable1=getDrawable(mContext,R.drawable.ic_checked);
            check.setImageDrawable(drawable1);
        }else {
            Drawable drawable2=getDrawable(mContext,R.drawable.ic_unchecked);
            check.setImageDrawable(drawable2);
        }



        return v;
    }


    @Override
    public Filter getFilter() {
        if (mFilter ==null){
            mFilter = new MyFilter();
        }
        return mFilter;
    }


    class MyFilter extends Filter {
        //我们在performFiltering(CharSequence charSequence)这个方法中定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<Note> list;
            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                list = backList;
            } else {//否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                for (Note note : backList) {
                    if (note.getContent().contains(charSequence)) {
                        list.add(note);
                    }

                }
            }
            result.values = list; //将得到的集合保存到FilterResults的value变量中
            result.count = list.size();//将集合的大小保存到FilterResults的count变量中

            return result;
        }
        //在publishResults方法中告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            noteList = (List<Note>)filterResults.values;
            if (filterResults.count>0){
                notifyDataSetChanged();//通知数据发生了改变
            }else {
                notifyDataSetInvalidated();//通知数据失效
            }
        }
    }

}
