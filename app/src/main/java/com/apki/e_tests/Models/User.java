package com.apki.e_tests.Models;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class User {

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
}
