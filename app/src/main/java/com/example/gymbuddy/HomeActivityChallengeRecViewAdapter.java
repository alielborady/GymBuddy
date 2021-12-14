package com.example.gymbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivityChallengeRecViewAdapter extends  RecyclerView.Adapter<HomeActivityChallengeRecViewAdapter.ViewHolder>{

    private ArrayList<Challenge> challenges = new ArrayList<>();
    private ArrayList<String> challengesKeys = new ArrayList<>();

    public HomeActivityChallengeRecViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Challenge challenge = challenges.get(position);

        holder.challengeName.setText(challenge.getName());
        holder.level.setText(challenge.getLevel());
        holder.participants.setText(String.valueOf(challenge.getParticipants().size()));

        String challengeKey = challengesKeys.get(position);

        Intent intent = new Intent(holder.context, ChallengePage.class);
        intent.putExtra("challenge", challenge);
        intent.putExtra("key",challengeKey);
        intent.putExtra("position", position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.context.startActivity(intent);
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
