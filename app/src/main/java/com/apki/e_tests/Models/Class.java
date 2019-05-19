package com.apki.e_tests.Models;

import java.util.ArrayList;

public class Class {
    private ArrayList<User> students;
    private ArrayList<User> teachers;
    private ArrayList<Test> tests;
    private User owner;
    private String className;

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
}
