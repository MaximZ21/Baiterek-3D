package com.example.android.opengl;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


class Storage {
    private static Storage ourInstance;
    public List<String> info_texts;
    public List<Question> questions;
    public List<Bitmap> info_images;
    public List<Bitmap> question_images;


    public boolean use_SQLite_data = false;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private StorageReference mStorageRef;
    Bitmap barr[];
    int images_downloaded;
    Bitmap qarr[];
    int question_images_downloaded;
    DatabaseHelper dh;

    public boolean download_images_complete = false;
    public boolean download_questioins_complete = false;

    static Storage getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new Storage(context);
        }
        return ourInstance;
    }

    private Storage(Context context) {
        dh = new DatabaseHelper(context);
        System.out.println("db created");
        info_texts = new LinkedList<>();
        info_images = new LinkedList<>();
        questions = new LinkedList<>();
        question_images = new LinkedList<>();
        images_downloaded = 0;
        question_images_downloaded = 0;
    }

    public void getInfoTexts(final Context context){
        FirebaseApp.initializeApp(context);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("info");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String text = String.valueOf(data.getValue());
                    info_texts.add(text);
                    Log.d("noticeme", text);
                }
                getInfoImages(context);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("noticeme", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addListenerForSingleValueEvent(postListener);
    }

    public void getInfoImages(Context context){
        Log.d("noticeme", "getInfoImages");
        try {
            FirebaseApp.initializeApp(context);
            final File localFile = File.createTempFile("images", "jpg");
            barr = new Bitmap[info_texts.size()];
            for(int i = 0; i < info_texts.size() ; i++) {
                getInfoImagesHelper g = new getInfoImagesHelper(i);
                g.start();
            }
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                public void run(){
                    if(images_downloaded == info_texts.size()){
                        for(Bitmap b : barr){
                            info_images.add(b);
                        }
                        dh.setInfo(info_texts,info_images);
                        System.out.println("+++");
                        System.out.println(Arrays.toString(dh.getInfoTexts().toArray()));
                        download_images_complete = true;
                    }
                    else{
                        handler.postDelayed(this,300);
                    }
                }
            }, 1);

        }catch(Exception ex){
            System.out.println("Exception in getInfoImages"+ex.getMessage());
        }
    }

    public class getInfoImagesHelper extends Thread {

        int i;

        public getInfoImagesHelper(int i){
            this.i = i;
        }

        public void run(){
            Log.d("noticeme","started");
            getInfoImagesHelper(i);
        }
    }



    public void getInfoImagesHelper(final int i){
        try {
            final File localFile = File.createTempFile("images", "jpg");
            StorageReference sRef = mStorageRef.child("i" + String.valueOf(i + 1) + ".jpg");
            sRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("noticeme", "Downloaded Image " + localFile.getName());
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            barr[i] = bitmap;
                            images_downloaded += 1;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("noticeme", "failed to load image N"+String.valueOf(i));
                }
            });
        }catch(Exception ex){
            //do nothing because lazy
        }
    }



    public void getQuestions(final Context context){
        FirebaseApp.initializeApp(context);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("questions");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String text = (data.child("question_text").getValue().toString());
                    String v1 = (data.child("v1").getValue().toString());
                    String v2 = (data.child("v2").getValue().toString());
                    String v3 = (data.child("v3").getValue().toString());
                    String v4 = (data.child("v4").getValue().toString());
                    Long c = (Long)data.child("correct").getValue();
                    int correct = c.intValue();
                    Question q = new Question(text,v1,v2,v3,v4,correct);
                    questions.add(q);
                }
                getQuestionImages(context);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("noticeme", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addListenerForSingleValueEvent(postListener);
    }

    public void getQuestionImages(Context context){
        Log.d("noticeme", "getQuestionImages");
        try {
            FirebaseApp.initializeApp(context);
            final File localFile = File.createTempFile("questionImages", "jpg");
            qarr = new Bitmap[questions.size()];
            for(int i = 0; i < questions.size() ; i++) {
                getQuestionImagesHelper g = new getQuestionImagesHelper(i);
                g.start();
            }
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                public void run(){
                    if(question_images_downloaded == questions.size()){
                        for(Bitmap b : qarr){
                            question_images.add(b);
                        }
                        dh.setQuestion(questions,question_images);
                        download_questioins_complete = true;
                    }
                    else{
                        handler.postDelayed(this,300);
                    }
                }
            }, 1);

        }catch(Exception ex){
            System.out.println("Exception in getQuestionImages"+ex.getMessage());
        }
    }

    public class getQuestionImagesHelper extends Thread {

        int i;

        public getQuestionImagesHelper(int i){
            this.i = i;
        }

        public void run(){
            Log.d("noticeme","started");
            getQuestionImagesHelper(i);
        }
    }



    public void getQuestionImagesHelper(final int i){
        try {
            final File localFile = File.createTempFile("images", "jpg");
            StorageReference sRef = mStorageRef.child("q" + String.valueOf(i + 1) + ".jpg");
            sRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("noticeme", "Downloaded Question Image " + localFile.getName());
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            qarr[i] = bitmap;
                            question_images_downloaded += 1;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("noticeme", "failed to load question image N"+String.valueOf(i));
                }
            });
        }catch(Exception ex){
            //do nothing because lazy
        }
    }


}
