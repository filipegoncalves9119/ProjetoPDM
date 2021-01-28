package com.example.medicacaocrianca.model;

public class Teacher {

    private String name;
    private String email;
    private String password;

    public Teacher(String name, String email) {
        this.name = name;
        this.email = email;

    }

    public Teacher() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
