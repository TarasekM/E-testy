package com.apki.e_tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateTestActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    LinearLayout ansContainer;
    ArrayList<View> records = new ArrayList<>();
    Map<String,Map<String, Object>> questions = new HashMap<>();
    int questionCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);
        ansContainer = findViewById(R.id.answerContainer);
        configureAddAnswerButton();
        configureNextQuestionButton();
        configureSaveDataButton();
    }

    private void configureSaveDataButton(){
        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
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
        questionCount++;
        Map<String, Object> question = new HashMap<>();
        EditText sentence = findViewById(R.id.sentence);
        question.put("sentence", sentence.getText().toString());
        ArrayList<String> answers = new ArrayList<>();
        ArrayList<String> checks = new ArrayList<>();

        for (View view: records) {
            EditText answer = view.findViewById(R.id.answer);
            CheckBox checkBox = view.findViewById(R.id.checkBox);
            answers.add(answer.getText().toString());
            checks.add(checkBox.isChecked() + "");
        }

        question.put("answers", answers);
        question.put("isTrue", checks);
        questions.put("Question" + questionCount, question);

        //Clear UI
        records.clear();
        ansContainer.removeAllViews();
        sentence.setText("");
    }

    private void saveData(){


        db.collection("TEST")
                .add(questions)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("DOCUMENT", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DOCUMENT", "Error adding document", e);
                    }
                });
        startActivity(new Intent(CreateTestActivity.this, MainActivity.class));
        finish();

    }
}
