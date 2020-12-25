package com.example.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDatabase extends SQLiteOpenHelper {
    public static final String TABLE_NAME="notes";
    public static final String CONTENT="content";
    public static final String ID="id";
    public static final String TIME="time";
    public static final String MODE="mode";
    public NoteDatabase(Context context)
    {
        super(context,"notes",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes(id INTEGER PRIMARY KEY AUTOINCREMENT,content TEXT NOT NULL,time TEXT NOT NULL,mode INTEGER DEFAULT 1)");
        db.execSQL("create table user(id integer primary key,username text,password text,bookeditor text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);

    }
}
