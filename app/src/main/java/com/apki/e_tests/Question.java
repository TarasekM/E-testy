package com.apki.e_tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Question {

    // private Map<String, Object> question;

    private ArrayList<String> answers;
    private ArrayList<Boolean> checks;

    private String sentence;

    Question(){
        // question = new HashMap<>();
        answers = new ArrayList<>();
        checks = new ArrayList<>();
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
}