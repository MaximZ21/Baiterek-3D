package com.example.android.opengl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

public class ChoiseActivity extends AppCompatActivity {

    Button local;
    Button internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise);

        local = (Button) findViewById(R.id.local);
        internet = (Button) findViewById(R.id.internet);
    }

    public void local(View v){
        Storage storage = Storage.getInstance(this);
        DatabaseHelper dh = new DatabaseHelper(this);
        storage.info_texts = dh.getInfoTexts();
        System.out.println(Arrays.toString(storage.info_texts.toArray()));
        storage.questions = dh.getQuestions();
        storage.info_images = dh.getInfoImages();
        storage.question_images = dh.getQuestionImages();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void internet(View v){
        Intent i = new Intent(this, WaitActivity.class);
        startActivity(i);
    }
}
