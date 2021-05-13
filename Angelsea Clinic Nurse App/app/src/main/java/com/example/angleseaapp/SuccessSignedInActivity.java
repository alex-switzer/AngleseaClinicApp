package com.example.angleseaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SuccessSignedInActivity extends AppCompatActivity {
    public static final String NAMEID = "NameID";
    private TextView textViewName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_signed_in);

        Intent intent = getIntent();
        String name = intent.getStringExtra(NAMEID);
        textViewName = findViewById(R.id.textViewSignedInName);
        textViewName.setText(name + " signed in");

    }
    public void Finish(View view){
        startActivity(new Intent(SuccessSignedInActivity.this, MainActivity.class));
    }
}