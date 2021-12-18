package com.example.gymbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AllChallengesRecViewAdapter extends RecyclerView.Adapter<AllChallengesRecViewAdapter.ViewHolder>{

    private ArrayList<Challenge> challenges = new ArrayList<>();
    private ArrayList<String> challengesKeys = new ArrayList<>();

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Challenge challenge = challenges.get(position);

        int max = challenge.getLimit() * 10 * challenge.getParticipants().size();

        holder.challengeName.setText(challenge.getName());
        holder.level.setText(challenge.getLevel());
        holder.participants.setText(String.valueOf(challenge.getParticipants().size()));
        holder.progressIndicator.setMin(0);
        holder.progressIndicator.setMax(max);
        holder.progressIndicator.setProgress(challenge.getCurrentProgress());

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        String challengeKey = challengesKeys.get(position);
        Intent intent = new Intent(holder.context, HomeActivity.class);
//        intent.putExtra("challenge", challenge);
//        intent.putExtra("key",challengeKey);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challenge.getParticipants().add(userId);

                addParticipant(challengeKey, challenge, view, holder);

                holder.context.startActivity(intent);
            }
        });

    }

    private void addParticipant(String challengeKey, Challenge challenge, View view, ViewHolder holder) {

        HashMap challengeMap = new HashMap();
        challengeMap.put("participants", challenge.getParticipants());

        mDatabase = FirebaseDatabase.getInstance()
                .getReference("challenges")
                .child(challengeKey);


        mDatabase.updateChildren(challengeMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()){
                    Toast.makeText(view.getContext(), "You have joined the challenge", Toast.LENGTH_LONG).show();
                    holder.context.startActivity(new Intent(holder.context, HomeActivity.class));
                } else {
                    Toast.makeText(view.getContext(), "Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public void setChallenges(ArrayList<Challenge> challenges) {
        this.challenges = challenges;
        notifyDataSetChanged();
    }

    public void setKeys(ArrayList<String> challengesKeys) {
        this.challengesKeys = challengesKeys;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView challengeName;
        private TextView participants;
        private TextView level;
        private LinearProgressIndicator progressIndicator;

        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            challengeName = itemView.findViewById(R.id.challengeName);
            participants = itemView.findViewById(R.id.participants);
            level = itemView.findViewById(R.id.level);
            progressIndicator = itemView.findViewById(R.id.progressIndicator);

            context = itemView.getContext();

        }
    }

}
