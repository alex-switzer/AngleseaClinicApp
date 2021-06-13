package com.angleseahospital.admin.firestore;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.angleseahospital.admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.angleseahospital.admin.firestore.Shift.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
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
        monday.setFirstDayOfWeek(Calendar.MONDAY);
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

    private Calendar monday;
    private HashMap<ShiftDay, ShiftType> days;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    public boolean build(View v) {
        days = new HashMap<>();
        //TODO: Link to RosterView
        RadioGroup dg_mon = v.findViewById(R.id.rgroup_Mon);
        RadioGroup dg_tue = v.findViewById(R.id.rgroup_Tue);
        RadioGroup dg_wed = v.findViewById(R.id.rgroup_Wed);
        RadioGroup dg_thu = v.findViewById(R.id.rgroup_Thu);
        RadioGroup dg_fri = v.findViewById(R.id.rgroup_Fri);
        RadioGroup dg_sat = v.findViewById(R.id.rgroup_Sat);
        RadioGroup dg_sun = v.findViewById(R.id.rgroup_Sun);

        if (dg_mon == null || dg_tue == null || dg_wed == null || dg_thu == null || dg_fri == null || dg_sat == null || dg_sun == null)
            return false;

        switch (dg_mon.getCheckedRadioButtonId()) {
            case R.id.rbtn_Mon_AM:
                days.put(ShiftDay.MON, ShiftType.AM);
                break;
            case R.id.rbtn_Mon_PM:
                days.put(ShiftDay.MON, ShiftType.PM);
                break;
            case R.id.rbtn_Mon_Night:
                days.put(ShiftDay.MON, ShiftType.NIGHT);
                break;
        }
        switch (dg_tue.getCheckedRadioButtonId()) {
            case R.id.rbtn_Tue_AM:
                days.put(ShiftDay.TUE, ShiftType.AM);
                break;
            case R.id.rbtn_Tue_PM:
                days.put(ShiftDay.TUE, ShiftType.PM);
                break;
            case R.id.rbtn_Tue_Night:
                days.put(ShiftDay.TUE, ShiftType.NIGHT);
                break;
        }
        switch (dg_wed.getCheckedRadioButtonId()) {
            case R.id.rbtn_Wed_AM:
                days.put(ShiftDay.WED, ShiftType.AM);
                break;
            case R.id.rbtn_Wed_PM:
                days.put(ShiftDay.WED, ShiftType.PM);
                break;
            case R.id.rbtn_Wed_Night:
                days.put(ShiftDay.WED, ShiftType.NIGHT);
                break;
        }
        switch (dg_thu.getCheckedRadioButtonId()) {
            case R.id.rbtn_Thu_AM:
                days.put(ShiftDay.THU, ShiftType.AM);
                break;
            case R.id.rbtn_Thu_PM:
                days.put(ShiftDay.THU, ShiftType.PM);
                break;
            case R.id.rbtn_Thu_Night:
                days.put(ShiftDay.THU, ShiftType.NIGHT);
                break;
        }
        switch (dg_fri.getCheckedRadioButtonId()) {
            case R.id.rbtn_Fri_AM:
                days.put(ShiftDay.FRI, ShiftType.AM);
                break;
            case R.id.rbtn_Fri_PM:
                days.put(ShiftDay.FRI, ShiftType.PM);
                break;
            case R.id.rbtn_Fri_Night:
                days.put(ShiftDay.FRI, ShiftType.NIGHT);
                break;
        }
        switch (dg_sat.getCheckedRadioButtonId()) {
            case R.id.rbtn_Sat_AM:
                days.put(ShiftDay.SAT, ShiftType.AM);
                break;
            case R.id.rbtn_Sat_PM:
                days.put(ShiftDay.SAT, ShiftType.PM);
                break;
            case R.id.rbtn_Sat_Night:
                days.put(ShiftDay.SAT, ShiftType.NIGHT);
                break;
        }
        switch (dg_sun.getCheckedRadioButtonId()) {
            case R.id.rbtn_Sun_AM:
                days.put(ShiftDay.SUN, ShiftType.AM);
                break;
            case R.id.rbtn_Sun_PM:
                days.put(ShiftDay.SUN, ShiftType.PM);
                break;
            case R.id.rbtn_Sun_Night:
                days.put(ShiftDay.SUN, ShiftType.NIGHT);
                break;
        }

        return true;
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

    public Calendar getMonday() {
        return monday;
    }

    public Shift getNextUncompletedShift() {
        if (!isBuilt())
            return null;
        for (ShiftDay shift : ShiftDay.values())
            if (!days.get(shift).isCompleted())
                return new Shift(shift, days.get(shift));
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
    public int getTotalShifts() {
        if (!isBuilt())
            return 0;
        return days.size();
    }

    public Iterator<ShiftDay> getIterator() {
        if (!isBuilt())
            return null;
        return days.keySet().iterator();
    }

    public HashMap<ShiftDay, ShiftType> getShifts() {
        return days;
    }

    public boolean isBuilt() { return days != null; }

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
