package com.angleseahospital.nurse.firestore;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.angleseahospital.nurse.firestore.Shift.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Roster implements Parcelable {

    private String docRef;
    private boolean built = false;

    private HashMap<ShiftDay, ShiftType> days = new HashMap<>();

    public Roster(String docRef) {
        this.docRef = docRef;
    }

    public void build(FirebaseFirestore db, OnCompleteListener listener) { build(db, listener, docRef); }
    public void build(FirebaseFirestore db, OnCompleteListener listener, String docRef) {
        this.docRef = docRef;
        db.document(docRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful())
                    return;
                DocumentSnapshot documentSnapshot = task.getResult();
                for (ShiftDay shift : ShiftDay.values())
                    days.put(shift, ShiftType.fromString((String) documentSnapshot.get(shift.name().toLowerCase())));
                built = true;
            }
        }).addOnCompleteListener(listener);
    }

    public Shift getNextUncompletedShift() {
        if (!built)
            return null;
        for (ShiftDay shift : ShiftDay.values())
            if (!days.get(shift).isCompleted())
                return new Shift(shift, days.get(shift));
        return null;
    }

    public boolean earlySignout() {
        if (!built)
            return false;
        Shift unShift = getNextUncompletedShift();
        if (unShift == null)
            return false;
        //TODO: Add early signout
        return false;
    }

    public Shift completeShift() {
        if (!built)
            return null;
        Shift unShift = getNextUncompletedShift();
        if (unShift == null)
            return null;

        days.get(unShift.day).complete();
        unShift.type.complete();
        return unShift;
    }


    /*--Parcelable stuff--*/
    public static final Creator<Roster> CREATOR = new Creator<Roster>() {
        @Override
        public Roster createFromParcel(Parcel in) {
            return new Roster(in);
        }

        @Override
        public Roster[] newArray(int size) {
            return new Roster[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    protected Roster(Parcel in) {
        built = in.readInt() == 1;
        int size = in.readInt();
        for (int i = 0; i < size; i++)
            days.put(ShiftDay.values()[in.readInt()], ShiftType.values()[in.readInt()]);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(built ? 1 : 0);
        if (!built)
            return;

        dest.writeInt(days.size());
        for (ShiftDay key : days.keySet()) {
            dest.writeInt(key.ordinal());
            dest.writeInt(days.get(key).ordinal());
        }
    }
}
