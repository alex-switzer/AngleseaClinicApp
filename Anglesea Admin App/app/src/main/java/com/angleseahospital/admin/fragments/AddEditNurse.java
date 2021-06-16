package com.angleseahospital.admin.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.angleseahospital.admin.MainActivity;
import com.angleseahospital.admin.R;
import com.angleseahospital.admin.classes.RosterView;
import com.angleseahospital.admin.firestore.Constants;
import com.angleseahospital.admin.firestore.Nurse;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddEditNurse extends Fragment {
    private View v;

    private EditText etxt_firstname;
    private EditText etxt_lastname;
    private EditText etxt_pin;

    public Nurse nurse;

    private RosterView rosterView;

    private String firstname;
    private String lastname;
    private String pin;

    private boolean editing;

    /**
     * Constructs a fragment with no nurse to edit
     */
    public AddEditNurse() {
        nurse = new Nurse();
        nurse.generateID();
        editing = false;
    }

    /**
     * Constructs a fragment to edit given Nurse object
     * @param nurse
     */
    public AddEditNurse(@NonNull Nurse nurse) {
        this.nurse = nurse;
        editing = true;
    }

    /**
     * Once fragment is given a view inflate it's layout
     * @param inflater Layout inflater for this fragment
     * @param container Parent container for this fragment
     * @param savedInstanceState Bundle for previous fragment instance
     * @return View for the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return v = inflater.inflate(R.layout.frag_addeditnurse, container, false);
    }

    /**
     * Sets up this fragment once layout is inflated
     * @param savedInstanceState Bundle from prior instance
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Once the activity is created. Assign View members with their respective views
        etxt_firstname = v.findViewById(R.id.etxt_firstname);
        etxt_lastname = v.findViewById(R.id.etxt_lastname);
        etxt_pin = v.findViewById(R.id.etxt_pin);

        //If not editing a nurse
        if (!editing)
            return;

        //Create a RosterView in the fragments view with passed nurse
        rosterView = new RosterView(v, nurse);

        //Setup views with nurses details
        etxt_firstname.setText(nurse.firstName);
        etxt_lastname.setText(nurse.lastName);
        etxt_pin.setText(nurse.pin);

        //Build nurses roster if it isn't already. Then display roster
        if (!nurse.roster.isBuilt())
            nurse.roster.build().addOnCompleteListener(task -> rosterView.displayRoster());
        else
            rosterView.displayRoster();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //Add the add_edit_nurse_menu options to the current toolbar / options menu
        inflater.inflate(R.menu.add_edit_nurse_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Activates option menus buttons functionality respectively
        switch(item.getItemId()) {
            case R.id.save_nurse_menu:
                save();
               //Consumes item selected call
                return true;
            case R.id.delete_nurse_menu:
                delete();
                //Consumes item selected call
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void delete() {
        if (!editing)
            goHome();

        //Toasts must have a context in order to show. But deleting causes the context to be lost
        FirebaseFirestore.getInstance()
                .document(Constants.COLLECTION_NURSES + "/" + nurse.id)
                .delete()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(), "Nurse Failed to delete", Toast.LENGTH_LONG).show();
                        return;
                    }
                    goHome();
                });
    }

    /**
     * Attempts to save the current nurse
     */
    public void save() {
        //Doesn't attempt to save if any input is invalid
        if (!verifyInputs()) {
            //TODO: Add visual indication that nurse changes didn't save
            return;
        }

        //Get nurses details
        nurse.firstName = firstname;
        nurse.lastName = lastname;
        nurse.pin = pin;

        //Build the roster from the input view and if successful, update the database
        if (nurse.roster.build(v))
            nurse.updateDatabase(editing).continueWith(task -> {
                if (!task.isSuccessful()) {
                    Log.d("UpdateDatabaseContinuation", "Database Failed to Update");
                    Log.e("UpdateDatabaseContinuation", task.getException().getMessage());
                    Toast.makeText(getContext(), "Nurse Failed to save", Toast.LENGTH_LONG).show();
                    //TODO: Display failure to save nurse more clearly
                    return null;
                }
                Log.d("UpdateDatabaseContinuation", "Database Updated!");
                Toast.makeText(getContext(), "Nurse Saved!", Toast.LENGTH_SHORT).show();
                goHome();
                return null;
            });
    }

    private void goHome() {
        MainActivity.changeCurrentFragment(getHost(), getParentFragmentManager(), new Home(), R.id.nav_home);
    }

    /**
     * Verifies the inputs for the nurses details
     * @return False if any input is invalid
     */
    private boolean verifyInputs() {
        String firstname = etxt_firstname.getText().toString().trim();
        if (firstname.equals(""))
            return false;

        String lastname = etxt_lastname.getText().toString().trim();
        if (lastname.equals(""))
            return false;

        String pin = etxt_pin.getText().toString().trim();
        if (pin.equals(""))
            return false;
        if (pin.length() != Constants.NURSE_PIN_LENGTH)
            return false;

        this.firstname = firstname;
        this.lastname = lastname;
        this.pin = pin;

        return true;
    }
}