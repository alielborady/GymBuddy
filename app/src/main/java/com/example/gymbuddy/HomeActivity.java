package com.example.gymbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton findABuddy;
    private ImageButton joinChallenge;

    public RecyclerView challengeRecView;

    public LocalDateTime now;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findABuddy = (ImageButton) findViewById(R.id.findBuddy);
        findABuddy.setOnClickListener(this);

        joinChallenge = (ImageButton) findViewById(R.id.joinChallenge);
        joinChallenge.setOnClickListener(this);

        challengeRecView = (RecyclerView) findViewById(R.id.recycleView);

        ArrayList<Challenge> challenges = new ArrayList<>();
        now = LocalDateTime.now();

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

        ChallengeRecViewAdapter adapter = new ChallengeRecViewAdapter();
        adapter.setChallenges(challenges);

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
}