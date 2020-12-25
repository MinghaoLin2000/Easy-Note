package com.example.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.core.app.NavUtils;

import com.example.note.db.UserDatabase;
import com.example.note.domain.User;

import java.util.ArrayList;
import java.util.List;

public class CRUD {
    public static int check=0;
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;
    private static final String[] User_columns={
            UserDatabase.ID,
            UserDatabase.USERNAME,
            UserDatabase.PASSWORD

    };
    private static final String[] Note_columns={
            NoteDatabase.ID,
            NoteDatabase.CONTENT,
            NoteDatabase.TIME,
            NoteDatabase.MODE
    };
    public CRUD(Context context)
    {
        dbHandler=new NoteDatabase(context);
    }
    public void open()
    {
        db=dbHandler.getWritableDatabase();
    }
    public void close()
    {
        dbHandler.close();
    }
    public Note addNote(Note note)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(NoteDatabase.CONTENT,note.getContent());
        contentValues.put(NoteDatabase.TIME,note.getTime());
        contentValues.put(NoteDatabase.MODE,note.getTag());
        long insertId=db.insert(NoteDatabase.TABLE_NAME,null,contentValues);
        note.setId(insertId);
        return note;
    }
    public Note getNote(long id)
    {
        Cursor cursor=db.query(NoteDatabase.TABLE_NAME,Note_columns,NoteDatabase.ID+"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null)
        {
            cursor.moveToFirst();

        }
        Note e =new Note(cursor.getString(1),cursor.getString(2),cursor.getInt(3));
        return e;

    }
    public Cursor queryData(String key) {
        Cursor cursor = null;
        try {
            String querySql = "select * from notes where content like '%" + key + "%'";
            cursor = db.rawQuery(querySql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }
    public List<Note> getAllNotes()
    {
        Cursor cursor=db.query(NoteDatabase.TABLE_NAME,Note_columns,null,null,null,null,null);
        List<Note> notes=new ArrayList<>();
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
        return notes;
    }
    public int updateNote(Note note)
    {
        ContentValues values=new ContentValues();
        values.put(NoteDatabase.CONTENT,note.getContent());
        values.put(NoteDatabase.TIME,note.getTime());
        values.put(NoteDatabase.MODE,note.getTag());
        //更新数据库
        return db.update(NoteDatabase.TABLE_NAME,values,NoteDatabase.ID+"="+note.getId(),null);
    }
    public void removeNote(Note note)
    {
        db.delete(NoteDatabase.TABLE_NAME,NoteDatabase.ID+"="+note.getId(),null);
    }
    /**
     * 用户的crud操作
     * @param
     * @return
     */
    public User addUser(User user)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(UserDatabase.USERNAME,user.getUsername());
        contentValues.put(UserDatabase.PASSWORD,user.getPassword());
        long insertId=db.insert(UserDatabase.TABLE_NAME,null,contentValues);
        user.setId(insertId);

        return user;
    }


    public User getUser(long id)
    {
        Cursor cursor=db.query(UserDatabase.TABLE_NAME,User_columns,UserDatabase.ID+"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        User user =new User(cursor.getString(1),cursor.getString(2));
        return user;
    }
    public User getUserByName(String name)
    {
        Cursor cursor=db.query(UserDatabase.TABLE_NAME,User_columns,UserDatabase.USERNAME+"=?",new String[]{name},null,null,null,null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            User user =new User(cursor.getString(1),cursor.getString(2));
            return user;
        }
        return null;

    }




}
