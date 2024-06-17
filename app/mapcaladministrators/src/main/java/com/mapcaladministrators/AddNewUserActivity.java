package com.mapcaladministrators;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapcaladministrators.Utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class AddNewUserActivity extends AppCompatActivity {

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
    TextInputEditText etFirstName, etLastName, etMobile, etEmail, etPassword;
    MaterialButton btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        initializeFirebase();
        initializeViews();
        handleUserInteractions();
    }

    private void initializeViews() {
        clLogin = findViewById(R.id.clLogin);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void handleUserInteractions() {
        btnRegister.setOnClickListener(view -> {
            Utils.hideKeyboard(this);
            validateRegistrationForm();
        });
    }

    private void validateRegistrationForm() {
        if (etEmail.getText().toString().isEmpty() ||
                etPassword.getText().toString().isEmpty() ||
                etFirstName.getText().toString().isEmpty() ||
                etLastName.getText().toString().isEmpty() ||
                etMobile.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String mobile = etMobile.getText().toString();

        AUTH.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("firstName", firstName);
                        userInfo.put("lastName", lastName);
                        userInfo.put("mobile", mobile);
                        userInfo.put("email", email);
                        userInfo.put("password", password);
                        userInfo.put("status", "ACTIVE");
                        userInfo.put("userType", "USER");
                        userInfo.put("uid", AUTH.getUid());

                        DB.collection("users").document(AUTH.getUid())
                                .set(userInfo)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // sign out and log back in as admin
                                            AUTH.signOut();

                                            AUTH.signInWithEmailAndPassword(Utils.Cache.getString(getApplicationContext(), "admin_email"), Utils.Cache.getString(getApplicationContext(), "admin_password"))
                                                    .addOnCompleteListener(task2 -> {
                                                        if (task2.isSuccessful()) {
                                                            startActivity(new Intent(AddNewUserActivity.this, MainActivity.class));
                                                            finish();
                                                        }
                                                    });
                                        }
                                        else {
                                            Toast.makeText(AddNewUserActivity.this, "Registration error: "+task.getException(), Toast.LENGTH_SHORT).show();
                                            btnRegister.setEnabled(true);
                                        }
                                    }
                                });
                    }
                    else {
                        Utils.basicDialog(this, "Incorrect email or password.", "Try again");
                        btnRegister.setEnabled(true);
                    }
                });
    }
}