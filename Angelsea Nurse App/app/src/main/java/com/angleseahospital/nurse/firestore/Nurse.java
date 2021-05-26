package com.angleseahospital.nurse.firestore;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Nurse implements Parcelable {

    public String id;
    public String firstName;
    public String lastName;
    public String pin;
    public boolean present;
    public String roster;

    public Nurse() { /* Empty constructor for Firestore */}

    public Nurse(QueryDocumentSnapshot baseNurse) {
        id = baseNurse.getId();
        firstName = (String) baseNurse.get("firstname");
        lastName = (String) baseNurse.get("lastname");
        pin = (String) baseNurse.get("pin");
        present = (boolean) baseNurse.get("present");
        roster = ((DocumentReference) baseNurse.get("roster")).getPath();
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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    //--Parcelable stuff--
    @Override
    public int describeContents() {
        return 0;
    }
    protected Nurse(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        pin = in.readString();
        present = in.readByte() != 0;
        roster = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(pin);
        dest.writeString(roster);
    }
}
