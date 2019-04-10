package com.apki.e_tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Question {

    // private Map<String, Object> question;

    private ArrayList<String> answers;
    private ArrayList<Boolean> checks;

    private String sentence;

    private String label;

    Question(){
        // question = new HashMap<>();
        answers = new ArrayList<>();
        checks = new ArrayList<>();
        sentence = "null";
        label = "Pytanie";
    }

    public void addAnswer(String answer, Boolean isTrue){
        answers.add(answer);
        checks.add(isTrue);
    }

    public Map<String, Object> getAsMap(){
        Map<String, Object> question = new HashMap<>();

//        if(sentence.isEmpty() || answers.isEmpty() || checks.isEmpty()){
//            throw new FieldsEmptyException("Sentence: " + sentence.isEmpty() + '\n'
//                + "Answers: " + answers.isEmpty() + '\n'
//                + "Checks: " + checks.isEmpty());
//        }

        question.put("sentence", sentence);
        question.put("answers", answers);
        question.put("isTrue", checks);
        return question;
    }

    private class FieldsEmptyException extends Exception {
        FieldsEmptyException(String message) {
            super(message);
        }
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
