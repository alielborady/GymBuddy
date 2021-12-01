package com.example.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllBuddys extends AppCompatActivity {

    public RecyclerView buddysRecView;

    private static final String TAG = "MyActivity";

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_buddys);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        String userEmail = user.getEmail();

        buddysRecView = (RecyclerView) findViewById(R.id.recycleView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        ArrayList<User> users = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        BuddyRecViewAdapter adapter = new BuddyRecViewAdapter();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (!(ds.getValue(User.class).getEmail().equals(userEmail))){
                        users.add(ds.getValue(User.class));
                    }
                }
                adapter.setUsers(users);

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        buddysRecView.setAdapter(adapter);
        buddysRecView.setLayoutManager(new LinearLayoutManager(this));
    }

}