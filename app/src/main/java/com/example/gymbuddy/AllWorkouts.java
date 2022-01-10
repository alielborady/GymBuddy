package com.example.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AllWorkouts extends AppCompatActivity {

    private ProgressBar progressBar;

    public RecyclerView workoutsRecView;

    private static final String TAG = "MyActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference addWorkout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_workouts);

        workoutsRecView = (RecyclerView) findViewById(R.id.recycleView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

//        addWorkout = FirebaseDatabase.getInstance().getReference("workouts");
//        Workout workout = new Workout("Barbell Bicep Curls", "Hypertrophy", "3 sets 10-12 reps");
//        workout.setLink("https://www.youtube.com/watch?v=dDI8ClxRS04");
//        Workout workout2 = new Workout("Hammer Curls", "Hypertrophy", "3 sets 10-12 reps");
//        workout2.setLink("https://www.youtube.com/watch?v=0IAM2YtviQY");
//        Workout workout3 = new Workout("Preacher Curls", "Hypertrophy", "3 sets 10-15 reps");
//        workout3.setLink("https://www.youtube.com/watch?v=RgN216Cumtw");
//        Workout workout4 = new Workout("Overhead Triceps Extensions", "Hypertrophy", "3 sets 10-12 reps");
//        workout4.setLink("https://www.youtube.com/watch?v=ntBjdnckWgo");
//        Workout workout5 = new Workout("Skull Crushers", "Hypertrophy", "4 sets 8-10 reps");
//        workout5.setLink("https://www.youtube.com/watch?v=4re6CJ0XNF8");
//        Workout workout6 = new Workout("Triceps Push-downs", "Hypertrophy", "3 sets 10-15 reps");
//        workout6.setLink("https://www.youtube.com/watch?v=HIKzvHkibWc");
//        addWorkout.push().setValue(workout);
//        addWorkout.push().setValue(workout2);
//        addWorkout.push().setValue(workout3);
//        addWorkout.push().setValue(workout4);
//        addWorkout.push().setValue(workout5);
//        addWorkout.push().setValue(workout6);

        mDatabase = FirebaseDatabase.getInstance().getReference("workouts");

        ArrayList<Workout> workouts = new ArrayList<>();

        AllWorkoutsRecViewAdapter adapter = new AllWorkoutsRecViewAdapter();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    workouts.add(ds.getValue(Workout.class));
                }

                adapter.setWorkouts(workouts);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

        workoutsRecView.setAdapter(adapter);
        workoutsRecView.setLayoutManager(new LinearLayoutManager(this));

    }
}