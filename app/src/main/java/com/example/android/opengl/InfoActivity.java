package com.example.android.opengl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView textView;
    Storage storage = Storage.getInstance();
    Button backButton;
    Button nextButton;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        textView = (TextView)findViewById(R.id.textView);
        textView.setText(storage.info_texts.get(0));
        page = 0;
        backButton = (Button)findViewById(R.id.button1);
        nextButton = (Button)findViewById(R.id.button2);
        backButton.setVisibility(View.INVISIBLE);
    }

    public void BackButtonClick(View v){
        page -= 1;
        if(page==0){
            backButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
        if(page==storage.info_texts.size()-1){
            nextButton.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.VISIBLE);
        }
        if(page > 0 && page < storage.info_texts.size()-1){
            nextButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        }
        textView.setText(storage.info_texts.get(page));
    }

    public void nextButtonClick(View v){
        page += 1;
        if(page==0){
            backButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
        if(page==storage.info_texts.size()-1){
            nextButton.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.VISIBLE);
        }
        if(page > 0 && page < storage.info_texts.size()-1){
            nextButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        }
        textView.setText(storage.info_texts.get(page));
    }




}
