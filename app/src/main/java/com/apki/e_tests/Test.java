package com.apki.e_tests;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Test {

    private String title, subject, section;
    private Map<String, Object> questions = new HashMap<>();
    // TODO add time restriction

    public void addQuestion(Question question){
        String questionLabel = "Question " + (questions.size() + 1);
        questions.put(questionLabel, question.getAsMap());
    }

    public void saveDataToFirestore(FirebaseFirestore db){

        Map<String, Object> details = new HashMap<>();

        details.put("examTitle", title);
        details.put("subject", subject);
        details.put("section", section);
        questions.put("Details", details);


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
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getSubject() {
        return subject;
    }
}
