package com.example.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

public class CreateGym extends AppCompatActivity {

    private EditText gymName;
    private EditText gymAddress;

    private ImageButton createGym;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference secondDatabaseReference;
    private DatabaseReference userDatabaseReference;

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gym);

        mDatabase = FirebaseDatabase.getInstance().getReference("gyms");
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        gymName = (EditText) findViewById(R.id.gymName);
        gymAddress = (EditText) findViewById(R.id.gymAddress);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        createGym = (ImageButton) findViewById(R.id.createGym);
        createGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                String name = gymName.getText().toString().trim();
                String address = gymAddress.getText().toString().trim();

                if(name.isEmpty()){
                    gymName.setError("Name is required!!");
                    gymName.requestFocus();
                    return;
                }

                if(address.isEmpty()){
                    gymAddress.setError("Address is required!!");
                    gymAddress.requestFocus();
                    return;
                }

                Gym gym = new Gym(name, address);

                mDatabase.push().setValue(gym)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    // add as main gym

                                    secondDatabaseReference = FirebaseDatabase.getInstance().getReference("gyms");
                                    secondDatabaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot ds : snapshot.getChildren()){
                                                if (ds.getValue(Gym.class).getName().equalsIgnoreCase(name)){
                                                    String gymKey = ds.getKey();

                                                    userDatabaseReference = FirebaseDatabase
                                                            .getInstance()
                                                            .getReference("users")
                                                            .child(userId);

                                                    HashMap userMap = new HashMap();
                                                    userMap.put("gymId",gymKey);

                                                    userDatabaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(CreateGym.this, "Gym is created and added as your main gym", Toast.LENGTH_LONG).show();
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                startActivity(new Intent(CreateGym.this, FypActivity.class));
                                                            } else {
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                Toast.makeText(CreateGym.this, "Please try again", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.w(TAG, "loadPost:onCancelled", error.toException());
                                        }
                                    });
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(CreateGym.this, "Please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}