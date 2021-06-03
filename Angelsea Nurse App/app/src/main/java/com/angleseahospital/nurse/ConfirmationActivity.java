package com.angleseahospital.nurse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.angleseahospital.nurse.firestore.Nurse;

import static com.angleseahospital.nurse.MainActivity.*;

public class ConfirmationActivity extends AppCompatActivity {
    private Button yesCorrect;
    private Button noIncorrect;
    private TextView nameTextView;

    private String profileName = "";
    private SigningStatus status;
    private Nurse signingNurse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        nameTextView = findViewById(R.id.textView_welcome_name);
        yesCorrect = findViewById(R.id.button_yesCorrect);

        Intent intent = getIntent();

        signingNurse = intent.getParcelableExtra(NURSE_OBJECT);
        profileName = signingNurse.getFullName();
        status = SigningStatus.values()[intent.getIntExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_IN.ordinal())];

        if (status == SigningStatus.SIGNING_OUT) {
            if (signingNurse.roster.earlySignout())
                status = SigningStatus.SIGNING_OUT_EARLY;
            signingNurse.roster.completeShift();
        }

        nameTextView.setText("Welcome " + profileName);

        switch (status) {
            case SIGNING_IN:
                yesCorrect.setText("Yes, Sign Me In");
                break;

            case SIGNING_OUT:
            case SIGNING_OUT_EARLY:
                yesCorrect.setText("Yes, Sign Me Out");
                break;
        }
    }

    public void incorrect(View view) {
        finish();
    }

    public void correct(View view) {
        Intent intent;
        switch (status) {
            case SIGNING_OUT_EARLY:
                intent = new Intent(ConfirmationActivity.this, EarlySignoutActivity.class);
                break;
            case SIGNING_OUT:
            case SIGNING_IN:
            default:
                intent = new Intent(ConfirmationActivity.this, SuccessSignedActivity.class);
        }
        intent.putExtra(NURSE_OBJECT, signingNurse);
        intent.putExtra(SIGNING_STATUS_EXTRA, status.ordinal());
        startActivity(intent);
        finish();
    }
}