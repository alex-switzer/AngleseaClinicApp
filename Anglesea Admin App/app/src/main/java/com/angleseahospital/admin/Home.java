package com.angleseahospital.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angleseahospital.admin.firestore.NurseAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends Fragment {

    private NurseAdapter recyclerViewAdapter;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return v = inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        v.findViewById(R.id.btn_addNurse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.changeCurrentFragment(getActivity(), getParentFragmentManager(), new AddNurse(), R.id.nav_addNurse);
            }
        });

        RecyclerView rv_nurse = v.findViewById(R.id.rv_home_nurse);
        NurseAdapter nurseAdapter = new NurseAdapter();
        
        rv_nurse.setHasFixedSize(true);
        rv_nurse.setAdapter(nurseAdapter);
    }

}
