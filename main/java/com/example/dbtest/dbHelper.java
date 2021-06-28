package com.example.dbtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class dbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "User.db";
    public static final String TABLE_NAME1 = "user_details";
    public static final String TABLE_NAME2 = "login_details";
    public static final String TABLE_NAME3= "contact_details";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME1+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,EMAIL TEXT,NUMBER INTEGER,PASSWORD TEXT)");
        db.execSQL("create table "+TABLE_NAME2+" (ID INTEGER PRIMARY KEY ,NAME TEXT)");
        db.execSQL("create table "+TABLE_NAME3+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NUMBER INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        onCreate(db);
    }

    public boolean insertData(String name,String email,String number,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NAME",name);
        cv.put("EMAIL",email);
        cv.put("NUMBER",number);
        cv.put("PASSWORD",password);
        Long res=db.insert(TABLE_NAME1,null,cv);
        if(res==-1)
            return false;
        else
            return true;
    }

    public Cursor getData(String number, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from "+TABLE_NAME1+" where NUMBER = ? and PASSWORD = ?",new String[]{number,password});
        return results;
    }

    public boolean updateloginStatus(String id,String name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("ID",Integer.parseInt(id));
        cv.put("NAME",name);
        Long res=db.insert(TABLE_NAME2,null,cv);
        if(res==-1)
            return false;
        else
            return true;
    }

    public Cursor validateNumber(String number){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from "+TABLE_NAME1+" where NUMBER = ?",new String[]{number});
        return results;
    }

    public Cursor checkLoginStatus(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from "+TABLE_NAME2+" ",null);
        return results;
    }

    public boolean insertContactData(String number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NUMBER",number);
        Long res=db.insert(TABLE_NAME3,null,cv);
        if(res==-1)
            return false;
        else
            return true;
    }

    public String getEmerNum(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor results = db.rawQuery("Select * from "+TABLE_NAME3+" ",null);
        if(results.moveToNext())
        {
            return results.getString(1);
        }
        else
            return null;
    }

}
