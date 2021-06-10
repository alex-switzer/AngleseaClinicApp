package com.angleseahospital.admin.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.angleseahospital.admin.R;
import com.angleseahospital.admin.firestore.Constants;
import com.angleseahospital.admin.firestore.Nurse;
import com.angleseahospital.admin.firestore.Shift;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

public class AddEditNurse extends Fragment {
    private View v;

    private EditText etxt_firstname;
    private EditText etxt_lastname;
    private EditText etxt_pin;

    private RadioGroup pg_roster_mon;
    private RadioGroup pg_roster_tue;
    private RadioGroup pg_roster_wed;
    private RadioGroup pg_roster_thu;
    private RadioGroup pg_roster_fri;
    private RadioGroup pg_roster_sat;
    private RadioGroup pg_roster_sun;

    public Nurse nurse;

    private String firstname;
    private String lastname;
    private String pin;

    private boolean editing = false;

    public AddEditNurse() {
        nurse = new Nurse();
        nurse.generateID();
    }

    public AddEditNurse(Nurse nurse) {
        this.nurse = nurse;
        editing = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return v = inflater.inflate(R.layout.frag_addeditnurse, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etxt_firstname = v.findViewById(R.id.etxt_firstname);
        etxt_lastname = v.findViewById(R.id.etxt_lastname);
        etxt_pin = v.findViewById(R.id.etxt_pin);

        pg_roster_mon = v.findViewById(R.id.rgroup_Mon);
        pg_roster_tue = v.findViewById(R.id.rgroup_Tue);
        pg_roster_wed = v.findViewById(R.id.rgroup_Wed);
        pg_roster_thu = v.findViewById(R.id.rgroup_Thu);
        pg_roster_fri = v.findViewById(R.id.rgroup_Fri);
        pg_roster_sat = v.findViewById(R.id.rgroup_Sat);
        pg_roster_sun = v.findViewById(R.id.rgroup_Sun);

        if (nurse == null || !editing)
            return;

        etxt_firstname.setText(nurse.firstName);
        etxt_lastname.setText(nurse.lastName);
        etxt_pin.setText(nurse.pin);

        if (!nurse.roster.isBuilt())
            nurse.roster.build(task -> setupRoster());
        else
            setupRoster();
    }

    private void setupRoster() {
        Log.d("ATTENNNNNNNTION!", " ROSTER IS BEING BUILT");
        if (nurse.roster.getTotalShifts() == 0)
            return;

        for (Shift.ShiftDay day : nurse.roster.getShifts().keySet()) {
            Shift shift = nurse.roster.getShift(day);
            if (shift == null) {
                continue;
            }
            switch (shift.type) {
                case AM:
                    toggleAM(day);
                    break;
                case PM:
                    togglePM(day);
                    break;
                case NIGHT:
                    toggleNight(day);
                    break;
                case NONE:
                default:
                    break;
            }
        }
    }

    private void toggleAM(Shift.ShiftDay day) {
        switch (day) {
            case MON:
                pg_roster_mon.check(R.id.rbtn_Mon_AM);
                break;
            case TUE:
                pg_roster_tue.check(R.id.rbtn_Tue_AM);
                break;
            case WED:
                pg_roster_wed.check(R.id.rbtn_Wed_AM);
                break;
            case THU:
                pg_roster_thu.check(R.id.rbtn_Thu_AM);
                break;
            case FRI:
                pg_roster_fri.check(R.id.rbtn_Fri_AM);
                break;
            case SAT:
                pg_roster_sat.check(R.id.rbtn_Sat_AM);
                break;
            case SUN:
                pg_roster_sun.check(R.id.rbtn_Sun_AM);
                break;
        }
    }
    private void togglePM(Shift.ShiftDay day) {
        switch (day) {
            case MON:
                pg_roster_mon.check(R.id.rbtn_Mon_PM);
                break;
            case TUE:
                pg_roster_tue.check(R.id.rbtn_Tue_PM);
                break;
            case WED:
                pg_roster_wed.check(R.id.rbtn_Wed_PM);
                break;
            case THU:
                pg_roster_thu.check(R.id.rbtn_Thu_PM);
                break;
            case FRI:
                pg_roster_fri.check(R.id.rbtn_Fri_PM);
                break;
            case SAT:
                pg_roster_sat.check(R.id.rbtn_Sat_PM);
                break;
            case SUN:
                pg_roster_sun.check(R.id.rbtn_Sun_PM);
                break;
        }
    }
    private void toggleNight(Shift.ShiftDay day) {
        switch (day) {
            case MON:
                pg_roster_mon.check(R.id.rbtn_Mon_Night);
                break;
            case TUE:
                pg_roster_tue.check(R.id.rbtn_Tue_Night);
                break;
            case WED:
                pg_roster_wed.check(R.id.rbtn_Wed_Night);
                break;
            case THU:
                pg_roster_thu.check(R.id.rbtn_Thu_Night);
                break;
            case FRI:
                pg_roster_fri.check(R.id.rbtn_Fri_Night);
                break;
            case SAT:
                pg_roster_sat.check(R.id.rbtn_Sat_Night);
                break;
            case SUN:
                pg_roster_sun.check(R.id.rbtn_Sun_Night);
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (!verifyInputs()) {
            //TODO: Add visual indication that nurse changes didn't save
            return;
        }

        nurse.firstName = firstname;
        nurse.lastName = lastname;
        nurse.pin = pin;

        if (nurse.roster.build(v))
            nurse.updateRoster();

        nurse.updateDatabase(editing).continueWith(task -> {
            if (task.isSuccessful())
                Log.d("UpdateDatabaseContinuation", "Database Updated!");
            else {
                Log.d("UpdateDatabaseContinuation", "Database Failed to Update");
                Log.e("UpdateDatabaseContinuation", task.getException().getMessage());
            }
            return null;
        });
        //TODO: Update cache firestore with the new added or edited nurse
    }

    private boolean verifyInputs() {
        String firstname = etxt_firstname.getText().toString().trim();
        if (firstname.equals(""))
            return false;

        String lastname = etxt_lastname.getText().toString().trim();
        if (lastname.equals(""))
            return false;

        String pin = etxt_pin.getText().toString().trim();
        if (pin.equals(""))
            return false;
        if (pin.length() != Constants.NURSE_PIN_LENGTH)
            return false;

        this.firstname = firstname;
        this.lastname = lastname;
        this.pin = pin;

        return true;
    }
}