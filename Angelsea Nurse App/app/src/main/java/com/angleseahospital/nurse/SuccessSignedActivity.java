package com.angleseahospital.nurse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.Cancellable;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieListener;
import com.angleseahospital.nurse.firestore.Nurse;
import com.angleseahospital.nurse.firestore.Shift;
import com.angleseahospital.nurse.classes.Util;
import com.angleseahospital.nurse.roster.RosterView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.internal.http.CacheStrategy;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.angleseahospital.nurse.MainActivity.*;

public class SuccessSignedActivity extends AppCompatActivity {
    private TextView textView_Name = null;
    private RosterView rosterView;

    private boolean successful = false;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_signed_in);


        Intent intent = getIntent();

        Nurse signingNurse = intent.getParcelableExtra(NURSE_OBJECT);
        SigningStatus status = SigningStatus.values()[intent.getIntExtra(SIGNING_STATUS_EXTRA, SigningStatus.SIGNING_IN.ordinal())];

        String name = signingNurse.getFullName();
        textView_Name = findViewById(R.id.textViewSignedInName);

        rosterView = findViewById(R.id.roster_view);

        if (status == SigningStatus.SIGNING_IN) {
            signIn(signingNurse).continueWith(task -> {
                if (!task.isSuccessful()) {
                    textView_Name.setText("Failed to sign in\n" + name);
                    Log.d(TAG, "Failed to sign in: " + task.getException());
                    //TODO: Add failure animation
                    Toasty.error(SuccessSignedActivity.this, "Error signing in. Please try again!", Toast.LENGTH_LONG, true).show();

                    LottieAnimationView animationSuccess = findViewById(R.id.animationSuccess);
                    LottieCompositionFactory.fromRawRes(SuccessSignedActivity.this, R.raw.animationfailed).addListener(new LottieListener<LottieComposition>() {
                        @Override
                        public void onResult(LottieComposition result) {
                            animationSuccess.setComposition(result);
                            animationSuccess.playAnimation();
                        }
                    });

                    return null;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("present", true);
                data.put("lastSign", Shift.get24Time());
                db.collection("nurses").document(signingNurse.id).update(data);
                textView_Name.setText(name + "\n signed in");

                if (!signingNurse.roster.isBuilt()) {
                    Log.d(TAG, "Building roster for display: " + signingNurse.roster.reference);
                    signingNurse.roster.build(task2 -> {
                        rosterView.setVisibility(View.VISIBLE);
                        rosterView.construct(signingNurse.roster);
                        Log.d(TAG, "Finished displaying roster");
                    });
                }
                else {
                    Log.d(TAG, "Displaying already built roster...");
                    rosterView.setVisibility(View.VISIBLE);
                    rosterView.displayRoster(signingNurse.roster);
                    Log.d(TAG, "Finished displaying roster");
                }
                return null;
            });
        } else {
            signOut(signingNurse, null).continueWith(task -> {
                if (!task.isSuccessful()) {
                    if (status == SigningStatus.SIGNING_OUT)
                        textView_Name.setText("Sign out failed\n" + name);
                    else if (status == SigningStatus.SIGNING_OUT_EARLY)
                        textView_Name.setText("Early sign out failed\n" + name);
                    Log.d(TAG, "Failed to sign out: " + task.getException());
                    //TODO: Add failure animation
                    Toasty.error(SuccessSignedActivity.this, "Error signing out. Please try again!", Toast.LENGTH_LONG, true).show();

                    LottieAnimationView animationSuccess = findViewById(R.id.animationSuccess);
                    LottieCompositionFactory.fromRawRes(SuccessSignedActivity.this, R.raw.animationfailed).addListener(new LottieListener<LottieComposition>() {
                        @Override
                        public void onResult(LottieComposition result) {
                            animationSuccess.setComposition(result);
                            animationSuccess.playAnimation();
                        }
                    });

                    return null;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("present", false);
                data.put("lastSign", Shift.get24Time());
                db.collection("nurses").document(signingNurse.id).update(data);

                if (status == SigningStatus.SIGNING_OUT)
                    textView_Name.setText(name + "\nsigned out");
                else if (status == SigningStatus.SIGNING_OUT_EARLY)
                    textView_Name.setText(name + "\nsigned out early");
                return null;
            });
        }
        /*((LottieAnimationView) findViewById(R.id.animationSuccess)).addAnimatorListener(new Animator.AnimatorListener() {
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

    private Task<Void> signIn(Nurse nurse) {
        String log = Util.getTodayLogPath();
        log += "/signings";

        String time = Shift.get24Time();
        return db.collection(log).document("nurses").update(nurse.id + "." + time, true);
    }

    private Task<Void> signOut(Nurse nurse, String reason) {
        String log = Util.getTodayLogPath();
        log += "/signings";

        HashMap<String, Object> data = new HashMap<>();
        data.put("reason", reason);
        data.put("signedIn", false);

        String time = Shift.get24Time();
        return db.collection(log).document("nurses").update(nurse.id + "." + time, data);
    }

    public void finish(View view){
        finish();
    }
}