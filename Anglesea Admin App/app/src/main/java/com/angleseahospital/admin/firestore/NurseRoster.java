package com.angleseahospital.admin.firestore;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.angleseahospital.admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import com.angleseahospital.admin.firestore.Shift.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class NurseRoster implements Parcelable {

    public String reference;

    private HashMap<ShiftDay, ShiftType> days;

    public NurseRoster() {  }

    public NurseRoster(String reference) {
        this.reference = reference;
    }

    public static String getTodaysRosterReference() {
        //TODO: Get this weeks roster
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return "roster/" + cal.get(Calendar.YEAR) + "/" + (month < 10 ? "0" + month : month) + "/" + cal.get(Calendar.MONDAY);
    }

    public void build(OnCompleteListener listener) throws IllegalArgumentException {
        if (reference == null || reference.equals(""))
            throw new IllegalArgumentException("No doc reference provided");
        build(listener, reference);
    }
    public void build(OnCompleteListener listener, String docRef) {
        this.reference = docRef;
        FirebaseFirestore.getInstance().document(docRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
            }
        }).addOnCompleteListener(listener);
    }
    public boolean build(View v) {
        days = new HashMap<>();
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
        //TODO: Update roster on DB
        return null;
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
        if (in.readInt() == 0)
            return;

        int size = in.readInt();
        days = new HashMap<>();
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
