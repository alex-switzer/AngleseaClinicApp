package com.angleseahospital.admin.firestore;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Empty Nurse constructor
     */
    public Nurse() { /* Empty constructor for Firestore */ }

    /**
     * Constructs Nurse with details from docNurse
     * @param docNurse Document to read details for Nurse from
     */
    public Nurse(DocumentSnapshot docNurse) {
        id = docNurse.getId();
        Log.d("NURSE CONSTRUCTOR", "Nurse ID: " + id);
        firstName = (String) docNurse.get(FIELD_FIRSTNAME);
        lastName = (String) docNurse.get(FIELD_LASTNAME);
        pin = (String) docNurse.get(FIELD_PIN);
        present = (boolean) docNurse.get(FIELD_PRESENT);
        lastSign = (String) docNurse.get(FIELD_LASTSIGN);
        roster = new NurseRoster((String) docNurse.get(FIELD_ROSTER));
        Log.d("NURSE CONSTRUCTOR", id + " added successfully");
    }

    /**
     * Gets the fullname of the nurse
     * @return fullname of the nurse
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Generates a ID for this nurse
     * @return Generated ID
     */
    public String generateID() {
        return id = db.collection(Constants.COLLECTION_NURSES).document().getId();
    }

    /**
     * Updates the database with nurses details and roster
     * @param editing If details should be updated or overriden in the database
     * @return Task of the database updates
     */
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
    /**
     * Parcel CREATOR creates a Nurse object from another parcel
     */
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

    /**
     * Describes content
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Constructs this nurse with given Parcel object
     * @param in Parcel to construct nurse with
     */
    protected Nurse(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        pin = in.readString();
        present = in.readByte() != 0;
        roster = new NurseRoster(in);
    }

    /**
     * Writes to Parcel object with details of this Nurse object
     * @param dest Parcel to parse details to
     * @param flags Flags for Parcel
     */
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
