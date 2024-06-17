package com.mapcaladministrators;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapcaladministrators.Adapters.UsersAdapter;
import com.mapcaladministrators.Objects.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements UsersAdapter.OnUsersListener {

    FirebaseFirestore DB;
    FirebaseAuth AUTH;
    FirebaseUser USER;

    private void initializeFirebase() {
        DB = FirebaseFirestore.getInstance();
        AUTH = FirebaseAuth.getInstance();
        USER = AUTH.getCurrentUser();
    }
    TabLayout tlUsers;
    ExtendedFloatingActionButton btnReportUser;

    ArrayList<User> arrUsers;
    UsersAdapter incidentsAdapter;
    UsersAdapter.OnUsersListener onUsersListener = this;

    RecyclerView rvUsers;
    MaterialButton btnLogout;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFirebase();
        initializeViews();
        loadRecyclerView();
        handleUserInteraction();
    }

    private void handleUserInteraction() {
        btnLogout.setOnClickListener(view -> {
            AUTH.signOut();
            startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
            finish();
        });

        btnAdd.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddNewUserActivity.class));
        });
    }

    private void initializeViews() {
        rvUsers = findViewById(R.id.rvUsers);
        btnLogout = findViewById(R.id.btnLogout);
        btnAdd = findViewById(R.id.btnAdd);
    }

    private void loadRecyclerView() {
        arrUsers = new ArrayList<>();
        rvUsers = findViewById(R.id.rvUsers);
        rvUsers.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rvUsers.setLayoutManager(linearLayoutManager);

        Query qryUsers = DB.collection("users").whereEqualTo("userType", "USER");

        qryUsers.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                arrUsers.clear();

                if (queryDocumentSnapshots == null) {
                    return;
                }

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    User user = documentSnapshot.toObject(User.class);

                    arrUsers.add(user);
                    incidentsAdapter.notifyDataSetChanged();
                }

                if (arrUsers.isEmpty()) {
                    rvUsers.setVisibility(View.GONE);
                }
                else {
                    rvUsers.setVisibility(View.VISIBLE);
                }
            }
        });

        incidentsAdapter = new UsersAdapter(this, arrUsers, onUsersListener);
        rvUsers.setAdapter(incidentsAdapter);
        incidentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUsersClick(int position) {
        User user = arrUsers.get(position);
        Map<String, Object> updateData = new HashMap<>();

        if (user.getStatus().equals("ACTIVE")) {
            updateData.put("status", "SUSPENDED");
            DB.collection("users").document(user.getUid()).update(updateData);
        }
        else {
            updateData.put("status", "ACTIVE");
            DB.collection("users").document(user.getUid()).update(updateData);
        }
    }
}