package com.example.android.opengl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends AppCompatActivity {

    ImageView questionImageView;
    TextView questionTextView;
    Button bv1;
    Button bv2;
    Button bv3;
    Button bv4;
    int page;
    Storage storage;
    int correct;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        storage = Storage.getInstance(this);
        score = 0;
        page = 0;
        questionImageView = (ImageView)findViewById(R.id.questionImageView);
        questionTextView = (TextView)findViewById(R.id.questionTextView);
        bv1 = (Button)findViewById(R.id.bv1);
        bv2 = (Button)findViewById(R.id.bv2);
        bv3 = (Button)findViewById(R.id.bv3);
        bv4 = (Button)findViewById(R.id.bv4);
        questionTextView.setText(storage.questions.get(0).question_text);
        questionImageView.setImageBitmap(storage.question_images.get(page));
        bv1.setText(storage.questions.get(0).v1);
        bv2.setText(storage.questions.get(0).v2);
        bv3.setText(storage.questions.get(0).v3);
        bv4.setText(storage.questions.get(0).v4);
        correct = storage.questions.get(0).correct;
    }

    public void nextQuestion(View v){
        if(String.valueOf(correct).equals(v.getTag())){
            Toast.makeText(this,"CORRECT!",Toast.LENGTH_SHORT).show();
            score+=1;
        } else {
            Toast.makeText(this,"WRONG!",Toast.LENGTH_SHORT).show();
        }
        page += 1;
        if(page < storage.questions.size()) {
            questionTextView.setText(storage.questions.get(page).question_text);
            questionImageView.setImageBitmap(storage.question_images.get(page));
            bv1.setText(storage.questions.get(page).v1);
            bv2.setText(storage.questions.get(page).v2);
            bv3.setText(storage.questions.get(page).v3);
            bv4.setText(storage.questions.get(page).v4);
            correct = storage.questions.get(page).correct;
        }else{
            Log.d("question","end");
            endOfQuiz();
        }
    }


    public void endOfQuiz(){
        Toast.makeText(this,"The Test is over. Your score is "+score+" out of "+storage.questions.size(),Toast.LENGTH_LONG).show();
    }
}
