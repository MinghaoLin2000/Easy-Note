package com.example.note.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.note.Note;
import com.example.note.NoteDatabase;
import com.example.note.db.UserDatabase;
import com.example.note.domain.User;

public class UserCRUD {
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;
    private static final String[] columns={
            UserDatabase.ID,
            UserDatabase.USERNAME,
            UserDatabase.PASSWORD

    };
    public UserCRUD(Context context)
    {
        dbHandler=new UserDatabase(context);
    }
    public void open()
    {
        db=dbHandler.getWritableDatabase();
    }
    public void close()
    {
        dbHandler.close();
    }
    public User addNote(User user)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(UserDatabase.USERNAME,user.getUsername());
        contentValues.put(UserDatabase.PASSWORD,user.getPassword());
        long insertId=db.insert(UserDatabase.TABLE_NAME,null,contentValues);
        user.setId(insertId);
        return user;
    }

}
