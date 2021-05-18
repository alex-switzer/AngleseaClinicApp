package com.example.angleseaapp.firebase;

public class Nurse {
    private String id;
    private String name_first;
    private String name_last;
    private String pin;

    public Nurse() {  }

    public Nurse(String name_first, String name_last, String pin) {
        this.name_first = name_first;
        this.name_last = name_last;
        this.pin = pin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_first() {
        return name_first;
    }

    public void setName_first(String name_first) {
        this.name_first = name_first;
    }

    public String getName_last() {
        return name_last;
    }

    public void setName_last(String name_last) {
        this.name_last = name_last;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }


}
