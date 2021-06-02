package com.angleseahospital.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angleseahospital.admin.firestore.NurseAdapter;

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

        v.findViewById(R.id.btn_addNurse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.changeCurrentFragment(getActivity(), getParentFragmentManager(), new AddEditNurse(), R.id.nav_addNurse);
            }
        });

        RecyclerView rv_nurse = v.findViewById(R.id.rv_home_nurse);
        NurseAdapter nurseAdapter = new NurseAdapter();
        
        rv_nurse.setHasFixedSize(true);
        rv_nurse.setAdapter(nurseAdapter);
        rv_nurse.setLayoutManager(new LinearLayoutManager(getContext()));

        nurseAdapter.setOnItemClickListener(new NurseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MainActivity.changeCurrentFragment(getHost(), getParentFragmentManager(), new AddEditNurse(nurseAdapter.get(position)), R.id.nav_addNurse);
            }
        });
    }
}
