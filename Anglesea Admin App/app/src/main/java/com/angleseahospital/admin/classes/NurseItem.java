package com.angleseahospital.admin.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.angleseahospital.admin.R;

public class NurseItem extends LinearLayout {

    private View view;

    private ImageView img;
    private TextView firstname;
    private TextView lastname;

    public NurseItem(Context context) {
        this(context, null);
    }

    public NurseItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NurseItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_nurse, this);

        img = view.findViewById(R.id.nurseItem_img_edit);
        firstname = view.findViewById(R.id.nurseItem_txt_firstname);
        lastname =view.findViewById(R.id.nurseItem_txt_lastname);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("NURSE ITEM WIDTH", w + "");
        if (w < 250) {
            firstname.setText(firstname.getText().toString() + " " + lastname.getText().toString());
            firstname.setTextSize(16);
            img.setVisibility(GONE);
            lastname.setVisibility(GONE);
        }
    }
}
