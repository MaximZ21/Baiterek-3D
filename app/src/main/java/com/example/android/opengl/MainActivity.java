package com.example.android.opengl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    Storage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        storage = Storage.getInstance();
        storage.getInfoTexts(this);
        storage.getInfoImages(this);

    }

    public void onClick3(View v) {
        Intent i = new Intent(this, OpenGLES20Activity.class);
        startActivity(i);
    }

    public void onClick1(View v) {
        Intent i = new Intent(this, InfoActivity.class);
        startActivity(i);
        storage.Print();
    }
}
