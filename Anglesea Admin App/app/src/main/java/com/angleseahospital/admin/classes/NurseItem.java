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

    /**
     * Constructs a NurseItem and inflates the layout
     * @param context Context the view is being inflated in
     * @param attrs Views attributes
     * @param defStyleAttr Style attributes
     */
    public NurseItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //Inflate the respective layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_nurse, this);

        //Sets up member variables with their respective views
        img = view.findViewById(R.id.nurseItem_img_edit);
        firstname = view.findViewById(R.id.nurseItem_txt_firstname);
        lastname =view.findViewById(R.id.nurseItem_txt_lastname);
    }

    /**
     * Adapts the views style depending on the dimensions
     * @param w New width
     * @param h New height
     * @param oldw Old width
     * @param oldh Old height
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w < 250) {
            firstname.setTextSize(16);
            lastname.setTextSize(14);
            img.setVisibility(GONE);
        }
    }
}
