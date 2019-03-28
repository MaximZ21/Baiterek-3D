package com.example.android.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.LinkedList;

class Storage {
    private static final Storage ourInstance = new Storage();
    public LinkedList<String> info_texts;
    public LinkedList<Question> questions;
    public LinkedList<Bitmap> info_images;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private StorageReference mStorageRef;

    static Storage getInstance() {
        return ourInstance;
    }

    private Storage() {
        info_texts = new LinkedList<>();
        info_images = new LinkedList<>();
        questions = new LinkedList<>();
    }

    public void getInfoTexts(Context context){
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
                }
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
        try {
            FirebaseApp.initializeApp(context);
            final File localFile = File.createTempFile("images", "jpg");
            for(int i = 0; i < info_texts.size() ; i++) {
                StorageReference sRef = mStorageRef.child("i"+String.valueOf(i+1)+".jpg");
                sRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.d("noticeme", "Downloaded Image " + localFile.getName());
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                info_images.add(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle failed download
                        // ...
                    }
                });
            }
        }catch(Exception ex){
            System.out.println("Exception in getInfoImages"+ex.getMessage());
        }
    }

    public void getQuestions(Context context){
        FirebaseApp.initializeApp(context);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("questions");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String text = String.valueOf(data.child("question_text"));
                    String v1 = String.valueOf(data.child("v1"));
                    String v2 = String.valueOf(data.child("v2"));
                    String v3 = String.valueOf(data.child("v3"));
                    String v4 = String.valueOf(data.child("v4"));
                    Question q = new Question(text,v1,v2,v3,v4);
                    questions.add(q);
                }
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

    public void Print(){
        for(String i : info_texts){
            System.out.println(i);
        }
    }
}
