package com.example.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FypActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    private TextView userName;
    private TextView userAge;
    private TextView currentWeightInput;
    private TextView goalWeightInput;
    private TextView userGym;

    private EditText currentWeight;
    private EditText goalWeight;

    private Button currentWeightButton;
    private Button goalWeightButton;
    private Button editGym;

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

        currentWeight = (EditText) findViewById(R.id.currentWeightEditText);
        goalWeight = (EditText) findViewById(R.id.goalWeightEditText);

        currentWeightButton = (Button) findViewById(R.id.currentWeightButton);
        currentWeightButton.setOnClickListener(this);

        goalWeightButton = (Button) findViewById(R.id.goalWeightButton);
        goalWeightButton.setOnClickListener(this);

        editGym = (Button) findViewById(R.id.editGym);
        editGym.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.currentWeightButton:
                String currentWeightString = currentWeight.getText().toString().trim();

                if(currentWeightString.isEmpty()){
                    currentWeight.setError("Current weight is required!!");
                    currentWeight.requestFocus();
                    return;
                }

                double update = Double.parseDouble(currentWeightString);

                if(update <= 0){
                    currentWeight.setError("Number of repetitions must be bigger than 0!!");
                    currentWeight.requestFocus();
                    return;
                }

                updateData("currentWeight",update);
                return;

            case R.id.goalWeightButton:
                String goalWeightString = goalWeight.getText().toString().trim();

                if(goalWeightString.isEmpty()){
                    goalWeight.setError("Current weight is required!!");
                    goalWeight.requestFocus();
                    return;
                }

                double updateDouble = Double.parseDouble(goalWeightString);

                if(updateDouble <= 0){
                    goalWeight.setError("Number of repetitions must be bigger than 0!!");
                    goalWeight.requestFocus();
                    return;
                }

                updateData("goalWeight",updateDouble);
                return;

            case R.id.editGym:
                startActivity(new Intent(FypActivity.this, AllGyms.class));
        }
    }

    private void updateData(String key, double weight) {

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        HashMap userMap = new HashMap();
        userMap.put(key,weight);

        databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(FypActivity.this, "Personal info is updated", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(FypActivity.this, "Please try again", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}