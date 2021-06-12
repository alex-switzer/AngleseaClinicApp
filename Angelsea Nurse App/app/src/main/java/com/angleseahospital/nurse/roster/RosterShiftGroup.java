package com.angleseahospital.nurse.roster;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.angleseahospital.nurse.R;
import com.angleseahospital.nurse.firestore.Shift;

import java.util.HashMap;
import java.util.Map;

public class RosterShiftGroup extends LinearLayout {


    private Map<Shift.ShiftType, RosterShift> shifts = new HashMap<>();
    private Shift.ShiftType current;
    private Context context;

    private View view;

    public RosterShiftGroup(Context context) {
        this(context, null);
    }
    public RosterShiftGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RosterShiftGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.roster_shift_group, this);
    }

    public void construct() { construct(null); }
    public void construct(Shift.ShiftType type) {
        shifts.put(Shift.ShiftType.AM, view.findViewById(R.id.roster_shift_am));
        shifts.put(Shift.ShiftType.PM, view.findViewById(R.id.roster_shift_pm));
        shifts.put(Shift.ShiftType.NIGHT, view.findViewById(R.id.roster_shift_night));

        for (Shift.ShiftType key : shifts.keySet())
            shifts.get(key).construct();

        if (type == null)
            current = Shift.ShiftType.NONE;
        else
            setActive(type);
    }

    public void setActive(Shift.ShiftType type) {
        if (current == Shift.ShiftType.NONE) {
            if (type == Shift.ShiftType.NONE)
                return;
            current = type;
            shifts.get(type).setActive(type);
            return;
        }

        shifts.get(type).setActive(type);
        shifts.get(current).setActive(false);
        current = type;
    }
}
