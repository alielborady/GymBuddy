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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

public class AllBuddys extends AppCompatActivity {

    public RecyclerView buddysRecView;

    private static final String TAG = "MyActivity";

    private DatabaseReference mDatabase;
    private DatabaseReference gettingUser;

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_buddys);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        String userEmail = user.getEmail();

        buddysRecView = (RecyclerView) findViewById(R.id.recycleView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        ArrayList<User> users = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        BuddyRecViewAdapter adapter = new BuddyRecViewAdapter();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (!(ds.getValue(User.class).getEmail().equals(userEmail))){
                        users.add(ds.getValue(User.class));
                    }
                }

                ArrayList<User> orderedList = new ArrayList();

                for (User user : users) {
                    if (user.getGymId().equalsIgnoreCase("unavailable") && user.getWorkoutTime() == null) {
                        orderedList.add(0, user);
                    }
                }

                for (User user : users) {
                    if (((user.getWorkoutTime() == null) && (!user.getGymId().equalsIgnoreCase("unavailable")))
                            || ((user.getWorkoutTime() != null) && (user.getGymId().equalsIgnoreCase("unavailable")))) {
                        orderedList.add(0, user);
                    }
                }

                for (User user : users) {
                    if (!(user.getWorkoutTime() == null) && !(user.getGymId().equalsIgnoreCase("unavailable"))) {
                        orderedList.add(0, user);
                    }
                }

                String userId = mAuth.getCurrentUser().getUid();

                gettingUser = FirebaseDatabase.getInstance().getReference("users").child(userId);
                gettingUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<User> orderedList2 = new ArrayList();
                        currentUser = snapshot.getValue(User.class);

                        if (!(currentUser.getWorkoutTime() == null)){
                            LocalTime userWorkoutTime = LocalTime.parse(currentUser.getWorkoutTime()) ;
                            int userWorkoutHour = userWorkoutTime.getHour();
//                  int userWorkoutMin = userWorkoutTime.getMinute();

                            for (User user : orderedList){
                                if (!(user.getWorkoutTime() == null)){
                                    LocalTime buddyWorkoutTime = LocalTime.parse(user.getWorkoutTime()) ;
                                    int buddyWorkoutHour = buddyWorkoutTime.getHour();
//                          int buddyWorkoutMin = buddyWorkoutTime.getMinute();

                                    if ((Math.abs(userWorkoutHour - buddyWorkoutHour) <= 2) || (Math.abs(userWorkoutHour - buddyWorkoutHour) >= 22)){
                                        orderedList2.add(user);
                                    }
                                }
                            }

                            for (User user : orderedList){
                                if (!(user.getWorkoutTime() == null)){
                                    LocalTime buddyWorkoutTime = LocalTime.parse(user.getWorkoutTime()) ;
                                    int buddyWorkoutHour = buddyWorkoutTime.getHour();
//                          int buddyWorkoutMin = buddyWorkoutTime.getMinute();

                                    if ((Math.abs(userWorkoutHour - buddyWorkoutHour) > 2) && (Math.abs(userWorkoutHour - buddyWorkoutHour) <= 4)){
                                        orderedList2.add(user);
                                    }
                                }
                            }

                            for (User user : orderedList){
                                if (!(user.getWorkoutTime() == null)){
                                    LocalTime buddyWorkoutTime = LocalTime.parse(user.getWorkoutTime()) ;
                                    int buddyWorkoutHour = buddyWorkoutTime.getHour();
//                          int buddyWorkoutMin = buddyWorkoutTime.getMinute();

                                    if ((Math.abs(userWorkoutHour - buddyWorkoutHour) > 4) && (Math.abs(userWorkoutHour - buddyWorkoutHour) <= 6)){
                                        orderedList2.add(user);
                                    }
                                }
                            }
                            for (User user : orderedList){
                                if (!(user.getWorkoutTime() == null)){
                                    LocalTime buddyWorkoutTime = LocalTime.parse(user.getWorkoutTime()) ;
                                    int buddyWorkoutHour = buddyWorkoutTime.getHour();
//                          int buddyWorkoutMin = buddyWorkoutTime.getMinute();

                                    if ((Math.abs(userWorkoutHour - buddyWorkoutHour) > 6)){
                                        orderedList2.add(user);
                                    }
                                }
                            }

                            for (User user : orderedList){
                                if ((user.getWorkoutTime() == null)){
                                    orderedList2.add(user);
                                }
                            }

                        }
                        adapter.setUsers(orderedList2);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        buddysRecView.setAdapter(adapter);
        buddysRecView.setLayoutManager(new LinearLayoutManager(this));
    }

}