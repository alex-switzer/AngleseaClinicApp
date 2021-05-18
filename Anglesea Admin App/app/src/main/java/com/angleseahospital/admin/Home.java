package com.angleseahospital.admin;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.angleseahospital.admin.firebase.NurseAdapter;

public class Home extends Fragment {

    private RecyclerView rc_nurse;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rc_nurse = getView().findViewById(R.id.rc_nurse);
        rc_nurse.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        NurseAdapter nurseAdapter = new NurseAdapter(/*TODO: Get nurses from database, load them into adapter*/);

        rc_nurse.setLayoutManager(layoutManager);
        rc_nurse.setAdapter(nurseAdapter);
    }
}