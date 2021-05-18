package com.example.angleseaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "PinLockView";
    public static final String NAME_ID_EXTRA = "NameID";
    public static final String SIGNING_STATUS_EXTRA = "SigningStatus";

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

        mPinLockView = findViewById(R.id.pin_lock_view);
        mIndicatorDots = findViewById(R.id.indicator_dots);
        mIndicatorDots.setBackgroundColor(getColor(R.color.greyish));
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                Log.d(TAG, "pin entered: " + pin);

                //TODO: Query database for nurse with given pin
                if (pin.equals("1234")) {
                    Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                    //TODO: Update presence for nurse with given pin
                    //TODO: Add early sign out condition, + must give reason?
                    String personName = "Robert";
                    intent.putExtra(NAME_ID_EXTRA, personName);
                    intent.putExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_IN.ordinal());
                    startActivity(intent);
                } else if (pin.equals("4321")) {
                    Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                    String personName = "Ben";

                    intent.putExtra(NAME_ID_EXTRA, personName);
                    intent.putExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_OUT_EARLY.ordinal());
                    startActivity(intent);
                } else {
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

}