package com.example.meetapp;

public class Student {

    private String name;
    private String klas;
    private String Id;

    public Student(){}

    public Student(String name, String klas, String Id) {
        this.name = name;
        this.klas = klas;
        this.Id = Id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public String getKlas() {
        return klas;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKlas(String klas) {
        this.klas = klas;
    }
}
