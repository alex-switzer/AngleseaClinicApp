package com.example.angleseaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmationActivity extends AppCompatActivity {
    private Button yesCorrect;
    private Button noIncorrect;
    private TextView nameTextView;
    public static final String NAMEID = "NameID";
    private String profileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        nameTextView = findViewById(R.id.textView_welcome_name);
        Intent intent = getIntent();
        profileName = intent.getStringExtra(NAMEID);
        nameTextView.setText("Welcome\n" + profileName);

    }

    public void incorrect(View view) {
        finish();
    }

    public void correct(View view) {
        Intent intent = new Intent(ConfirmationActivity.this, SuccessSignedInActivity.class);
        intent.putExtra(NAMEID, profileName);
        startActivity(intent);
        finish();
    }
}