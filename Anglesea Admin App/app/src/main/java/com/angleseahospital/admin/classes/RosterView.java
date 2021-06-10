package com.angleseahospital.admin.classes;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.angleseahospital.admin.R;
import com.angleseahospital.admin.firestore.Constants;
import com.angleseahospital.admin.firestore.Nurse;
import com.angleseahospital.admin.firestore.NurseRoster;
import com.angleseahospital.admin.firestore.Shift;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import static com.angleseahospital.admin.firestore.NurseRoster.*;

public class RosterView {

    private TextView txt_weekdate;

    private RadioGroup pg_mon;
    private RadioGroup pg_tue;
    private RadioGroup pg_wed;
    private RadioGroup pg_thu;
    private RadioGroup pg_fri;
    private RadioGroup pg_sat;
    private RadioGroup pg_sun;

    private ImageButton navLeft;
    private ImageButton navRight;

    private Calendar selectedMonday;

    private Nurse nurse;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public RosterView(View v, Nurse nurse) {

        txt_weekdate = v.findViewById(R.id.roster_txt_weekdate);

        pg_mon = v.findViewById(R.id.rgroup_Mon);
        pg_tue = v.findViewById(R.id.rgroup_Tue);
        pg_wed = v.findViewById(R.id.rgroup_Wed);
        pg_thu = v.findViewById(R.id.rgroup_Thu);
        pg_fri = v.findViewById(R.id.rgroup_Fri);
        pg_sat = v.findViewById(R.id.rgroup_Sat);
        pg_sun = v.findViewById(R.id.rgroup_Sun);

        navLeft = v.findViewById(R.id.roster_btn_nav_left);
        navRight = v.findViewById(R.id.roster_btn_nav_right);

        this.nurse = nurse;
    }

    public void prevWeek() {
        setNavEnabled(false);
        //TODO: Query Database then update view
        displayRoster();
    }

    public void nextWeek() {
        setNavEnabled(false);
        //TODO: Query Database then update view
        displayRoster();
    }


    public void setNavEnabled(boolean enabled) {
        navLeft.setEnabled(enabled);
        navRight.setEnabled(enabled);
    }

    public void displayRoster() { displayRoster(nurse.roster); }
    public void displayRoster(NurseRoster roster) {
//        selectedMonday =

        txt_weekdate.setText(getThisWeeksRosterDate());

        if (roster.getTotalShifts() == 0)
            return;

        for (Shift.ShiftDay day : roster.getShifts().keySet()) {
            Shift shift = roster.getShift(day);
            if (shift == null)
                continue;

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
                pg_mon.check(R.id.rbtn_Mon_AM);
                break;
            case TUE:
                pg_tue.check(R.id.rbtn_Tue_AM);
                break;
            case WED:
                pg_wed.check(R.id.rbtn_Wed_AM);
                break;
            case THU:
                pg_thu.check(R.id.rbtn_Thu_AM);
                break;
            case FRI:
                pg_fri.check(R.id.rbtn_Fri_AM);
                break;
            case SAT:
                pg_sat.check(R.id.rbtn_Sat_AM);
                break;
            case SUN:
                pg_sun.check(R.id.rbtn_Sun_AM);
                break;
        }
    }
    private void togglePM(Shift.ShiftDay day) {
        switch (day) {
            case MON:
                pg_mon.check(R.id.rbtn_Mon_PM);
                break;
            case TUE:
                pg_tue.check(R.id.rbtn_Tue_PM);
                break;
            case WED:
                pg_wed.check(R.id.rbtn_Wed_PM);
                break;
            case THU:
                pg_thu.check(R.id.rbtn_Thu_PM);
                break;
            case FRI:
                pg_fri.check(R.id.rbtn_Fri_PM);
                break;
            case SAT:
                pg_sat.check(R.id.rbtn_Sat_PM);
                break;
            case SUN:
                pg_sun.check(R.id.rbtn_Sun_PM);
                break;
        }
    }
    private void toggleNight(Shift.ShiftDay day) {
        switch (day) {
            case MON:
                pg_mon.check(R.id.rbtn_Mon_Night);
                break;
            case TUE:
                pg_tue.check(R.id.rbtn_Tue_Night);
                break;
            case WED:
                pg_wed.check(R.id.rbtn_Wed_Night);
                break;
            case THU:
                pg_thu.check(R.id.rbtn_Thu_Night);
                break;
            case FRI:
                pg_fri.check(R.id.rbtn_Fri_Night);
                break;
            case SAT:
                pg_sat.check(R.id.rbtn_Sat_Night);
                break;
            case SUN:
                pg_sun.check(R.id.rbtn_Sun_Night);
                break;
        }
    }

}
