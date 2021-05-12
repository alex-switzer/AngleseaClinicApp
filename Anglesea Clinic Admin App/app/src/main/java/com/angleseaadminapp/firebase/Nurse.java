package com.angleseaadminapp.firebase;

import com.google.firebase.database.Exclude;

public class Nurse {
    private String id;
    private String name_first;
    private String name_last;
    private String pin;

    private String shift0;
    private String shift1;
    private String shift2;
    private String shift3;
    private String shift4;
    private String shift5;
    private String shift6;

    public Nurse(){ }

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

    @Exclude
    public Shift[] getShifts(){
        //TODO: DB lookup
        return new Shift[]{
                //lookup 1,
        };
    }

    @Exclude
    public void setShifts(Shift[] shifts){
        shift0 = shifts[0].getId();
        shift1 = shifts[1].getId();
        shift2 = shifts[2].getId();
        shift3 = shifts[3].getId();
        shift4 = shifts[4].getId();
        shift5 = shifts[5].getId();
        shift6 = shifts[6].getId();
    }


    public String getShift0() {
        return shift0;
    }

    public void setShift0(String shift0) {
        this.shift0 = shift0;
    }

    public String getShift1() {
        return shift1;
    }

    public void setShift1(String shift1) {
        this.shift1 = shift1;
    }

    public String getShift2() {
        return shift2;
    }

    public void setShift2(String shift2) {
        this.shift2 = shift2;
    }

    public String getShift3() {
        return shift3;
    }

    public void setShift3(String shift3) {
        this.shift3 = shift3;
    }

    public String getShift4() {
        return shift4;
    }

    public void setShift4(String shift4) {
        this.shift4 = shift4;
    }

    public String getShift5() {
        return shift5;
    }

    public void setShift5(String shift5) {
        this.shift5 = shift5;
    }

    public String getShift6() {
        return shift6;
    }

    public void setShift6(String shift6) {
        this.shift6 = shift6;
    }
}
