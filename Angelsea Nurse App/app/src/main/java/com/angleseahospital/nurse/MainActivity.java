package com.angleseahospital.nurse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import com.angleseahospital.nurse.firestore.Nurse;
import com.angleseahospital.nurse.firestore.Shift;
import com.angleseahospital.nurse.classes.Util;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "PinLockView";
    public static final String NAME_ID_EXTRA = "NameID";
    public static final String SIGNING_STATUS_EXTRA = "SigningStatus";
    public static final int RC_SIGN_IN = 1;
    public static final String NURSE_OBJECT = "NurseObject";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public enum SigningStatus {
        SIGNING_IN,
        SIGNING_OUT,
        SIGNING_OUT_EARLY
    }

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;

    private Task<DocumentSnapshot> check;
    private boolean todayDirectoryExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Authenticate this apps instance to the Firebase project if they haven't been authenticated before
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }

        mPinLockView = findViewById(R.id.pin_lock_view);
        mIndicatorDots = findViewById(R.id.indicator_dots);
        //TODO: Add loading circle while checking the logs collection

        //Check if todays collection exists. If not, creates it
        check = db.collection("/logs/").document(Util.getToday() + "/signings/nurses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot result = task.getResult();
                todayDirectoryExists = result.exists();
                Log.d(TAG, "EXISTS: " + todayDirectoryExists);
            }
        }).continueWith(task -> {
            if (!todayDirectoryExists) {
                db.collection("/logs/").document(Util.getToday() + "/signings/nurses").set(new HashMap<>()).continueWith(task1 -> {
                    mPinLockView.setVisibility(View.VISIBLE);
                    mIndicatorDots.setVisibility(View.VISIBLE);
                    return null;
                });
            } else {
                mPinLockView.setVisibility(View.VISIBLE);
                mIndicatorDots.setVisibility(View.VISIBLE);
            }
            return null;
        });

        mIndicatorDots.setBackgroundColor(getColor(R.color.greyish));
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(getColor(R.color.black));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(final String pin) {
                Log.d(TAG, "pin entered: " + pin);
                //Queries Firestore Nurses table for all nurses
                db.collection("nurses")
                        .whereEqualTo("pin", pin) //Get all nurses with the pin
                        .get(Source.SERVER).continueWith(task -> {
                            mPinLockView.resetPinLockView();
                            if (!task.isSuccessful()) {
                                Log.e(TAG, "Task was not successful: " + task.getException());
                                return null;
                            }

                            QuerySnapshot result = task.getResult();
                            if (result == null) {
                                Log.e(TAG, "Empty response for nurses collection");
                                return null;
                            }
                            //Loop through all nurses
                            for (QueryDocumentSnapshot nurse : result) {
                                signQueriedNurse(new Nurse(nurse));
                                return null;
                            }
                            return null;
                        });
            }

            //Opens the ConfirmationActivity with the given nurse
            public void signQueriedNurse(Nurse nurse) {
                if (nurse == null)
                    return;

                if (nurse.lastSign != null && nurse.lastSign.equals(Shift.get24Time())) {
                    //TODO: Show error for signing again too soon
                    return;
                }

                Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                intent.putExtra(NURSE_OBJECT, nurse);
                if (nurse.present)
                    intent.putExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_OUT.ordinal());
                else
                    intent.putExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_IN.ordinal());
                startActivity(intent);
            }

            @Override
            public void onEmpty() { }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) { }
        });
    }

    public void signQueriedNurse(Nurse nurse) {
        Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
        intent.putExtra(NURSE_OBJECT, nurse);
        if (nurse.present)
            intent.putExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_OUT.ordinal());
        else
            intent.putExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_IN.ordinal());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPinLockView.resetPinLockView();
    }
}