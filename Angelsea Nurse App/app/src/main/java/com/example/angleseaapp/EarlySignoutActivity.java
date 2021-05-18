package com.example.angleseaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.angleseaapp.MainActivity.*;

public class EarlySignoutActivity extends AppCompatActivity {

    private Button btn_submitReason;
    private String reason;

    private String profileName;
    private SigningStatus status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_early_signout);

        Intent intent = getIntent();

        profileName = intent.getStringExtra(NAME_ID_EXTRA);
        status = SigningStatus.values()[intent.getIntExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_IN.ordinal())];

        btn_submitReason = findViewById(R.id.button_submitReason);
        EditText etxt_reason = findViewById(R.id.editText_reason);
        etxt_reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                reason = s.toString().trim();
                btn_submitReason.setEnabled(reason.length() > 1);
            }
        });
    }

    public void submitReason(View v) {
        //TODO: Submit the reason
        Intent intent = new Intent(EarlySignoutActivity.this, SuccessSignedActivity.class);

        intent.putExtra(NAME_ID_EXTRA, profileName);
        intent.putExtra(SIGNING_STATUS_EXTRA, status.ordinal());

        startActivity(intent);
        finish();
    }
}