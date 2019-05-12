package com.apki.e_tests;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;


public class Test implements Serializable {

    private String title, subject , section;
    private ArrayList<Question> questions;
    // TODO add time restriction

    public Test(){
        title = "";
        subject = "";
        section = "";
        questions = new ArrayList<>();
    }

    public Test(QueryDocumentSnapshot document){
        retrieveTestObject(document);
    }

    private void retrieveTestObject(QueryDocumentSnapshot document){

        this.setTitle(document.get("title").toString());
        this.setSubject(document.get("subject").toString());
        this.setSection(document.get("section").toString());
        this.setQuestions(retrieveQuestions(document));
    }

    private ArrayList<Question> retrieveQuestions(QueryDocumentSnapshot document){
        ArrayList<Question> q = new ArrayList<>();
        for (Map<String, Object> map: (ArrayList<Map<String,Object>>) document.get("questions")) {
            q.add(new Question(map));
        }
        return q;
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

    public void saveDataToFirestore(FirebaseFirestore db){

        db.collection("TEST")
                .add(this)
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

    public void setQuestions(ArrayList<Question>questions) {
        this.questions = questions;
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

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}
