package com.apki.e_tests;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

//TODO Questions should keep assigned index after edit

public class CreateTestActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    LinearLayout ansContainer;
    ArrayList<View> records = new ArrayList<>();
    ArrayList<Question> questions = new ArrayList<>();
    Test test = new Test();

    String toEdit = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentAsCreateTestSettings();

        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onBackPressed(){
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        String tag = viewGroup.getTag().toString();
        System.out.println(tag);

        if(tag.equals("create_question")){
            setContentAsCreateTest();
        }else if (tag.equals("create_test")){
            setContentAsCreateTestSettings();
        }else if(tag.equals("create_test_settings")){
            finish();
        }

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
                setContentAsCreateTest();
            }
        });
    }

    private void setContentAsCreateTestSettings(){
        setContentView(R.layout.activity_create_test_settings);

        EditText edExamTitle = findViewById(R.id.inputExamTitle);
        EditText edSubject = findViewById(R.id.inputSubject);
        EditText edSection = findViewById(R.id.inputSection);

        edExamTitle.setText(test.getTitle());
        edSubject.setText(test.getSection());
        edSection.setText(test.getSection());

        configureNextButton();
    }

    private void setContentAsCreateTest(){
        setContentView(R.layout.content_create_test);
        fillPreviews();
        configureButtonsForQuestionLayout();
    }

    private void setContentAsCreateQuestion(){
        setContentView(R.layout.activity_create_question);

        ansContainer = findViewById(R.id.answerContainer);
        TextView questionLabel = findViewById(R.id.questionLabel);
        questionLabel.setText("Pytanie " + (questions.size() + 1));

        configureButtonsForAnswerLayout();
    }

    private void configureButtonsForQuestionLayout(){
        configureAddQuestionButton();
        configureSaveDataButton();
    }

    private void configureButtonsForAnswerLayout(){
        configureAddAnswerButton();
        configureRemoveAnswerButton();
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

    private void configureAddQuestionButton(){
        Button createQuestion = findViewById(R.id.addQuestionButton);
        createQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentAsCreateQuestion();
            }
        });
    }

    private void configureNextQuestionButton(){
        Button next = findViewById(R.id.saveQuestion);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
                setContentAsCreateTest();
            }
        });
    }

    private void fillPreviews(){
        // refresh layout first
        setContentView(R.layout.content_create_test);

        LinearLayout questionPreviewContainer = findViewById(R.id.questionPreviewContainer);
        for(Question question : questions){
            View view = getLayoutInflater().inflate(R.layout.question_preview_template, null);
            fillQuestionPreview(view, question);
            questionPreviewContainer.addView(view, questionPreviewContainer.getChildCount());
        }
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

    private void configureRemoveAnswerButton(){
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

    private void fillQuestionPreview(View view, final Question question){
        TextView questionContent = view.findViewById(R.id.questionContent);
        TextView questionLabel = view.findViewById(R.id.testTitleText);

        questionContent.setText(question.getSentence());
        questionLabel.setText(question.getLabel());

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
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateTestActivity.this);

                builder.setCancelable(true);
                builder.setTitle("Czy na pewno chcesz usunąć pytanie?");

                builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        questions.remove(question);
                        fillPreviews();
                        configureButtonsForQuestionLayout();
                    }
                });
                builder.show();
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
                questionLabel.setText(question.getLabel());
                toEdit = question.getLabel();
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

        int index = 0;
        for (Question q : questions){
            if(q.getLabel().equals(toEdit)){
                question.setLabel(q.getLabel());
                break;
            }
            index++;
        }

        if (question.getLabel().equals(toEdit)){
            questions.set(index, question);
        }else{
            question.setLabel("Pytanie " + (questions.size() + 1));
            questions.add(question);
        }

        //Clear UI
        toEdit = "null";
        records.clear();
        ansContainer.removeAllViews();
        edSentence.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            onBackPressed();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
