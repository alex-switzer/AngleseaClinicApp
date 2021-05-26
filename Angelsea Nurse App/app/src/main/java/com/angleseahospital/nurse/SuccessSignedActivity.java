package com.angleseahospital.nurse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.angleseahospital.nurse.firestore.Nurse;

import static com.angleseahospital.nurse.MainActivity.*;

public class SuccessSignedActivity extends AppCompatActivity {
    private TextView textView_Name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_signed_in);

        Intent intent = getIntent();

        Nurse signingNurse = intent.getParcelableExtra(NURSE_OBJECT);
        String name = signingNurse.getFullName();

        SigningStatus status = SigningStatus.values()[intent.getIntExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_IN.ordinal())];

        textView_Name = findViewById(R.id.textViewSignedInName);
        switch (status) {
            case SIGNING_IN:
                textView_Name.setText(name + "\nsigned in");
                break;
            case SIGNING_OUT_EARLY:
                textView_Name.setText(name + "\nsigned out early");
                break;
            case SIGNING_OUT:
                textView_Name.setText(name + "\nsigned out");
                break;
        }

      /* ((LottieAnimationView) findViewById(R.id.animationSuccess)).addAnimatorListener(new Animator.AnimatorListener() {
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
        });*/
    }

    public void finish(View view){
        finish();
    }
}