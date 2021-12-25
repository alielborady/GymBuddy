package com.example.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChallengePage extends AppCompatActivity {

    private TextView workout;
    private TextView challengeProgress;
    private TextView maxPerDay;

    private Button updateReps;

    private EditText updateRepsEditText;

    private LinearProgressIndicator progressIndicator;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_page);

        workout = (TextView) findViewById(R.id.workout);
        challengeProgress = (TextView) findViewById(R.id.challengeProgress);
        maxPerDay = (TextView) findViewById(R.id.maxPerDay);
        progressIndicator = (LinearProgressIndicator) findViewById(R.id.progressIndicator);
        updateReps = (Button) findViewById(R.id.updateReps);
        updateRepsEditText = (EditText) findViewById(R.id.updateRepsEditText);

        Intent intent = getIntent();
        Challenge challenge = (Challenge) intent.getSerializableExtra("challenge");
        String challengeKey = (String) intent.getSerializableExtra("key");
//        int challengePosition = (Integer) intent.getSerializableExtra("position");

        int max = challenge.limit * 10 * challenge.getParticipants().size();

        progressIndicator.setMax(max);
        progressIndicator.setMin(0);
        progressIndicator.setProgress(challenge.getCurrentProgress());

        workout.setText(challenge.getName());
        String challengeProgressString = challenge.getCurrentProgress() + " / " + max;
        challengeProgress.setText(challengeProgressString);
        maxPerDay.setText("Your target is "+ String.valueOf(challenge.getLimit()) + " reps per day");

        updateReps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateString = updateRepsEditText.getText().toString().trim();

                if(updateString.isEmpty()){
                    updateRepsEditText.setError("Number of repetitions is required!!");
                    updateRepsEditText.requestFocus();
                    return;
                }

                Pattern pattern = Pattern.compile("^\\d+$");
                Matcher matcher = pattern.matcher(updateString);
                boolean matchFound = matcher.find();

                if (!matchFound) {
                    updateRepsEditText.setError("Number of repetitions must be a whole number");
                    updateRepsEditText.requestFocus();
                    return;
                }

                int update = Integer.parseInt(updateString);

                if(update <= 0){
                    updateRepsEditText.setError("Number of repetitions must be bigger than 0!!");
                    updateRepsEditText.requestFocus();
                    return;
                }

//                int doneDaily = challenge.getDoneDaily();
                int limit = challenge.getLimit();
                int currentProgress = challenge.getCurrentProgress();

                if (update + currentProgress < (limit * 10)){
//                    challenge.setDoneDaily(doneDaily + update);
                    challenge.setCurrentProgress(currentProgress + update);
                    updateData(challengeKey, challenge );

                } else {
                    Toast.makeText(ChallengePage.this, "You have already completed your part in this challenge", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updateData(String challengeKey, Challenge challenge) {

        HashMap challengeMap = new HashMap();
        challengeMap.put("currentProgress",challenge.getCurrentProgress());

        LocalDateTime now = LocalDateTime.now();
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        mDatabase = FirebaseDatabase.getInstance()
                .getReference("challenges")
                .child(challengeKey);
        
        if (nowDate.compareTo(challenge.getEndDate()) < 0){

            mDatabase.updateChildren(challengeMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()){
                        Toast.makeText(ChallengePage.this, "Nicely done!", Toast.LENGTH_LONG).show();
                        updateRepsEditText = (EditText) findViewById(R.id.updateRepsEditText);
                        updateRepsEditText.setText("");
                        startActivity(new Intent(ChallengePage.this, HomeActivity.class));
                    } else {
                        Toast.makeText(ChallengePage.this, "Try Again", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {

            mDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChallengePage.this, "Sorry, this challenge has already expired", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ChallengePage.this, HomeActivity.class));
                    }
                }
            });
        }

    }
}