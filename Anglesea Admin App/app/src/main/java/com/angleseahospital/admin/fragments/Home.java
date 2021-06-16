package com.angleseahospital.admin.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angleseahospital.admin.MainActivity;
import com.angleseahospital.admin.R;
import com.angleseahospital.admin.firestore.NurseAdapter;
import com.angleseahospital.admin.firestore.NurseRoster;

import java.util.Calendar;

public class Home extends Fragment {

    public NurseAdapter nurseAdapter;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return v = inflater.inflate(R.layout.frag_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //Set a onClickListener for the Add Nurse button
        v.findViewById(R.id.btn_addNurse).setOnClickListener(v -> {
            //Opens the AddEditNurse fragment
            MainActivity.changeCurrentFragment(getActivity(), getParentFragmentManager(), new AddEditNurse(), R.id.nav_addNurse);
        });

        //Once the activity is created. Assign View member with its respective view
        RecyclerView rv_nurse = v.findViewById(R.id.rv_home_nurse);

        //Set the adapter and settings
        NurseAdapter nurseAdapter = new NurseAdapter(true);
        rv_nurse.setHasFixedSize(true);
        rv_nurse.setAdapter(nurseAdapter);
        rv_nurse.setLayoutManager(new LinearLayoutManager(getContext()));

        //Set OnClickListener to each NurseItem
        nurseAdapter.setOnItemClickListener(position ->
            //Opens the AddEditNurse fragment with clicked Nurse item
            MainActivity.changeCurrentFragment(getHost(), getParentFragmentManager(), new AddEditNurse(nurseAdapter.get(position)), R.id.nav_addNurse)
        );
    }
}
