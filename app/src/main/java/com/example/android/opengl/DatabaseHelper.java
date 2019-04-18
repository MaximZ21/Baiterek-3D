package com.example.android.opengl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context,"Data",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" CREATE TABLE INFO ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + " INFO_TEXT TEXT , "
                + " INFO_IMAGE TEXT );");
        db.execSQL(" CREATE TABLE QUESTION ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + " QUESTION_TEXT TEXT , "
                + " v1 TEXT , "
                + " v2 TEXT , "
                + " v3 TEXT , "
                + " v4 TEXT , "
                + " correct INT , "
                + " QUESTION_IMAGE TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void clear_db(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM INFO");
        db.execSQL("DELETE FROM QUESTION");
    }

    public void setInfo(List<String> texts, List<Bitmap> bitmaps){
        SQLiteDatabase db = this.getWritableDatabase();
        if(texts.size() != bitmaps.size()){
            Log.e("","PROBLEM IN DB SETINFO");
        } else {
            for (int i = 0 ; i < texts.size() ; i++){
                Log.e("DB","set1");
                String text = texts.get(i);
                ContentValues values = new ContentValues();
                values.put("INFO_TEXT", text);
                Bitmap bitmap = bitmaps.get(i);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                values.put("INFO_IMAGE", stream.toByteArray());
                db.insert("INFO", null, values);
            }
        }
        db.close();

    }

    public List<String> getInfoTexts(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> texts = new ArrayList<>();
        Cursor cursor = db.query("INFO",new String[]{"INFO_TEXT"},
                null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Log.e("DB","get1");
                texts.add(cursor.getString(cursor.getColumnIndex("INFO_TEXT")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return texts;
    }

    public List<Bitmap> getInfoImages(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        Cursor cursor = db.query("INFO",new String[]{"INFO_IMAGE"},
                null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Bitmap bitmap = BitmapFactory.decodeByteArray(cursor.getBlob(cursor.getColumnIndex("INFO_IMAGE")),
                        0,cursor.getBlob(cursor.getColumnIndex("INFO_IMAGE")).length);
                bitmaps.add(bitmap);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bitmaps;
    }

    public void setQuestion(List<Question> questions, List<Bitmap> bitmaps){
        SQLiteDatabase db = this.getWritableDatabase();
        if(questions.size() != bitmaps.size()){
            Log.e("","PROBLEM IN DB SETQUESTION");
        } else {
            for (int i = 0 ; i < questions.size() ; i++){
                Question question = questions.get(i);
                ContentValues values = new ContentValues();
                values.put("QUESTION_TEXT", question.question_text);
                Bitmap bitmap = bitmaps.get(i);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                values.put("QUESTION_IMAGE", stream.toByteArray());
                values.put("v1",question.v1);
                values.put("v2",question.v2);
                values.put("v3",question.v3);
                values.put("v4",question.v4);
                values.put("correct",question.correct);
                db.insert("QUESTION", null, values);
            }
        }
        db.close();

    }

    public List<Question> getQuestions(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Question> questions = new ArrayList<>();
        Cursor cursor = db.query("QUESTION",new String[]{"QUESTION_TEXT",
                "v1","v2","v3","v4","correct"},
                null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {

                questions.add(new Question(
                        cursor.getString(cursor.getColumnIndex("QUESTION_TEXT")),
                        cursor.getString(cursor.getColumnIndex("v1")),
                        cursor.getString(cursor.getColumnIndex("v2")),
                        cursor.getString(cursor.getColumnIndex("v3")),
                        cursor.getString(cursor.getColumnIndex("v4")),
                        cursor.getInt(cursor.getColumnIndex("correct"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return questions;
    }

    public List<Bitmap> getQuestionImages(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        Cursor cursor = db.query("QUESTION",new String[]{"QUESTION_IMAGE"},
                null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                Bitmap bitmap = BitmapFactory.decodeByteArray(cursor.getBlob(cursor.getColumnIndex("QUESTION_IMAGE")),
                        0,cursor.getBlob(cursor.getColumnIndex("QUESTION_IMAGE")).length);
                bitmaps.add(bitmap);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bitmaps;
    }
}
