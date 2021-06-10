package com.angleseahospital.nurse.firestore;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import com.angleseahospital.nurse.firestore.Shift.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class NurseRoster implements Parcelable {

    private String docRef;

    private HashMap<ShiftDay, ShiftType> days = new HashMap<>();

    public NurseRoster(String docRef) {
        this.docRef = docRef;
    }

    public void build(FirebaseFirestore db, OnCompleteListener listener) { build(db, listener, docRef); }
    public void build(FirebaseFirestore db, OnCompleteListener listener, String docRef) {
        this.docRef = docRef;
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

    public boolean isBuilt() {
        return days != null;
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
        if (in.readInt() == 0)
            return;

        int size = in.readInt();
        for (int i = 0; i < size; i++)
            days.put(ShiftDay.values()[in.readInt()], ShiftType.values()[in.readInt()]);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
