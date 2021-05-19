package com.angleseahospital.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angleseahospital.admin.firebase.Nurse;
import com.angleseahospital.admin.firebase.NurseAdapter;
import com.angleseahospital.admin.firebase.NurseHelper;

import java.util.ArrayList;

public class Home extends Fragment {

    private NurseAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView rc_nurse = getView().findViewById(R.id.rc_nurse);
        rc_nurse.setHasFixedSize(true);

        /*NurseHelper nurseHelper = new NurseHelper();
        nurseHelper.getNurses().observe(getViewLifecycleOwner(), new Observer<ArrayList<Nurse>>() {
            @Override
            public void onChanged(ArrayList<Nurse> nurseArrayList) {
                recyclerViewAdapter = new NurseAdapter(getContext(), nurseArrayList);
                rc_nurse.setLayoutManager(new LinearLayoutManager(getContext()));
                rc_nurse.setAdapter(recyclerViewAdapter);
            }
        });*/
    }
}