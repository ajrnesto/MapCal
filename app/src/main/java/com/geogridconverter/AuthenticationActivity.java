package com.geogridconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.geogridconverter.Utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthenticationActivity extends AppCompatActivity {

    FirebaseFirestore DB;
    FirebaseAuth AUTH;
    FirebaseUser USER;

    private void initializeFirebase() {
        DB = FirebaseFirestore.getInstance();
        AUTH = FirebaseAuth.getInstance();
        USER = AUTH.getCurrentUser();
    }

    // authentication views
    ConstraintLayout clLogin;
    TextInputEditText etLoginEmail, etLoginPassword;
    MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        initializeFirebase();
        checkPreviousLoggedSession();
        initializeViews();
        handleUserInteractions();
    }

    private void initializeViews() {
        clLogin = findViewById(R.id.clLogin);
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void checkPreviousLoggedSession() {
        if (USER != null) {
            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
            finish();
        }
    }

    private void handleUserInteractions() {
        btnLogin.setOnClickListener(view -> {
            Utils.hideKeyboard(this);
            validateAuthenticationForm();
        });
    }

    private void validateAuthenticationForm() {
        if (etLoginEmail.getText().toString().isEmpty() ||
                etLoginPassword.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);

        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        AUTH.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AuthenticationActivity.this, "Signed in as "+email, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                        finish();
                    }
                    else {
                        Utils.basicDialog(this, "Incorrect email or password.", "Try again");
                        btnLogin.setEnabled(true);
                    }
                });
    }
}