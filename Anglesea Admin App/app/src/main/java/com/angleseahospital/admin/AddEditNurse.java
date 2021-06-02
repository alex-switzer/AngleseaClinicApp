package com.angleseahospital.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.angleseahospital.admin.firestore.Nurse;

public class AddEditNurse extends Fragment {
    private View v;

    public Nurse nurse;

    public AddEditNurse() { }

    public AddEditNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return v = inflater.inflate(R.layout.frag_addeditnurse, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (nurse == null)
            return;

        ((EditText)v.findViewById(R.id.etxt_firstname)).setText(nurse.firstName);
        ((EditText)v.findViewById(R.id.etxt_lastname)).setText(nurse.lastName);
        ((EditText)v.findViewById(R.id.etxt_pin)).setText(nurse.pin);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //TODO: Update cache firestore with the new added or edited nurse
    }
}