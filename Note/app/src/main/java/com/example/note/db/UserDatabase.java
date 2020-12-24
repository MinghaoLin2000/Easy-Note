package com.example.note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase extends SQLiteOpenHelper {
    public static final String TABLE_NAME="user";
    public static final String USERNAME="username";
    public static final String ID="id";
    public static final String PASSWORD="password";
    public UserDatabase(Context context)
    {
       super(context,"users",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ USERNAME+" TEXT NOT NULL,"+PASSWORD+" TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
