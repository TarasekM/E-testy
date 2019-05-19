package com.apki.e_tests.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Question implements Serializable {


    private ArrayList<String> answers;
    private ArrayList<Boolean> checks;

    private String sentence;

    private String label = "Pytanie";

    public Question(){
        answers = new ArrayList<>();
        checks = new ArrayList<>();
        sentence = "null";
    }

    public Question(Map<String, Object> data){
        answers = (ArrayList<String>) data.get("answers");
        checks = (ArrayList<Boolean>) data.get("checks");
        sentence = (String) data.get("sentence");
        label = (String) data.get("label");
    }

    public void addAnswer(String answer, Boolean isTrue){
        answers.add(answer);
        checks.add(isTrue);
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentence() {
        return sentence;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public ArrayList<Boolean> getChecks() {
        return checks;
    }

}
