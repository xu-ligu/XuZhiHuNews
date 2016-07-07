package com.example.administrator.myzzhihuday.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class WebCacheDatabase extends SQLiteOpenHelper{
    public WebCacheDatabase(Context context,int version){
        super(context,"webCache.db",null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("create table if not exists WebCache (id INTEGER primary key autoincrement,newsId INTEGER unique,json text");
        db.execSQL("create table if not exists WebCache (id INTEGER primary key autoincrement,newsId INTEGER unique,json text)");

      //  db.execSQL("create table if not exists Cache(id INTEGER primary key autoincrement,date INTEGER unique,json text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
