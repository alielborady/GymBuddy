package com.example.gymbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton findABuddy;
    private ImageButton joinChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findABuddy = (ImageButton) findViewById(R.id.findBuddy);
        findABuddy.setOnClickListener(this);

        joinChallenge = (ImageButton) findViewById(R.id.joinChallenge);
        joinChallenge.setOnClickListener(this);

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