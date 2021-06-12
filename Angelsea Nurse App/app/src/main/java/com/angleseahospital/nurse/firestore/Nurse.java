package com.angleseahospital.nurse.firestore;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.HashMap;
import java.util.Map;

public class Nurse implements Parcelable {

    public static final String FIELD_FIRSTNAME = "firstname";
    public static final String FIELD_LASTNAME = "lastname";
    public static final String FIELD_LASTSIGN = "lastSign";
    public static final String FIELD_PIN = "pin";
    public static final String FIELD_PRESENT = "present";
    public static final String FIELD_ROSTER = "roster";

    public String id;
    public String firstName;
    public String lastName;
    public String pin;
    public boolean present;
    public String lastSign;
    public NurseRoster roster = new NurseRoster();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Nurse() { /* Empty constructor for Firestore */ }

    public Nurse(QueryDocumentSnapshot baseNurse) {
        id = baseNurse.getId();
        firstName = (String) baseNurse.get(FIELD_FIRSTNAME);
        lastName = (String) baseNurse.get(FIELD_LASTNAME);
        pin = (String) baseNurse.get(FIELD_PIN);
        present = (boolean) baseNurse.get(FIELD_PRESENT);
        lastSign = (String) baseNurse.get(FIELD_LASTSIGN);
        roster = new NurseRoster((String) baseNurse.get(FIELD_ROSTER));
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

    public String generateID() {
        return id = db.collection(Constants.COLLECTION_NURSES).document().getId();
    }

    public Task<Object> updateDatabase(boolean editing) {
        Map<String, Object> data = new HashMap<>();
        data.put(FIELD_FIRSTNAME, firstName);
        data.put(FIELD_LASTNAME, lastName);
        data.put(FIELD_PIN, pin);
        data.put(FIELD_PRESENT, present);
        data.put(FIELD_LASTSIGN, lastSign);

        if (editing) {
            return db.collection(Constants.COLLECTION_NURSES).document(id).update(data)
                    .continueWith(task -> roster.update(id));
        } else {
            return db.collection(Constants.COLLECTION_NURSES).document(id).set(data)
                    .continueWith(task -> roster.update(id, NurseRoster.getThisWeeksRosterPath(id)));
        }
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
        roster = new NurseRoster(in);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(pin);
        dest.writeInt(present ? 1 : 0);
        roster.writeToParcel(dest, flags);
    }
}