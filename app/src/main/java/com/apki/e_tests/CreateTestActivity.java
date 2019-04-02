package com.apki.e_tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CreateTestActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    LinearLayout ansContainer;
    ArrayList<View> records = new ArrayList<>();
    ArrayList<Question> questions = new ArrayList<>();
    Test test = new Test();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test_settings);

        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureNextButton();
    }

    private void configureButtonsForQuestionLayout(){
        configureAddQuestionButton();
        configureSaveDataButton();
    }

    private void configureButtonsForAnswerLayout(){
        configureAddAnswerButton();
        configureRemoveQuestionButton();
        configureNextQuestionButton();
    }

    private void configureSaveDataButton(){
        Button save = findViewById(R.id.saveTestButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Question question : questions){
                    test.addQuestion(question);
                }
                test.saveDataToFirestore(db);
                finish();
            }
        });
    }

    private void configureNextButton(){
        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Find all edit texts
                EditText edExamTitle = findViewById(R.id.inputExamTitle);
                EditText edSubject = findViewById(R.id.inputSubject);
                EditText edSection = findViewById(R.id.inputSection);
                // Add records to test
                test.setTitle(edExamTitle.getText().toString());
                test.setSubject(edSubject.getText().toString());
                test.setSection(edSection.getText().toString());
                // Change view
                setContentView(R.layout.content_create_test);
                configureButtonsForQuestionLayout();
            }
        });
    }

    private void configureAddQuestionButton(){
        Button createQuestion = findViewById(R.id.addQuestionButton);
        createQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_create_question);
                ansContainer = findViewById(R.id.answerContainer);
                // Configure buttons
                TextView questionLabel = findViewById(R.id.questionLabel);
                questionLabel.setText("Pytanie " + (questions.size() + 1));
                configureButtonsForAnswerLayout();
            }
        });
    }

    private void configureNextQuestionButton(){
        Button next = findViewById(R.id.saveQuestion);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
                setContentView(R.layout.content_create_test);
                fillPreviews();
            }
        });
    }

    private void fillPreviews(){
        // refresh layout first
        setContentView(R.layout.content_create_test);

        LinearLayout questionPreviewContainer = findViewById(R.id.questionPreviewContainer);
        for(Question question : questions){
            View view = getLayoutInflater().inflate(R.layout.question_preview_template, null);
            fillQuestionPreview(view, question, questionPreviewContainer.getChildCount() + 1);
            questionPreviewContainer.addView(view, questionPreviewContainer.getChildCount());
        }

        configureButtonsForQuestionLayout();
    }

    private void configureAddAnswerButton(){
        ImageButton addButton = findViewById(R.id.addInput);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = ansContainer.getChildCount();
                    View newAnswer = getLayoutInflater().inflate(R.layout.answer_template, null);
                    fillRecord(newAnswer, childCount);
                    records.add(newAnswer);
                    ansContainer.addView(newAnswer, childCount);
                if(childCount >= 6){
                    ImageView button = findViewById(R.id.addInput);
                    button.setVisibility(View.GONE);
                }
            }
        });

    }

    private void configureRemoveQuestionButton(){
        ImageButton removeButton = findViewById(R.id.removeInput);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = ansContainer.getChildCount();
                if(childCount > 0){
                    ansContainer.removeViewAt(childCount - 1);
                    records.remove(records.size() - 1);
                }
            }
        });
    }

    private void fillRecord(View view, int childCount){
        TextView letter = view.findViewById(R.id.letterPreview);
        EditText newEditText = view.findViewById(R.id.answer);
        char character = (char)('A' + childCount);
        String text = String.format("%c.", character);
        letter.setText(text);
        newEditText.setHint(String.format("Odpowiedź %c", character));
    }

    private void fillQuestionPreview(View view, final Question question, int counter){
        TextView questionContent = view.findViewById(R.id.questionContent);
        TextView questionLabel = view.findViewById(R.id.titleText);

        questionContent.setText(question.getSentence());
        questionLabel.setText("Pytanie " + counter);

        ArrayList<String> answers = question.getAnswers();
        ArrayList<Boolean> checks = question.getChecks();

        LinearLayout leftColumn = view.findViewById(R.id.leftColumn);
        LinearLayout rightColumn = view.findViewById(R.id.rightColumn);

        ImageButton remove = view.findViewById(R.id.removeQuestion);
        ImageButton edit = view.findViewById(R.id.editQuestion);

        for (int i = 0; i < question.getAnswers().size(); i++){
            View newAnswerPreview = getLayoutInflater().inflate(R.layout.answer_preview_template,null);
            ImageView isTrue = newAnswerPreview.findViewById(R.id.isTrue);
            TextView letter = newAnswerPreview.findViewById(R.id.letterPreview);
            TextView answer = newAnswerPreview.findViewById(R.id.answerPreview);

            answer.setText(answers.get(i));

            letter.setText(String.format("%c.", 'A' + i));

            if(checks.get(i)){
                isTrue.setImageResource(R.drawable.green_circle);
            }else {
                isTrue.setImageResource(R.drawable.red_circle);
            }

            if( i == 0){
                leftColumn.addView(newAnswerPreview, leftColumn.getChildCount());
            }else if( i % 2 == 1){
                rightColumn.addView(newAnswerPreview, rightColumn.getChildCount());
            }else {
                leftColumn.addView(newAnswerPreview, leftColumn.getChildCount());
            }
        }

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questions.remove(question);
                fillPreviews();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_create_question);
                ansContainer = findViewById(R.id.answerContainer);
                ArrayList<String> answers = question.getAnswers();
                ArrayList<Boolean> checks = question.getChecks();
                String sentence = question.getSentence();

                for(int i = 0; i < answers.size() ; i++){
                    View newAnswer = getLayoutInflater().inflate(R.layout.answer_template, null);
                    fillRecord(newAnswer, ansContainer.getChildCount());
                    CheckBox check = newAnswer.findViewById(R.id.checkBox);
                    EditText answer = newAnswer.findViewById(R.id.answer);
                    check.setChecked(checks.get(i));
                    answer.setText(answers.get(i));
                    records.add(newAnswer);
                    ansContainer.addView(newAnswer, i);
                }
                EditText edSentence = findViewById(R.id.sentence);
                edSentence.setText(sentence);
                TextView questionLabel = findViewById(R.id.questionLabel);
                questionLabel.setText("Pytanie " + (questions.size()));
                questions.remove(question);

                configureButtonsForAnswerLayout();
            }
        });
    }

    private void nextQuestion(){
        Question question = new Question();
        EditText edSentence = findViewById(R.id.sentence);
        String sentence = edSentence.getText().toString();

        question.setSentence(sentence);
        for (View view: records) {
            EditText answer = view.findViewById(R.id.answer);
            CheckBox checkBox = view.findViewById(R.id.checkBox);
            question.addAnswer(answer.getText().toString(), checkBox.isChecked());
        }

        questions.add(question);

        //Clear UI

        records.clear();
        ansContainer.removeAllViews();
        edSentence.setText("");
    }
}
