package com.angleseahospital.nurse.firestore;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.angleseahospital.nurse.firestore.Shift.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NurseRoster implements Parcelable {

    public static String getThisWeeksRosterPath(String nursesID) {
        String output = Constants.COLLECTION_ROSTERS;
        return output + "/" + getThisWeeksRosterDate() + "/nurses/" + nursesID;
    }
    public static String getThisWeeksRosterPath() {
        String output = Constants.COLLECTION_ROSTERS;
        return output + "/" + getThisWeeksRosterDate();
    }

    public static String getThisWeeksRosterDate() {
        return getWeeksDate(Calendar.getInstance());
    }
    public static String getWeeksDate(Calendar dayOfWeek) {

        Calendar monday = getWeeksMonday(dayOfWeek);

        int month = monday.get(Calendar.MONTH) + 1;
        int day = monday.get(Calendar.DAY_OF_MONTH);

        return monday.get(Calendar.YEAR) + "/" + (month < 10 ? "0" + month : month) + "/" + (day < 10 ? "0" + day : day);
    }

    public static Calendar getThisWeeksMonday() {
        return getWeeksMonday(Calendar.getInstance());
    }
    public static Calendar getWeeksMonday(Calendar dayOfWeek) {
        Calendar monday = Calendar.getInstance();
        monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return monday;
    }

    public static Calendar getPrevWeeksMonday() { return getPrevWeeksMonday(getThisWeeksMonday()); }
    public static Calendar getPrevWeeksMonday(Calendar currentMonday) {
        currentMonday.add(Calendar.DATE, -7);  // for previous Monday
        return currentMonday;
    }

    public static Calendar getNextWeeksMonday() { return getNextWeeksMonday(getThisWeeksMonday()); }
    public static Calendar getNextWeeksMonday(Calendar currentMonday) {
        currentMonday.add(Calendar.DATE, 7);  // for next Monday
        return currentMonday;
    }



    public String reference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private HashMap<ShiftDay, ShiftType> days;

    public NurseRoster() {  }

    public NurseRoster(String reference) {
        this.reference = reference;
    }

    public void build(OnCompleteListener listener) throws IllegalArgumentException {
        if (reference == null || reference.equals(""))
            throw new IllegalArgumentException("No doc reference provided");
        build(listener, reference);
    }
    public void build(OnCompleteListener listener, @NonNull String docRef) {
        this.reference = docRef;
        db.document(docRef).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful())
                return;
            DocumentSnapshot documentSnapshot = task.getResult();
            if (documentSnapshot == null)
                return;

            String value;
            days = new HashMap<>();
            for (ShiftDay shift : ShiftDay.values()) {
                value = (String) documentSnapshot.get(shift.name().toLowerCase());
                if (value == null)
                    continue;
                days.put(shift, ShiftType.fromString(value));
            }
        }).addOnCompleteListener(listener);
    }
    public boolean isBuilt() {
        return days != null;
    }

    public Task<Void> update(String id) {
        return update(id, reference);
    }
    public Task<Void> update(String id, String reference) {
        if (!isBuilt())
            throw new IllegalStateException("Roster has to be built first");
        this.reference = reference;

        WriteBatch batch = db.batch();
        DocumentReference nurseDocRef = db.collection(Constants.COLLECTION_NURSES).document(id);
        DocumentReference rosterDocRef = db.document(reference);

        nurseDocRef.update(Nurse.FIELD_ROSTER, reference);

        Map<String, Object> rosterData = new HashMap<>();
        for (ShiftDay day : days.keySet()) {
            rosterData.put(day.name().toLowerCase(), days.get(day).name());
            Log.d("Updating database with data: ", day.name() + ": \"" + days.get(day).name() + "\"");
        }

        rosterDocRef.set(rosterData);

        return batch.commit().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("NurseRoster.Update", " Failed to update: " + task.getException());
                //TODO: Display failure to update roster
            }
        });
    }


    public Shift getNextUncompletedShift() {
        if (!isBuilt())
            return null;
        for (ShiftDay day : ShiftDay.values()) {
            ShiftType type = days.get(day);

            if (type == null)
                continue;

            if (!type.isCompleted())
                return new Shift(day, days.get(day));
        }
        return null;
    }

    public Shift getShift(ShiftDay day) {
        if (!isBuilt())
            return null;
        ShiftType type = days.get(day);
        if (type == null)
            return null;
        return new Shift(day, type);
    }

    public HashMap<ShiftDay, ShiftType> getShifts() {
        return days;
    }

    public int getTotalShifts() {
        if (!isBuilt())
            return 0;
        return days.size();
    }

    public boolean earlySignout() {
        if (!isBuilt())
            return false;
        Shift unShift = getNextUncompletedShift();
        if (unShift == null)
            return false;
        //TODO: Add early signout
        return false;
    }

    public Shift completeShift() {
        if (!isBuilt())
            return null;
        Shift unShift = getNextUncompletedShift();
        if (unShift == null)
            return null;

        days.get(unShift.day).complete();
        unShift.type.complete();
        return unShift;
    }


    /*--Parcelable stuff--*/
    public static final Creator<NurseRoster> CREATOR = new Creator<NurseRoster>() {
        @Override
        public NurseRoster createFromParcel(Parcel in) {
            return new NurseRoster(in);
        }

        @Override
        public NurseRoster[] newArray(int size) {
            return new NurseRoster[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    protected NurseRoster(Parcel in) {
        reference = in.readString();

        if (in.readInt() == 0)
            return;

        int size = in.readInt();
        for (int i = 0; i < size; i++)
            days.put(ShiftDay.values()[in.readInt()], ShiftType.values()[in.readInt()]);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reference);

        dest.writeInt(isBuilt() ? 1 : 0);
        if (!isBuilt())
            return;

        dest.writeInt(days.size());
        for (ShiftDay key : days.keySet()) {
            dest.writeInt(key.ordinal());
            dest.writeInt(days.get(key).ordinal());
        }
    }
}
