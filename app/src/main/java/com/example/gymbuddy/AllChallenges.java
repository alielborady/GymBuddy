package com.example.gymbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AllChallenges extends AppCompatActivity {

    private ImageButton createChallenge;

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


    }
}