package com.example.angleseaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.angleseaapp.MainActivity.*;

public class ConfirmationActivity extends AppCompatActivity {
    private Button yesCorrect;
    private Button noIncorrect;
    private TextView nameTextView;
    private String profileName = "";
    private MainActivity.SigningStatus status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        nameTextView = findViewById(R.id.textView_welcome_name);
        yesCorrect = findViewById(R.id.button_yesCorrect);

        Intent intent = getIntent();

        profileName = intent.getStringExtra(MainActivity.NAME_ID_EXTRA);
        status = SigningStatus.values()[intent.getIntExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_IN.ordinal())];

        nameTextView.setText("Welcome " + profileName);

        switch (status) {
            case SIGNING_IN:
                yesCorrect.setText("Yes, Sign Me In");
                break;
            case SIGNING_OUT_EARLY:
            case SIGNING_OUT:
                yesCorrect.setText("Yes, Sign Me Out");
                break;
        }
    }

    public void incorrect(View view) {
        finish();
    }

    public void correct(View view) {
        Intent intent;

        if (status == MainActivity.SigningStatus.SIGNING_OUT_EARLY) {
            intent = new Intent(ConfirmationActivity.this, EarlySignoutActivity.class);
        } else {
            intent = new Intent(ConfirmationActivity.this, SuccessSignedActivity.class);
        }

        intent.putExtra(MainActivity.NAME_ID_EXTRA, profileName);
        intent.putExtra(SIGNING_STATUS_EXTRA, status.ordinal());
        startActivity(intent);
        finish();
    }
}