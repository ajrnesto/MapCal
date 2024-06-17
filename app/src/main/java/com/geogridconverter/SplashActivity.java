package com.geogridconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class SplashActivity extends AppCompatActivity {

    FirebaseFirestore DB;
    FirebaseAuth AUTH;
    FirebaseUser USER;

    private void initializeFirebase() {
        DB = FirebaseFirestore.getInstance();
        AUTH = FirebaseAuth.getInstance();
        USER = AUTH.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initializeFirebase();

        ImageView imgLogo = findViewById(R.id.imgLogo);
        Animation animBounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        imgLogo.startAnimation(animBounce);

        animBounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkPreviousLoggedSession();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void checkPreviousLoggedSession() {
        if (USER != null) {
            // check status
            DB.collection("users").document(USER.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String status = task.getResult().getString("status");

                                if (status.equals("ACTIVE")) {
                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    finish();
                                }
                                else {
                                    startActivity(new Intent(SplashActivity.this, SuspendedActivity.class));
                                    finish();
                                }
                            }
                        }
                    });
        }
        else {
            startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));
            finish();
        }
    }
}