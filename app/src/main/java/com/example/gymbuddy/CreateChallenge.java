package com.example.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateChallenge extends AppCompatActivity {

    private Spinner challengeSpinner;
    private Spinner levelSpinner;

    private ImageButton createChallenge;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference("challenges");

        createChallenge = findViewById(R.id.createChallenge);
        createChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String name = challengeSpinner.getSelectedItem().toString();
                String level = levelSpinner.getSelectedItem().toString();

                LocalDateTime now = LocalDateTime.now();
                Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

                Challenge challenge = new Challenge(name, level,nowDate);
                challenge.addParticipant(currentUserId);

                mDatabase.push().setValue(challenge)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(CreateChallenge.this, "Challenge is created", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(CreateChallenge.this, HomeActivity.class));

                                } else {
                                    Toast.makeText(CreateChallenge.this, "Please try again", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


            }
        });


    }
}