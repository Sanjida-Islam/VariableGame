package com.example.variablegame;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Quiz extends AppCompatActivity {
    TextView quiz, timer, option1, option2, option3, option4;
    ProgressBar progressBar;

    private static final long COUNTDOWN_IN_MILLIS = 4000;


    // private static final String KEY_PROGRESS = "keyProgress";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;


    private ColorStateList textColorDefault;
    private ArrayList<Question> questionList;

    private Question currentQuestion;
    private int questionCounter;
    private int questionCountTotal;
    private Question CurrentQuestion;
    private boolean answered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        progressBar = findViewById(R.id.progressBar);
        timer = findViewById(R.id.timer);
        quiz = findViewById(R.id.quiz);
        option1 = findViewById(R.id.number);
        option2 = findViewById(R.id.string);
        option3 = findViewById(R.id.bolean);
        option4 = findViewById(R.id.incorrect);


        textColorDefault = option1.getTextColors();
        textColorDefault = option2.getTextColors();
        textColorDefault = option3.getTextColors();
        textColorDefault = option4.getTextColors();


        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestion();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        clickunable();

        Runnable r = new Runnable() {
            @Override
            public void run() {

                showNextQuestion();
            }

        };
        Handler h = new Handler();
        h.postDelayed(r, 2500);
    }


    private void showNextQuestion() {
        clickable();

        option1.setTextColor(textColorDefault);
        option2.setTextColor(textColorDefault);
        option3.setTextColor(textColorDefault);
        option4.setTextColor(textColorDefault);

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            quiz.setText(currentQuestion.getQuestion());
            option1.setText(currentQuestion.getOption1());
            option2.setText(currentQuestion.getOption2());
            option3.setText(currentQuestion.getOption3());
            option4.setText(currentQuestion.getOption4());

            questionCounter++;
            answered = false;

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {

            finishQuiz();
        }
    }


    private void startCountDown() {
        progressBar.setProgress(4);
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                int progress = (int) (millisUntilFinished / 50);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(1);
                showNext();
            }
        }.start();
    }

    private void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d", seconds);

        timer.setText(timeFormatted);

    }


    private void checkAnswer(final int ansNmb) {

        countDownTimer.cancel();
        answered = true;

        if (ansNmb == currentQuestion.getAnsNumber()) {

            switch (currentQuestion.getAnsNumber()) {
                case 1:
                    option1.setTextColor(Color.GREEN);
                    option1.setBackgroundResource(R.drawable.rightans);
                    Toast.makeText(Quiz.this, "Right answer", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    option2.setTextColor(Color.GREEN);
                    option2.setBackgroundResource(R.drawable.rightans);
                    Toast.makeText(Quiz.this, "Right answer", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    option3.setTextColor(Color.GREEN);
                    option3.setBackgroundResource(R.drawable.rightans);
                    Toast.makeText(Quiz.this, "Right answer", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    option4.setTextColor(Color.GREEN);
                    option4.setBackgroundResource(R.drawable.rightans);
                    Toast.makeText(Quiz.this, "Right answer", Toast.LENGTH_SHORT).show();
                    break;
            }
            showNext();
        } else {
            Toast.makeText(Quiz.this, "wrong answer", Toast.LENGTH_SHORT).show();
            showNext();
        }


    }


    private void showNext() {

        Runnable r = new Runnable() {
            @Override
            public void run() {

                if (questionCounter < questionCountTotal) {

                    clickable();
                    optionbackground();
                    showNextQuestion();
                } else {
                    Intent intent = new Intent(Quiz.this, Finish.class);
                    startActivity(intent);
                    finish();
                }
            }

        };
        Handler h = new Handler();
        h.postDelayed(r, 2500);

    }

    private void finishQuiz() {

        finish();
    }


    public void onClick(View v) {

        int ansNmb;
        switch (v.getId()) {

            case R.id.number:
                ansNmb = 1;
                option1.setTextColor(Color.RED);
                option1.setBackgroundResource(R.drawable.wrongans);
                clickunable();
                checkAnswer(ansNmb);
                break;
            case R.id.string:
                ansNmb = 2;
                option2.setTextColor(Color.RED);
                option2.setBackgroundResource(R.drawable.wrongans);
                clickunable();
                checkAnswer(ansNmb);
                break;
            case R.id.bolean:
                ansNmb = 3;
                option3.setTextColor(Color.RED);
                option3.setBackgroundResource(R.drawable.wrongans);
                clickunable();
                checkAnswer(ansNmb);
                break;
            case R.id.incorrect:
                ansNmb = 4;
                option4.setTextColor(Color.RED);
                option4.setBackgroundResource(R.drawable.wrongans);
                clickunable();
                checkAnswer(ansNmb);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void clickable() {
        option1.setClickable(true);
        option2.setClickable(true);
        option3.setClickable(true);
        option4.setClickable(true);
    }

    private void clickunable() {
        option1.setClickable(false);
        option2.setClickable(false);
        option3.setClickable(false);
        option4.setClickable(false);
    }

    private void optionbackground() {
        option1.setBackgroundResource(R.drawable.optionbtnstyle);
        option2.setBackgroundResource(R.drawable.optionbtnstyle);
        option3.setBackgroundResource(R.drawable.optionbtnstyle);
        option4.setBackgroundResource(R.drawable.optionbtnstyle);
    }


}
