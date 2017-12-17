package com.twelo.mylist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int VERSION = 3;

    public static final String TABLE_NAME = "List";
    public static final String COL_TITLE = "Title";
    public static final String COL_ITEM = "item";
    public static final String COL_DATE = "date_time";

    public static final String ALL_LIST_TABLE = "ALL_LIST";
    public static final String ALL_LIST_KEY = "ITEM";

    public static final String HIDDEN_lIST = "HIDDEN_LIST";
    public static final String HIDDEN_TITLE = "HIDDEN_TITTLE";

    public static final String PROTECT_lIST = "PRO_LIST";
    public static final String PROTECT_TITLE = "PRO_TITTLE";

    private ArrayList list;



    Context public_context;

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.public_context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME + " ( " +
                COL_TITLE + " TEXT  , " +
                COL_ITEM + " TEXT  ," +
                COL_DATE + " TEXT Default \"No Data\"" +
                ");";

        db.execSQL(query);


        String qu_3 = "CREATE TABLE " + HIDDEN_lIST + " ( " +
                HIDDEN_TITLE + " TEXT UNIQUE" + " );";

        db.execSQL(qu_3);

        String qu_4 = "CREATE TABLE " + PROTECT_lIST + " ( " +
                PROTECT_TITLE + " TEXT UNIQUE" + " );";

        db.execSQL(qu_4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(this.public_context, "Database Updated :)", Toast.LENGTH_LONG).show();

        if (oldVersion == 1) {

            String qu_3 = "CREATE TABLE " + HIDDEN_lIST + " ( " +
                    HIDDEN_TITLE + " TEXT UNIQUE" + " );";

            db.execSQL(qu_3);

            String qu_4 = "CREATE TABLE " + PROTECT_lIST + " ( " +
                    PROTECT_TITLE + " TEXT UNIQUE" + " );";

            db.execSQL(qu_4);

            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_DATE + " TEXT DEFAULT \"No Data\" ;");
        }

        if (oldVersion == 2) {
            String qu_4 = "CREATE TABLE " + PROTECT_lIST + " ( " +
                    PROTECT_TITLE + " TEXT UNIQUE" + " );";

            db.execSQL(qu_4);
        }

    }


    public ArrayList getList() {
        return list;
    }

    public void add_hidden_list(String List) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        contentValues.put(HIDDEN_TITLE, List);
        db.insert(HIDDEN_lIST, null, contentValues);
    }

    public void add_protect_list(String List){
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        contentValues.put(PROTECT_TITLE, List);
        db.insert(PROTECT_lIST, null, contentValues);
    }


    public boolean add_list(List_maker list_maker) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        list = list_maker.getList();

        long res = 0;

        for (int i = 0; i < list_maker.getList().size(); i++) {

            contentValues.put(COL_TITLE, list_maker.getList_title());
            contentValues.put(COL_ITEM, list_maker.getList().get(i));
            contentValues.put(COL_DATE, list_maker.getDate().get(i));
            res = db.insert(TABLE_NAME, null, contentValues);
            contentValues = new ContentValues();
        }

        db.close();

        if (res == -1) {
            return false;
        } else {
            return false;
        }


    }

    public void Delete_Item(String Title, String item) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_TITLE + "=\"" + Title + "\" AND " + COL_ITEM + "=\"" + item + "\" ;");
    }

    public void Delete_List(String list) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_TITLE + " = \"" + list + "\"" + ";");
    }

    public void Delete_Hidden(String list) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + HIDDEN_lIST + " WHERE " + HIDDEN_TITLE + " = \"" + list + "\"" + ";");
    }

    public void Delete_protect(String list) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + PROTECT_lIST + " WHERE " + PROTECT_TITLE + " = \"" + list + "\"" + ";");
    }

    public Cursor Add_Delete_Item(String Title, String item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_TITLE, Title);
        contentValues.put(COL_ITEM, item);

        db.insert(TABLE_NAME, null, contentValues);

        Cursor cursor = db.rawQuery("SELECT " + COL_ITEM + " FROM " + TABLE_NAME + " WHERE " + COL_TITLE + " = \"" + Title + "\"" + ";", null);
        return cursor;

    }

    public Cursor getHiddenList() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + HIDDEN_TITLE + " FROM " + HIDDEN_lIST + ";", null);
        return cursor;
    }

    public Cursor getProtectList() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + PROTECT_TITLE + " FROM " + PROTECT_lIST + ";", null);
        return cursor;
    }

    public Cursor TitleToItem(String list) {

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COL_ITEM + " FROM " + TABLE_NAME + " WHERE " + COL_TITLE + " = \"" + list + "\"" + ";";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor TitleToDate(String list) {

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COL_DATE + " FROM " + TABLE_NAME + " WHERE " + COL_TITLE + " = \"" + list + "\"" + ";";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


    public boolean TitlePresent(String title) {
        SQLiteDatabase db = getWritableDatabase();

        String q = "SELECT " + COL_TITLE + " FROM " + TABLE_NAME + " ;";
        Cursor cursor = db.rawQuery(q, null);


        while (cursor.moveToNext()) {
            if (cursor.getString(0).toString().equals(title)) {
                return true;
            }
        }

        return false;
    }

    public Cursor DatabaseToTitle() {

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT DISTINCT " + COL_TITLE + " FROM " + TABLE_NAME + " ;";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


}
