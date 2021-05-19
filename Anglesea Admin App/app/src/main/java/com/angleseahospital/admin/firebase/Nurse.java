package com.angleseahospital.admin.firebase;

import com.google.firebase.database.Exclude;

public class Nurse {
    private String id;
    private String name_first;
    private String name_last;
    private String pin;

    private String shift_mon;
    private String shift_tue;
    private String shift_wed;
    private String shift_thu;
    private String shift_fri;
    private String shift_sat;
    private String shift_sun;

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

    @Exclude
    public String getFullname() {
        return name_first + " " + name_last;
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
        //TODO: Get Shifts from database
        return new Shift[]{
                //lookup 1,
        };
    }

    @Exclude
    public void setShifts(Shift[] shifts){
        shift_mon = shifts[0].getId();
        shift_tue = shifts[1].getId();
        shift_wed = shifts[2].getId();
        shift_thu = shifts[3].getId();
        shift_fri = shifts[4].getId();
        shift_sat = shifts[5].getId();
        shift_sun = shifts[6].getId();
    }


    public String getShift_mon() {
        return shift_mon;
    }

    public void setShift_mon(String shift_mon) {
        this.shift_mon = shift_mon;
    }

    public String getShift_tue() {
        return shift_tue;
    }

    public void setShift_tue(String shift_tue) {
        this.shift_tue = shift_tue;
    }

    public String getShift_wed() {
        return shift_wed;
    }

    public void setShift_wed(String shift_wed) {
        this.shift_wed = shift_wed;
    }

    public String getShift_thu() {
        return shift_thu;
    }

    public void setShift_thu(String shift_thu) {
        this.shift_thu = shift_thu;
    }

    public String getShift_fri() {
        return shift_fri;
    }

    public void setShift_fri(String shift_fri) {
        this.shift_fri = shift_fri;
    }

    public String getShift_sat() {
        return shift_sat;
    }

    public void setShift_sat(String shift_sat) {
        this.shift_sat = shift_sat;
    }

    public String getShift_sun() {
        return shift_sun;
    }

    public void setShift_sun(String shift_sun) {
        this.shift_sun = shift_sun;
    }
}
