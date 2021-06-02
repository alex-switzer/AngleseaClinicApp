package com.angleseahospital.admin.firestore;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

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
    public String lastSign;
    public Roster roster;

    public Nurse() { /* Empty constructor for Firestore */}

    public Nurse(FirebaseFirestore db, QueryDocumentSnapshot baseNurse) {
        id = baseNurse.getId();
        firstName = (String) baseNurse.get("firstname");
        lastName = (String) baseNurse.get("lastname");
        pin = (String) baseNurse.get("pin");
        present = (boolean) baseNurse.get("present");
        lastSign = (String) baseNurse.get("lastSign");
        roster = new Roster(((DocumentReference) baseNurse.get("roster")).getPath());
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