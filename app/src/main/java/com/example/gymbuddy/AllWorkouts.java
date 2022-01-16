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
    private DatabaseReference gettingUser;
    private DatabaseReference addWorkout;

    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_workouts);

        workoutsRecView = (RecyclerView) findViewById(R.id.recycleView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

//        addWorkout = FirebaseDatabase.getInstance().getReference("workouts");
//        Workout workout = new Workout("Stair Climber", "Cardio", "4 sets of 2 mins or 12 calories");
//        workout.setLink("https://www.youtube.com/watch?v=ST-5lD69XqU");
//        addWorkout.push().setValue(workout);

        mDatabase = FirebaseDatabase.getInstance().getReference("workouts");

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        gettingUser = FirebaseDatabase.getInstance().getReference("users").child(userId);
        gettingUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayList<Workout> workouts = new ArrayList<>();
        ArrayList<String> workoutsKeys = new ArrayList<>();

        AllWorkoutsRecViewAdapter adapter = new AllWorkoutsRecViewAdapter();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    workouts.add(ds.getValue(Workout.class));
                    workoutsKeys.add(ds.getKey());
                }

                ArrayList<Workout> orderedList = new ArrayList<>();
                ArrayList<String> orderedKeysList = new ArrayList<>();

                // sort based on rating
                for (double i = 5; i >= -1; i -= 0.1) {
                    for (int j = 0; j < workouts.size(); j++) {
                        if (workouts.get(j).getRating() >= i && workouts.get(j).getRating() < (i + 0.1)){
                            orderedList.add(workouts.get(j));
                            orderedKeysList.add(workoutsKeys.get(j));
                        }
                    }
                }

                ArrayList<Workout> filteredList = new ArrayList<>();
                ArrayList<String> filteredKeysList = new ArrayList<>();
                //sort based on weight goal
                if (!(currentUser.getCurrentWeight() == 0 && currentUser.getGoalWeight() == 0)){
                    if (currentUser.getGoalWeight() - currentUser.getCurrentWeight() > 0){
                        for (int i = 0; i < orderedList.size(); i++) {
                            // only add hypertrophy category
                            if (orderedList.get(i).getCategory().equalsIgnoreCase("Hypertrophy")){
                                filteredList.add(orderedList.get(i));
                                filteredKeysList.add(orderedKeysList.get(i));
                            }
                        }
                    } else if (currentUser.getGoalWeight() - currentUser.getCurrentWeight() < 0){
                        // only add cardio category
                        for (int i = 0; i < orderedList.size(); i++) {
                            // only add hypertrophy category
                            if (orderedList.get(i).getCategory().equalsIgnoreCase("Cardio")){
                                filteredList.add(orderedList.get(i));
                                filteredKeysList.add(orderedKeysList.get(i));
                            }
                        }
                    } else {
                        filteredList = orderedList;
                        filteredKeysList = orderedKeysList;
                    }
                }


                adapter.setWorkouts(filteredList);
                adapter.setKeys(filteredKeysList);
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