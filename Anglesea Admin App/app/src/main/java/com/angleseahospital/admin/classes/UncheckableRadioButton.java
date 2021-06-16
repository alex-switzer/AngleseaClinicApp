package com.angleseahospital.admin.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

public class UncheckableRadioButton extends AppCompatRadioButton {

    public UncheckableRadioButton(Context context) {
        super(context);
    }

    public UncheckableRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UncheckableRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Overrides the toggle functionality to allow for turning off the button once clicked while checked
     */
    @Override
    public void toggle() {
        //If button is already checked, uncheck it
        if (isChecked()) {
            if (getParent() != null && getParent() instanceof RadioGroup) {
                ((RadioGroup) getParent()).clearCheck();
            }
        } else {
            super.toggle();
        }
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return UncheckableRadioButton.class.getName();
    }
}
