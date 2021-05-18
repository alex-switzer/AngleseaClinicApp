package com.example.angleseaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import com.example.angleseaapp.firebase.*;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "PinLockView";
    public static final String NAME_ID_EXTRA = "NameID";
    public static final String SIGNING_STATUS_EXTRA = "SigningStatus";
    public static final int RC_SIGN_IN = 1;

    public enum SigningStatus {
        SIGNING_IN,
        SIGNING_OUT,
        SIGNING_OUT_EARLY
    }

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private NurseHelper nurseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        nurseHelper = new NurseHelper();

        mPinLockView = findViewById(R.id.pin_lock_view);
        mIndicatorDots = findViewById(R.id.indicator_dots);
        mIndicatorDots.setBackgroundColor(getColor(R.color.greyish));
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                Log.d(TAG, "pin entered: " + pin);

                boolean is_found = false;
                for (Nurse nurse : nurseHelper.getNurses().getValue()) {
                    if (pin.equals(nurse.getPin())) {
                        is_found = true;
                        Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                        //TODO: Update presence for nurse with given pin
                        //TODO: Add early sign out condition, + must give reason?
                        String personName = nurse.getName_first();
                        intent.putExtra(NAME_ID_EXTRA, personName);
                        intent.putExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_OUT.ordinal());
                        startActivity(intent);
                    }
                    /*
                    else if (pin.equals("4321")) {
                        Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                        String personName = "Ben";

                        intent.putExtra(NAME_ID_EXTRA, personName);
                        intent.putExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_OUT_EARLY.ordinal());
                        startActivity(intent);
                    }
                     */
                }
                if (!is_found) {
                    //TODO: Add failure to sign-in
                    mPinLockView.resetPinLockView();
                }
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
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(getColor(R.color.black));

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPinLockView.resetPinLockView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                //just logged in
            } else {
                //error logging in
            }
        }
    }

}