package com.example.android.opengl;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.LinkedList;

public class StorageClass implements Serializable {

    public LinkedList<String> info_texts;

    FirebaseDatabase database;
    DatabaseReference myRef;

    public StorageClass(Context context){
        info_texts = new LinkedList<>();
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
