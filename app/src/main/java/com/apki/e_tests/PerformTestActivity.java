package com.apki.e_tests;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.snapshot.Index;

import java.util.ArrayList;

public class PerformTestActivity extends AppCompatActivity {

    private Test test;
    private int questionNumber = 0;
    private LinearLayout ansContainer;
    private Button nextQuestionButton, prevQuestionButton, endTestButton;
    private boolean checks [][] ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_test);

        ansContainer = findViewById(R.id.answerContainerPerform);
        test = (Test) getIntent().getSerializableExtra("TEST");
        checks = new boolean[test.getQuestions().size()][];

        setTitle(test.getTitle());
        configurePrevButton();
        configureNextButton();
        configureEndTestButton();
        disableButtonsIfNecessary();
        initializeQuestion(questionNumber);
    }

    public void configurePrevButton(){
        prevQuestionButton = findViewById(R.id.previousQuestion);
        prevQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnswers();
                questionNumber--;
                changeQuestion();
                retrieveAnswers();
            }
        });
    }

    public void configureNextButton(){
        nextQuestionButton = findViewById(R.id.nextQuestion);
        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnswers();
                questionNumber++;
                changeQuestion();
                retrieveAnswers();
            }
        });
    }

    public void configureEndTestButton(){
        endTestButton = findViewById(R.id.endTest);
        endTestButton.setEnabled(false);
        endTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTest();
            }
        });
    }

    public void configureReturnButton(){
        Button returnButton = findViewById(R.id.returnToMain);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void endTest(){
        saveAnswers();
        float score = calculatePercentage();
        setContentView(R.layout.conclusion_perform_test);
        TextView scoreView = findViewById(R.id.points);
        scoreView.setText(Math.ceil(score * 100) +" %" );
        configureReturnButton();
    }

    private float calculatePercentage(){
        ArrayList<Question> quest = test.getQuestions();
        Question currentQ;



        float out = 0;

        for(int i = 0; i < checks.length; i++){
            float correct = 0;
            float all = 0;
            currentQ = quest.get(i);
            ArrayList<Boolean> correctAnswers = currentQ.getChecks();
            for( int j = 0; j < checks[i].length; j++){
                Log.d("j", j + "");

                if (correctAnswers.get(j)){
                    all++;
                }

                if (checks[i][j] && correctAnswers.get(j)){
                    correct++;
                }
            }
            out += correct / all;
        }
        return out / checks.length;
    }

    private void changeQuestion(){
        disableButtonsIfNecessary();
        questionNumber = clampValue(questionNumber, 0, test.getQuestions().size() - 1);
        initializeQuestion(questionNumber);
    }

    private void saveAnswers(){
        int childCount = ansContainer.getChildCount();
        boolean currentAnswers [] = new boolean[childCount];
        for (int i = 0; i < childCount; i++){
            LinearLayout child = (LinearLayout) ansContainer.getChildAt(i);
            CheckBox checkBox = child.findViewById(R.id.checkBox);
            currentAnswers[i] = checkBox.isChecked();
        }
        checks[questionNumber] = currentAnswers;
    }

    private void retrieveAnswers(){
        int childCount = ansContainer.getChildCount();
        boolean  currentAnswers [];
        try{
            currentAnswers = checks[questionNumber];
            for (int i = 0; i < childCount; i++){
                LinearLayout child = (LinearLayout) ansContainer.getChildAt(i);
                CheckBox checkBox = child.findViewById(R.id.checkBox);
                checkBox.setChecked(currentAnswers[i]);
            }

        }catch(Exception e){
            Log.d("retrieveAnswers", e.toString());
        }

    }

    public int clampValue(int value, int min, int max){
        int _value = value;
        if(_value <= min){
            _value = min;
        }

        if (_value >= max){
            _value = max;
        }
        return _value;

    }

    private void disableButtonsIfNecessary(){
        if (questionNumber <= 0){
            prevQuestionButton.setEnabled(false);
        } else{
            prevQuestionButton.setEnabled(true);
        }

        if (questionNumber >= test.getQuestions().size() - 1){
            nextQuestionButton.setEnabled(false);
            endTestButton.setEnabled(true);
        } else{
            nextQuestionButton.setEnabled(true);
        }
    }

    public void initializeQuestion(int questionNumber){
        ansContainer.removeAllViews();
        ArrayList<Question> quest = test.getQuestions();
        Question firstQuestion;
        try{
            firstQuestion = quest.get(questionNumber);
        }catch (IndexOutOfBoundsException e){
            Log.d("InitializeQuestion", e.toString());
            return;
        }

        TextView questionTextView = findViewById(R.id.questionText);
        TextView questionNumberText = findViewById(R.id.questionNumber);
        questionNumberText.setText(firstQuestion.getLabel());
        questionTextView.setText(firstQuestion.getSentence());
        for (String answer: firstQuestion.getAnswers()) {
            View view = getLayoutInflater().inflate(R.layout.answer_perform_test_template,null);
            TextView answerText = view.findViewById(R.id.answerContent);
            answerText.setText(answer);
            int childCount = ansContainer.getChildCount();
            String character = ((char)('A' + childCount)) + ". ";

            TextView letter = view.findViewById(R.id.letterPreview);
            letter.setText(character);

            ansContainer.addView(view, childCount);
        }
    }
}
