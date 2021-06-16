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

    /**
     * Creates a path string to this weeks roster for nurse with given ID
     * @param nursesID ID of nurse to path to
     * @return Path string to this weeks roster for nurse
     */
    public static String getThisWeeksRosterPath(String nursesID) {
        return Constants.COLLECTION_ROSTERS + "/" + getThisWeeksRosterDate() + "/nurses/" + nursesID;
    }

    /**
     * Creates a path string to this weeks roster
     * @return Path string to this weeks roster
     */
    public static String getThisWeeksRosterPath() {
        return Constants.COLLECTION_ROSTERS + "/" + getThisWeeksRosterDate();
    }

    /**
     * Returns the current weeks roster string date. i.e yyyy/mm/dd
     * @return
     */
    public static String getThisWeeksRosterDate() {
        return getWeeksDate(Calendar.getInstance());
    }

    /**
     * Returns the roster date string for a given day of a week. i.e yyyy/mm/dd
     * @param dayOfWeek Calendar instance for a day of the week to get roster string for
     * @return Roster date string
     */
    public static String getWeeksDate(Calendar dayOfWeek) {
        Calendar monday = getWeeksMonday(dayOfWeek);

        int year = monday.get(Calendar.YEAR);
        int month = monday.get(Calendar.MONTH) + 1;
        int dayOfMonth = monday.get(Calendar.DATE);

        return year + "/" + (month < 10 ? "0" + month : month) + "/" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
    }

    /**
     * Retuns a Calendar instance for this weeks Monday
     * @return Calendar instance for this weeks Monday
     */
    public static Calendar getThisWeeksMonday() {
        return getWeeksMonday(Calendar.getInstance());
    }

    /**
     * Returns a Calendar instance for a given day of the weeks Monday
     * @param dayOfWeek Day of the week to create Mondays Calendar instance from
     * @return Calendar instance for a given day of the weeks Monday
     */
    public static Calendar getWeeksMonday(Calendar dayOfWeek) {
        dayOfWeek.setFirstDayOfWeek(Calendar.MONDAY);
        dayOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return dayOfWeek;
    }

    /**
     * Returns a Calendar instance for last weeks Monday
     * @return Calendar instance for last weeks Monday
     */
    public static Calendar getPrevWeeksMonday() { return getPrevWeeksMonday(getThisWeeksMonday()); }

    /**
     * Returns a Calendar instance for the Monday prior to the given Monday
     * @param currentMonday Monday to get the prior weeks Monday from
     * @return Calendar instance for the prior weeks Monday
     */
    public static Calendar getPrevWeeksMonday(Calendar currentMonday) {
        currentMonday.add(Calendar.DATE, -7);  // for previous Monday
        return currentMonday;
    }

    /**
     * Returns a Calendar instance for next weeks Monday
     * @return Calendar instance for next weeks Monday
     */
    public static Calendar getNextWeeksMonday() { return getNextWeeksMonday(getThisWeeksMonday()); }

    /**
     * Returns a Calendar instance for the following weeks Monday to a given weeks Monday
     * @param currentMonday The Monday to retrieve the next Monday from
     * @return Calendar instance of the Monday following the given Monday
     */
    public static Calendar getNextWeeksMonday(Calendar currentMonday) {
        currentMonday.add(Calendar.DATE, 7);  // for next Monday
        return currentMonday;
    }


    public String reference;

    private HashMap<ShiftDay, ShiftType> days;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Empty constructor for the NurseRoster
     */
    public NurseRoster() {  }

    /**
     * Constructs a NurseRoster with a reference set
     * @param reference Reference to set
     * @throws IllegalArgumentException if {@code reference} is empty
     */
    public NurseRoster(@NonNull String reference) {
        if (reference.length() == 0)
            throw new IllegalArgumentException("Reference cannot be empty");
        this.reference = reference;
    }

    /**
     * Builds the roster with the preset reference
     * @param listener Listener to be called once built
     * @throws IllegalArgumentException if preset reference was not set
     */
    public void build(OnCompleteListener listener) {
        if (reference == null || reference.equals(""))
            throw new IllegalArgumentException("No doc reference provided");
        build(listener, reference);
    }

    /**
     * Builds the roster with given reference
     * @param listener Listener to be called once construction is complete
     * @param docRef Roster document reference to be built from
     */
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

    /**
     * Builds the roster with given view
     * @param v View to build roster from
     * @return Whether the build was successful
     */
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

    /**
     * Updates the database with built roster. Also updates the nurses roster reference
     * @param nurseID ID of nurse to update
     * @return Task of the database update operation
     */
    public Task<Void> update(String nurseID) {
        return update(nurseID, reference);
    }

    /**
     * Updates the database with built roster
     * @param nurseID ID of the nurse to update roster for
     * @param reference Reference to the roster document to update
     * @return Task of the database update operation
     * @throws IllegalStateException if roster has not been built before
     * @throws IllegalArgumentException if {@code reference} is empty
     */
    public Task<Void> update(String nurseID, @NonNull String reference) {
        if (reference.length() == 0)
            throw new IllegalArgumentException("Given reference cannot be empty");
        if (!isBuilt())
            throw new IllegalStateException("Roster has to be built first");
        this.reference = reference;

        WriteBatch batch = db.batch();
        //TODO: Don't necessarily change the roster reference for the nurse
        DocumentReference nurseDocRef = db.collection(Constants.COLLECTION_NURSES).document(nurseID);
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

    /**
     * Gets the next uncompleted shift in the roster
     * @return Last uncompleted Shift object
     * @throws IllegalStateException if roster has not been build
     */
    public Shift getNextUncompletedShift() {
        if (!isBuilt())
            throw new IllegalStateException("Roster must be built first");
        for (ShiftDay shift : ShiftDay.values())
            if (!days.get(shift).isCompleted())
                return new Shift(shift, days.get(shift));
        return null;
    }

    /**
     * Gets the shift of given day
     * @param day The day to get the shift for
     * @return Shift for the given day
     */
    public Shift getShift(ShiftDay day) {
        if (!isBuilt())
            return null;
        ShiftType type = days.get(day);
        if (type == null)
            return null;
        return new Shift(day, type);
    }

    /**
     * Gets the total amount of shifts
     * @return The total amount of shifts. -1 if roster has not been built
     */
    public int getTotalShifts() {
        if (!isBuilt())
            return -1;
        return days.size();
    }

    /**
     * Returns an Iterator for all shifts
     * @return The Iterator for every shift
     * @throws IllegalStateException if roster has not been built
     */
    public Iterator<ShiftDay> getIterator() {
        if (!isBuilt())
            throw new IllegalStateException("Roster must be built first");
        return days.keySet().iterator();
    }

    /**
     * Gets all shifts in a HashMap
     * @return HashMap of all shifts
     */
    public HashMap<ShiftDay, ShiftType> getShifts() {
        return days;
    }

    /**
     * Returns true if the roster is built. False otherwise
     * @return if roster has been built
     */
    public boolean isBuilt() { return days != null; }


    /*
    //To be implemented later
    private boolean earlySignout() {
        if (!isBuilt())
            return false;
        Shift unShift = getNextUncompletedShift();
        if (unShift == null)
            return false;
        //TODO: Add early signout
        return false;
    }
    private Shift completeShift() {
        if (!isBuilt())
            return null;

        Shift unShift = getNextUncompletedShift();
        if (unShift == null)
            return null;

        days.get(unShift.day).complete();
        unShift.type.complete();
        return unShift;
    }*/


    /*--Parcelable stuff--*/
    /**
     * Gets the CREATOR object for this NurseRoster
     */
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

    /**
     * Describes the contents for Parcels
     * @return Described contents
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Constructs NurseRoster from Parcel object
     * @param in Parcel to be read from
     */
    protected NurseRoster(Parcel in) {
        reference = in.readString();

        if (in.readInt() == 0)
            return;

        int size = in.readInt();
        for (int i = 0; i < size; i++)
            days.put(ShiftDay.values()[in.readInt()], ShiftType.values()[in.readInt()]);
    }

    /**
     * Writes contents of this NurseRoster to given Parcel
     * @param dest Parcel to be written to
     * @param flags Flags for Parcel
     */
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
