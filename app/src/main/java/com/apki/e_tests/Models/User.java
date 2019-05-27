package com.apki.e_tests.Models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.Map;

public class User implements Serializable {

    private String email;
    private String name;
    private String surname;

    public User(){

    }

    public User(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    public User(QueryDocumentSnapshot data){
        email = (String) data.get("email");
        name = (String) data.get("name");
        surname = (String) data.get("surname");
    }

    public User(DocumentSnapshot data){
        email = (String) data.get("email");
        name = (String) data.get("name");
        surname = (String) data.get("surname");
    }

    public User(Map<String, Object> map){
        email = (String) map.get("email");
        name = (String) map.get("name");
        surname = (String) map.get("surname");
    }

    /*
    * Setters
    * */

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    /*
    * Getters
    * */

    public String getFullName(){
        return String.format("%s %s", name, surname);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    /*
    * Overrides
    * */

    public boolean equals(User user){
        return this.email.equals(user.getEmail())
                && this.name.equals(user.getName())
                && this.surname.equals(user.getSurname());
    }
}
