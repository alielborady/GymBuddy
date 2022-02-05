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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class AllChallenges extends AppCompatActivity {

    private ImageButton createChallenge;

    public RecyclerView challengeRecView;

    private ProgressBar progressBar;

//    public LocalDateTime nowLocal;

    private static final String TAG = "MyActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_challenges);

        createChallenge = (ImageButton) findViewById(R.id.createChallenge);
        createChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllChallenges.this, CreateChallenge.class));
            }
        });

        challengeRecView = (RecyclerView) findViewById(R.id.recycleView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("challenges");
        String userId = mAuth.getCurrentUser().getUid();

        ArrayList<Challenge> challenges = new ArrayList<>();
        ArrayList<String> challengesKeys = new ArrayList<>();

        AllChallengesRecViewAdapter adapter = new AllChallengesRecViewAdapter();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LocalDateTime now = LocalDateTime.now();
                Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

                for (DataSnapshot ds : snapshot.getChildren()){
                    if ((!ds.getValue(Challenge.class).getParticipants().contains(userId))
                            && (ds.getValue(Challenge.class).getParticipants().size() < 10)
                            && (ds.getValue(Challenge.class).getEndDate().compareTo(nowDate)>0)){
                        challenges.add(ds.getValue(Challenge.class));
                        String key = ds.getKey();
                        challengesKeys.add(key);
                    }
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
}