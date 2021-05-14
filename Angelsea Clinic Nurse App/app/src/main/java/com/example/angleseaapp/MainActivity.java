package com.example.angleseaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "PinLockView";
    public static final String NAMEID = "NameID";

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "pin entered: " + pin);

            //TODO: Query database for nurse with given pin
            if (pin.equals("1234")) {
                Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                //TODO: Update presence for nurse with given pin
                //TODO: Add early sign out condition, + must give reason?
                String personName = "Robert";
                intent.putExtra(NAMEID, personName);
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
    };

    @Override
    protected void onResume() {
        super.onResume();
        mPinLockView.resetPinLockView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mPinLockView = findViewById(R.id.pin_lock_view);
        mIndicatorDots = findViewById(R.id.indicator_dots);
        mIndicatorDots.setBackgroundColor(getResources().getColor(R.color.grey));
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        mPinLockView.setPinLength(4);
        /*mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.black));*/

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }
}