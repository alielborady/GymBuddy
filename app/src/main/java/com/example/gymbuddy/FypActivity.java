package com.example.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FypActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    private TextView userName;
    private TextView userAge;
    private TextView currentWeightInput;
    private TextView goalWeightInput;
    private TextView userGym;

    private static final String TAG = "FYPActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fyp);

        userName = (TextView) findViewById(R.id.userName);
        userAge = (TextView) findViewById(R.id.userAge);
        currentWeightInput = (TextView) findViewById(R.id.currentWeightInput);
        goalWeightInput = (TextView) findViewById(R.id.goalWeightInput);
        userGym = (TextView) findViewById(R.id.userGym);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        User user;

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName.setText(snapshot.getValue(User.class).getFullName());
                userAge.setText("Age: " + snapshot.getValue(User.class).getAge());
                currentWeightInput.setText(String.valueOf(snapshot.getValue(User.class).getCurrentWeight()));
                goalWeightInput.setText(String.valueOf(snapshot.getValue(User.class).getGoalWeight()));
                if ((snapshot.getValue(User.class).getGymId()).equalsIgnoreCase("Unavailable")){
                    userGym.setText("Gym: Unavailable");
                } else {
                    String gymId = snapshot.getValue(User.class).getGymId();
                    getGymName(gymId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }

    private void getGymName(String gymId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("gyms");
    }
}