package com.example.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllGyms extends AppCompatActivity {

    private ImageButton createGym;

    public RecyclerView gymRecView;

    private ProgressBar progressBar;

    private static final String TAG = "MyActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_gyms);

        createGym = (ImageButton) findViewById(R.id.createGym);
        createGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllGyms.this, CreateGym.class));
            }
        });

        gymRecView = (RecyclerView) findViewById(R.id.recycleView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mDatabase = FirebaseDatabase.getInstance().getReference("gyms");

        ArrayList<Gym> gyms = new ArrayList<>();

        AllGymsRecViewAdapter adapter = new AllGymsRecViewAdapter();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    gyms.add(ds.getValue(Gym.class));
                }

                adapter.setGyms(gyms);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

        gymRecView.setAdapter(adapter);
        gymRecView.setLayoutManager(new LinearLayoutManager(this));
    }
}