package com.example.android.opengl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        storage = Storage.getInstance();
        score = 0;
        page = 0;
        questionImageView = (ImageView)findViewById(R.id.questionImageView);
        questionTextView = (TextView)findViewById(R.id.questionTextView);
        bv1 = (Button)findViewById(R.id.bv1);
        bv2 = (Button)findViewById(R.id.bv2);
        bv3 = (Button)findViewById(R.id.bv3);
        bv4 = (Button)findViewById(R.id.bv4);
        questionTextView.setText(storage.questions.get(0).question_text);
        bv1.setText(storage.questions.get(0).v1);
        bv2.setText(storage.questions.get(0).v2);
        bv3.setText(storage.questions.get(0).v3);
        bv4.setText(storage.questions.get(0).v4);

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
            bv1.setText(storage.questions.get(page).v1);
            bv2.setText(storage.questions.get(page).v2);
            bv3.setText(storage.questions.get(page).v3);
            bv4.setText(storage.questions.get(page).v4);
            correct = storage.questions.get(page).correct;
        }else{
            endOfQuiz();
        }
    }


    public void endOfQuiz(){

    }
}
