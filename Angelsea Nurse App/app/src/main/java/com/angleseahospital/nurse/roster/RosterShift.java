package com.angleseahospital.nurse.roster;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.angleseahospital.nurse.R;
import com.angleseahospital.nurse.firestore.Shift;

public class RosterShift extends LinearLayout {
    private View view;
    private Context context;
    private TextView txt_display;
    private Shift.ShiftType type;

    public enum Color {
        ACTIVE,
        INACTIVE
    }

    public RosterShift(Context context) {
        this(context, null);
    }
    public RosterShift(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RosterShift(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.roster_shift, this);

    }

    public void construct() {
        txt_display = view.findViewById(R.id.txt_roster_shift_display);

        setActive(false);
    }

    public void setActive(Shift.ShiftType type) {
        this.type = type;

        if (type == Shift.ShiftType.NONE)
            setActive(false);
        else
            setActive(true);
    }
    public void setActive(boolean active) {
        if (active) {
            if (type == null)
                throw new IllegalStateException("A ShiftType must have been given");
            setColor(Color.ACTIVE);
            txt_display.setText(type.name().toUpperCase());
        } else {
            setColor(Color.INACTIVE);
            txt_display.setText("");
        }
    }

    public void setColor(Color color) {
        switch (color) {
            case INACTIVE:
                setColor(ContextCompat.getColor(context, R.color.shift_inactive));
                break;
            case ACTIVE:
                setColor(ContextCompat.getColor(context, R.color.shift_active));
                break;
            default:
                throw new IllegalArgumentException("Supplied color was not valid");
        }
    }
    public void setColor(int color) {
        txt_display.setBackgroundColor(color);
    }
}
