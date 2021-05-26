package com.angleseahospital.nurse.firebase;

import android.os.Parcel;
import android.os.Parcelable;

public class Nurse implements Parcelable {
    private String id;
    private String name_first;
    private String name_last;
    private String pin;
    private boolean present;

    public Nurse() {  }

    public Nurse(String name_first, String name_last, String pin){
        this.name_first = name_first;
        this.name_last = name_last;
        this.pin = pin;
    }

    protected Nurse(Parcel in) {
        id = in.readString();
        name_first = in.readString();
        name_last = in.readString();
        pin = in.readString();
        present = in.readByte() != 0;
    }

    public static final Creator<Nurse> CREATOR = new Creator<Nurse>() {
        @Override
        public Nurse createFromParcel(Parcel in) {
            return new Nurse(in);
        }

        @Override
        public Nurse[] newArray(int size) {
            return new Nurse[size];
        }
    };

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

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name_first);
        dest.writeString(name_last);
        dest.writeString(pin);
        dest.writeByte((byte) (present ? 1 : 0));

    }
}
