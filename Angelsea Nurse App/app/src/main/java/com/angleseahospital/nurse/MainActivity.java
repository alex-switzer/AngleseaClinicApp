package com.angleseahospital.nurse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import com.angleseahospital.nurse.firestore.Nurse;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

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
                db.collection("nurses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                            //Loop through all nurses
                            for (QueryDocumentSnapshot nurse : task.getResult()) {
                                //Get nurses pin and compare to entered pin
                                String nursePin = (String) nurse.get("pin");
                                Log.d(TAG, "nurses pin: " + nursePin);
                                if (nursePin != null && nursePin.equals(pin)) {
                                    signQueriedNurse(new Nurse(nurse)); //Sign the nurse
                                    break;
                                }
                            }
                        else {
                            Log.d(TAG, "Failed to query the nurses collection: " + task.getException());
                            //TODO: Collection 'Nurses' could not be queried. Throw error message?
                        }
                        mPinLockView.resetPinLockView();
                    }
                });
            }

            //Opens the ConfirmationActivity with the given nurse
            public void signQueriedNurse(Nurse nurse) {
                Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                intent.putExtra(NURSE_OBJECT, nurse);
                if (nurse.present)
                    intent.putExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_OUT);
                else
                    intent.putExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_IN);
                startActivity(intent);
            }

            @Override
            public void onEmpty() {
                Log.d(TAG, "Pin empty");
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPinLockView.resetPinLockView();
    }
}