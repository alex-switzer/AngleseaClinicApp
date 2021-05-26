package com.angleseahospital.nurse.firestore;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Nurse implements Parcelable {

    public String id;
    public String firstName;
    public String lastName;
    public String pin;
    public boolean present;
    public String rosterRef;
    public Roster roster;

    public Nurse() { /* Empty constructor for Firestore */}

    public Nurse(FirebaseFirestore db, QueryDocumentSnapshot baseNurse) {
        Log.d("NURSE DEBUGGING", "Nurse being created");
        id = baseNurse.getId();
        firstName = (String) baseNurse.get("firstname");
        lastName = (String) baseNurse.get("lastname");
        pin = (String) baseNurse.get("pin");
        present = (boolean) baseNurse.get("present");
        Log.d("NURSE DEBUGGING", "Roster being created");
        roster = new Roster(((DocumentReference) baseNurse.get("roster")).getPath());
        Log.d("NURSE DEBUGGING", "Roster CREATED! FINSIHED WITH NURSE");

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
        roster = new Roster(in);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(pin);
        roster.writeToParcel(dest, flags);
    }
}
