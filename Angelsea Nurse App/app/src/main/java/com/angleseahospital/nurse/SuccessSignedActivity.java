package com.angleseahospital.nurse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.angleseahospital.nurse.firestore.Nurse;
import com.angleseahospital.nurse.firestore.Shift;
import com.angleseahospital.nurse.firestore.Util;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.angleseahospital.nurse.MainActivity.*;

public class SuccessSignedActivity extends AppCompatActivity {
    private TextView textView_Name = null;

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

        if (status == SigningStatus.SIGNING_IN) {
            signIn(signingNurse).continueWith(new Continuation<Void, Object>() {
                @Override
                public Object then(@NonNull Task<Void> task) throws Exception {
                    if (!task.isSuccessful()) {
                        textView_Name.setText("Failed to sign in\n" + name);
                        Log.d(TAG, "Failed to sign in: " + task.getException());
                        //TODO: Add failure animation
                        return null;
                    }

                    Map<String, Object> data = new HashMap<>();
                    data.put("present", true);
                    data.put("lastSign", Shift.get24Time());
                    db.collection("nurses").document(signingNurse.id).update(data);

                    textView_Name.setText(name + "\nsigned in");
                    return null;
                }
            });
            //TODO: Display roster once signed in. Note: Nurse.Roster.build() to build roster before retrieving
        } else {
            signOut(signingNurse, null).continueWith(new Continuation<Void, Object>() {
                @Override
                public Object then(@NonNull Task<Void> task) throws Exception {
                    if (!task.isSuccessful()) {
                        if (status == SigningStatus.SIGNING_OUT)
                            textView_Name.setText("Sign out failed\n" + name);
                        else if (status == SigningStatus.SIGNING_OUT_EARLY)
                            textView_Name.setText("Early sign out failed\n" + name);
                        Log.d(TAG, "Failed to sign out: " + task.getException());
                        //TODO: Add failure animation
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
                }
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
        Log.d("SIGN IN", "time: " + time);
        Log.d("SIGN IN", "log: " + log);
        return db.collection(log).document("nurses").update(nurse.id + "." + time, true);
    }

    private Task<Void> signOut(Nurse nurse, String reason) {
        String log = Util.getTodayLogPath();
        log += "/signings";

        HashMap<String, Object> data = new HashMap<>();
        data.put("reason", reason);
        data.put("signedIn", false);

        String time = Shift.get24Time();
        Log.d("SIGN OUT", "time" + time);
        Log.d("SIGN OUT", "log: " + log);
        return db.collection(log).document("nurses").update(nurse.id + "." + time, data);
    }

    public void finish(View view){
        finish();
    }
}