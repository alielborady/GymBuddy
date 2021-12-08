package com.example.gymbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

public class ChallengeRecViewAdapter extends  RecyclerView.Adapter<ChallengeRecViewAdapter.ViewHolder>{

    private ArrayList<Challenge> challenges = new ArrayList<>();

    public ChallengeRecViewAdapter() {
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

        holder.challengeName.setText(challenges.get(position).getName());
        holder.level.setText(challenges.get(position).getLevel());
        holder.participants.setText(String.valueOf(challenges.get(position).getParticipants().size()));


    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public void setChallenges(ArrayList<Challenge> challenges) {
        this.challenges = challenges;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView challengeName;
        private TextView participants;
        private TextView level;
        private LinearProgressIndicator progressIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            challengeName = itemView.findViewById(R.id.challengeName);
            participants = itemView.findViewById(R.id.participants);
            level = itemView.findViewById(R.id.level);
            progressIndicator = itemView.findViewById(R.id.progressIndicator);
        }
    }

}
