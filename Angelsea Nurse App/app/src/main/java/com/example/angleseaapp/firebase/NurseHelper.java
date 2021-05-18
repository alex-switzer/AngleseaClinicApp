package com.example.angleseaapp.firebase;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NurseHelper {
    private DatabaseReference mDatabase;
    private MutableLiveData<ArrayList<Nurse>> nurses;

    public NurseHelper() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance("https://anglesea-medical-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()
                .child(mUser.getUid()).child("nurses");

        nurses = new MutableLiveData<>();
        mDatabase.addValueEventListener(nurseListener);
    }

    public MutableLiveData<ArrayList<Nurse>> getNurses() {
        return nurses;
    }

    public void saveNurse(Nurse nurse){
        if (nurse.getId() == null) {
            mDatabase.push().setValue(nurse);
        } else {
            mDatabase.child(nurse.getId()).setValue(nurse);
        }
    }


    ValueEventListener nurseListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<Nurse> newNurses = new ArrayList<>();
            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                Nurse nurse = new Nurse();
                nurse.setId(ds.getKey());
                nurse.setName_first(ds.child("name_first").getValue(String.class));
                nurse.setName_last(ds.child("name_last").getValue(String.class));
                nurse.setPin(ds.child("pin").getValue(String.class));
                newNurses.add(nurse);
            }
            nurses.setValue(newNurses);
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
}
