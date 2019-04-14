package com.example.android.opengl;

public class Question {

    String question_text;
    String v1;
    String v2;
    String v3;
    String v4;
    int correct;

    public Question(String question_text, String v1, String v2, String v3, String v4, int correct) {
        this.question_text = question_text;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.correct = correct;
    }
}
