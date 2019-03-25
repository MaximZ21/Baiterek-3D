package com.example.android.opengl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void onClick3(View v) {
        Intent i = new Intent(this, OpenGLES20Activity.class);
        startActivity(i);
    }

    public void onClick1(View v) {
        Intent i = new Intent(this, InfoActivity.class);
        startActivity(i);
    }
}
