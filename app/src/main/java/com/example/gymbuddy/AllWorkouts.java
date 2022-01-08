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
//        Workout workout = new Workout("Leg Press", "Hypertrophy", "4 sets 8-10 reps");
//        addWorkout.push().setValue(workout);

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