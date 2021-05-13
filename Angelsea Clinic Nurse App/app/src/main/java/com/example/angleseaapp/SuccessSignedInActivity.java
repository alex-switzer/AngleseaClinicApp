package com.example.angleseaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

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

        LottieAnimationView successAnimation = findViewById(R.id.animationSuccess);
        successAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

    public void finish(View view){
        finish();
    }
}