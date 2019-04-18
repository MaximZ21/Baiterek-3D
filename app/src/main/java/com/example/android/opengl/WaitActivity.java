package com.example.android.opengl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class WaitActivity extends AppCompatActivity {

    AnimationDrawable aperture;
    Activity self_ref = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        ImageView apertureView = (ImageView) findViewById(R.id.aperture_view);
        apertureView.setImageResource(R.drawable.aperture_list);
        aperture = (AnimationDrawable) apertureView.getDrawable();
        aperture.start();
        final Storage storage = Storage.getInstance(this);
        if(!(storage.download_images_complete && storage.download_questioins_complete)){
            DatabaseHelper dh = new DatabaseHelper(this);
            dh.clear_db();
            storage.getInfoTexts(this);
            storage.getQuestions(this);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                if(storage.download_images_complete && storage.download_questioins_complete){
                    Intent i = new Intent(self_ref,MainActivity.class);
                    startActivity(i);
                }
                else{
                    handler.postDelayed(this,200);
                }
            }
        }, 1);
    }
}
