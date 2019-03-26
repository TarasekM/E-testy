package com.apki.e_tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CreateTestActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    LinearLayout ansContainer;
    ArrayList<View> records = new ArrayList<>();
    Test test = new Test();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test_settings);
        configureNextButton();
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
                setContentView(R.layout.activity_create_test);
                ansContainer = findViewById(R.id.answerContainer);
                // Configure buttons
                configureAddAnswerButton();
                configureNextQuestionButton();
                configureSaveDataButton();
            }
        });
    }

    private void configureSaveDataButton(){
        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.saveDataToFirestore(db);
                startActivity(new Intent(CreateTestActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void configureNextQuestionButton(){
        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
    }

    private void configureAddAnswerButton(){
        ImageButton addButton = findViewById(R.id.addInput);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = ansContainer.getChildCount();
                if (childCount < 7){
                    View newAnswer = getLayoutInflater().inflate(R.layout.answer_template,null);
                    fillRecord(newAnswer, childCount);
                    records.add(newAnswer);
                    ansContainer.addView(newAnswer, childCount);
                }else{
                    Toast.makeText(CreateTestActivity.this,
                            "Can't create more answers",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void fillRecord(View view, int childCount){
        TextView letter = view.findViewById(R.id.letter);
        EditText newEditText = view.findViewById(R.id.answer);
        char character = (char)('A' + childCount);
        String text = String.format("%c.", character);
        letter.setText(text);
        newEditText.setHint(String.format("Odpowiedź %c", character));
    }

    private void nextQuestion(){
        Question question = new Question();
        EditText sentence = findViewById(R.id.sentence);
        question.setSentence(sentence.getText().toString());

        for (View view: records) {
            EditText answer = view.findViewById(R.id.answer);
            CheckBox checkBox = view.findViewById(R.id.checkBox);
            question.addAnswer(answer.getText().toString(), checkBox.isChecked());
        }

        test.addQuestion(question);
        //Clear UI
        records.clear();
        ansContainer.removeAllViews();
        sentence.setText("");
    }
}
