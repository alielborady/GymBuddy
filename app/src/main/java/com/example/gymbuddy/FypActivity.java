package com.example.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FypActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference secondDatabaseReference;

    private TextView userName;
    private TextView userAge;
    private TextView currentWeightInput;
    private TextView goalWeightInput;
    private TextView userGym;
    private TextView userTime;

    private EditText currentWeight;
    private EditText goalWeight;

    private Button currentWeightButton;
    private Button goalWeightButton;
    private Button editGym;
    private Button editTime;

    private TimePickerDialog timePickerDialog;

    private Calendar calendar;
    private int currentHour;
    private int currentMinute;
    private String amPm;

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
        userTime = (TextView) findViewById(R.id.userTime);

        currentWeight = (EditText) findViewById(R.id.currentWeightEditText);
        goalWeight = (EditText) findViewById(R.id.goalWeightEditText);

        currentWeightButton = (Button) findViewById(R.id.currentWeightButton);
        currentWeightButton.setOnClickListener(this);

        goalWeightButton = (Button) findViewById(R.id.goalWeightButton);
        goalWeightButton.setOnClickListener(this);

        editGym = (Button) findViewById(R.id.editGym);
        editGym.setOnClickListener(this);

        editTime = (Button) findViewById(R.id.editTime);
        editTime.setOnClickListener(this);

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
                    userTime.setText("Time: Unavailable");
                } else {

                    if (snapshot.getValue(User.class).getWorkoutTime() == null){
                        userTime.setText("Time: Unavailable");
                    } else {
                        userTime.setText("Time: " + snapshot.getValue(User.class).getWorkoutTime());
                    }

                    String gymId = snapshot.getValue(User.class).getGymId();
                    secondDatabaseReference = FirebaseDatabase.getInstance().getReference("gyms").child(gymId);
                    secondDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userGym.setText("Gym: " + snapshot.getValue(Gym.class).getName());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w(TAG, "loadPost:onCancelled", error.toException());
                            userGym.setText("Unavailable");
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        finish();
//        startActivity(getIntent());
//    }

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
                return;

            case R.id.editTime:

                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(FypActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        if (i >= 12){
                            amPm = " PM";
                        } else {
                            amPm = " AM";
                        }
                        String update = String.format("%02d:%02d", i, i1) + amPm;
                        userTime.setText("Time:  " + update);
                        updateWorkoutTime(update);
                    }
                },currentHour,currentMinute,false);


                timePickerDialog.show();
                return;
        }
    }

    private void updateWorkoutTime(String update) {
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        HashMap userMap = new HashMap();
        userMap.put("workoutTime", update);

        databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(FypActivity.this, "Workout time is updated", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(FypActivity.this, "Please try again", Toast.LENGTH_LONG).show();
                }
            }
        });

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