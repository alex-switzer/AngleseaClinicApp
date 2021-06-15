package com.angleseahospital.nurse.roster;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.Nullable;

import com.angleseahospital.nurse.R;
import com.angleseahospital.nurse.firestore.NurseRoster;
import com.angleseahospital.nurse.firestore.Shift;

import java.util.HashMap;
import java.util.Map;

public class RosterView extends ScrollView {
    private View view;
    private Context context;
    private final Map<Shift.ShiftDay, RosterShiftGroup> groups = new HashMap<>();

    public RosterView(Context context) {
        this(context, null);
    }
    public RosterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RosterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.roster_view, this);
    }

    public void construct() {
        construct(null);
    }
    public void construct(NurseRoster roster) {
        groups.put(Shift.ShiftDay.MON, view.findViewById(R.id.roster_group_mon));
        groups.put(Shift.ShiftDay.TUE, view.findViewById(R.id.roster_group_tue));
        groups.put(Shift.ShiftDay.WED, view.findViewById(R.id.roster_group_wed));
        groups.put(Shift.ShiftDay.THU, view.findViewById(R.id.roster_group_thu));
        groups.put(Shift.ShiftDay.FRI, view.findViewById(R.id.roster_group_fri));
        groups.put(Shift.ShiftDay.SAT, view.findViewById(R.id.roster_group_sat));
        groups.put(Shift.ShiftDay.SUN, view.findViewById(R.id.roster_group_sun));

        for (Shift.ShiftDay day: groups.keySet())
            groups.get(day).construct();

        if (roster != null)
            displayRoster(roster);
    }

    public void displayRoster(NurseRoster roster) {
        if (!roster.isBuilt())
            throw new IllegalArgumentException("Roster is not built");

        Shift shift;
        for (Shift.ShiftDay day : Shift.ShiftDay.values()) {
            shift = roster.getShift(day);
            if (shift == null)
                continue;
            groups.get(shift.day).setActive(shift.type);
        }
    }
}
