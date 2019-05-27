package com.apki.e_tests.Models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class LectureClass implements Serializable {
    private ArrayList<User> students = new ArrayList<>();
    private ArrayList<User> teachers = new ArrayList<>();
    private ArrayList<Test> tests = new ArrayList<>();
    private User owner = new User();
    private String className = "";
    private String classCode = "";

    public LectureClass(){
    }

    //TODO create constructor for Test with arg: Map
    public LectureClass(QueryDocumentSnapshot data){
        getStudentsFromMap((ArrayList<Map<String,Object>>) data.get("students"));
        getTeachersFromMap((ArrayList<Map<String,Object>>) data.get("teachers"));
        tests = (ArrayList<Test>) data.get("test");
        owner = new User((Map<String,Object>) data.get("owner"));
        className =(String) data.get("className");
        classCode = (String) data.get("classCode");
    }

    public LectureClass(DocumentSnapshot data){
        getStudentsFromMap((ArrayList<Map<String,Object>>) data.get("students"));
        getTeachersFromMap((ArrayList<Map<String,Object>>) data.get("teachers"));
        tests = (ArrayList<Test>) data.get("test");
        owner = new User((Map<String,Object>) data.get("owner"));
        className =(String) data.get("className");
        classCode = (String) data.get("classCode");
    }

    private void getStudentsFromMap(ArrayList<Map<String,Object>> data){
        for (Map<String, Object> map: data) {
            students.add(new User(map));
        }
    }


    private void getTeachersFromMap(ArrayList<Map<String,Object>> data){
        for (Map<String, Object> map: data) {
            teachers.add(new User(map));
        }
    }


    private void generateClassCode(int size){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++){
            stringBuilder.append((char)(Math.random() * 74 + 48));
        }
        classCode = stringBuilder.toString();
    }

    public void saveClassToDB(FirebaseFirestore db){
        generateClassCode(6);
        db.collection("CLASS").document(getClassCode()).set(this);
    }

    public boolean contains(User user){
        for (User student : students) {
            if(student.equals(user)){
                return true;
            }
        }

        for (User teacher : teachers){
            if (teacher.equals(user)){
                return true;
            }
        }

        if (owner.equals(user)){
            return true;
        }
        return false;
    }

    public void addStudent(User user){
        students.add(user);
    }

    public void addTeacher(User user){
        teachers.add(user);
    }

    public void addTest(Test test){
        tests.add(test);
    }

    /*
    * Setters
    * */

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setStudents(ArrayList<User> students) {
        this.students = students;
    }

    public void setTeachers(ArrayList<User> teachers) {
        this.teachers = teachers;
    }

    public void setTests(ArrayList<Test> tests) {
        this.tests = tests;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /*
    * Getters
    * */
    public ArrayList<Test> getTests() {
        return tests;
    }

    public ArrayList<User> getStudents() {
        return students;
    }

    public ArrayList<User> getTeachers() {
        return teachers;
    }

    public User getOwner() {
        return owner;
    }

    public String getClassName() {
        return className;
    }

    public String getClassCode() {
        return classCode;
    }
}
