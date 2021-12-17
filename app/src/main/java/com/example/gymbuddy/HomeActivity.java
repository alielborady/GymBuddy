package com.example.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton findABuddy;
    private ImageButton joinChallenge;

    private ProgressBar progressBar;

    public RecyclerView challengeRecView;

    public LocalDateTime now;

    private static final String TAG = "MyActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findABuddy = (ImageButton) findViewById(R.id.findBuddy);
        findABuddy.setOnClickListener(this);

        joinChallenge = (ImageButton) findViewById(R.id.joinChallenge);
        joinChallenge.setOnClickListener(this);

        challengeRecView = (RecyclerView) findViewById(R.id.recycleView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("challenges");

        String userId = mAuth.getCurrentUser().getUid();

        ArrayList<Challenge> challenges = new ArrayList<>();
        ArrayList<String> challengesKeys = new ArrayList<>();
        now = LocalDateTime.now();

        HomeActivityChallengeRecViewAdapter adapter = new HomeActivityChallengeRecViewAdapter();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (ds.getValue(Challenge.class).getParticipants().contains(userId)){
                        challenges.add(ds.getValue(Challenge.class));
                    }
                    String key = ds.getKey();
                    challengesKeys.add(key);
                }

                adapter.setChallenges(challenges);
                adapter.setKeys(challengesKeys);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });


        challengeRecView.setAdapter(adapter);
        challengeRecView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.findBuddy:
                startActivity(new Intent(HomeActivity.this, AllBuddys.class));
                break;

            case R.id.joinChallenge:
                startActivity(new Intent(HomeActivity.this, AllChallenges.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Challenge> challenges = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        ArrayList<String> challengesKeys = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("challenges");
        HomeActivityChallengeRecViewAdapter adapter = new HomeActivityChallengeRecViewAdapter();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if (ds.getValue(Challenge.class).getParticipants().contains(userId)){
                        challenges.add(ds.getValue(Challenge.class));
                    }
                    String key = ds.getKey();
                    challengesKeys.add(key);
                }

                adapter.setChallenges(challenges);
                adapter.setKeys(challengesKeys);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

    }
}