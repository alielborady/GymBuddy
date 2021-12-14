package com.example.gymbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class AllChallenges extends AppCompatActivity {

    private ImageButton createChallenge;

    public RecyclerView challengeRecView;

    public LocalDateTime nowLocal;

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

        ArrayList<Challenge> challenges = new ArrayList<>();
        nowLocal = LocalDateTime.now();
        Date now = Date.from(nowLocal.atZone(ZoneId.systemDefault()).toInstant());

        challenges.add(new Challenge("Squats", "Easy", now ));
        challenges.add(new Challenge("Push Ups", "medium", now ));
        challenges.add(new Challenge("Pull Ups", "hard", now ));
        challenges.add(new Challenge("Squats", "Easy", now ));
        challenges.add(new Challenge("Push Ups", "medium", now ));
        challenges.add(new Challenge("Pull Ups", "hard", now ));
        challenges.add(new Challenge("Squats", "Easy", now ));
        challenges.add(new Challenge("Push Ups", "medium", now ));
        challenges.add(new Challenge("Pull Ups", "hard", now ));
        challenges.add(new Challenge("Squats", "Easy", now ));
        challenges.add(new Challenge("Push Ups", "medium", now ));
        challenges.add(new Challenge("Pull Ups", "hard", now ));

        HomeActivityChallengeRecViewAdapter adapter = new HomeActivityChallengeRecViewAdapter();
        adapter.setChallenges(challenges);

        challengeRecView.setAdapter(adapter);
        challengeRecView.setLayoutManager(new LinearLayoutManager(this));


    }
}