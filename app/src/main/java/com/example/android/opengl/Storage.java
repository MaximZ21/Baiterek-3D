package com.example.android.opengl;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

class Storage {
    private static final Storage ourInstance = new Storage();
    public LinkedList<String> info_texts;

    FirebaseDatabase database;
    DatabaseReference myRef;

    static Storage getInstance() {
        return ourInstance;
    }

    private Storage() {
        info_texts = new LinkedList<>();
    }

    public void getData(Context context){
        FirebaseApp.initializeApp(context);
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

    public void Print(){
        for(String i : info_texts){
            System.out.println(i);
        }
    }
}
