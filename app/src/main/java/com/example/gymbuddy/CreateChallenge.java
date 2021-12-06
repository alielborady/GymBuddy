package com.example.gymbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class CreateChallenge extends AppCompatActivity {

    private Spinner challengeSpinner;
    private Spinner levelSpinner;

    private ImageButton createChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);

        challengeSpinner = (Spinner) findViewById(R.id.challengeSpinner);
        levelSpinner = (Spinner) findViewById(R.id.levelSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> challengeAdapter = ArrayAdapter.createFromResource(this, R.array.challenges, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        challengeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        challengeSpinner.setAdapter(challengeAdapter);
        levelSpinner.setAdapter(levelAdapter);

        createChallenge = findViewById(R.id.createChallenge);
        createChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateChallenge.this, ChallengePage.class));
            }
        });


    }
}