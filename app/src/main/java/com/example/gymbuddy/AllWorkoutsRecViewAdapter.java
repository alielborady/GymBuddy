package com.example.gymbuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllWorkoutsRecViewAdapter extends RecyclerView.Adapter<AllWorkoutsRecViewAdapter.ViewHolder>{

    public ArrayList<Workout> workouts = new ArrayList<>();

    private TextView helloWorld;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Workout workout = workouts.get(position);

        holder.workoutName.setText(workout.getName());
        holder.category.setText(workout.getCategory());
        holder.reps.setText(workout.getReps());

        holder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pop up window here
                holder.createNewRateDialog();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(workout.getLink()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                holder.context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public void setWorkouts(ArrayList<Workout> workouts){
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView workoutName;
        private TextView category;
        private TextView reps;

        private ImageButton rate;

        private Context context;

        private AlertDialog.Builder dialogBuilder;
        private AlertDialog dialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            workoutName = itemView.findViewById(R.id.workoutName);
            category = itemView.findViewById(R.id.category);
            reps = itemView.findViewById(R.id.reps);

            rate = itemView.findViewById(R.id.rate);

            context = itemView.getContext();

        }

        public void createNewRateDialog(){

            dialogBuilder = new AlertDialog.Builder(itemView.getContext());
            final View ratePopupView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.ratepopup,null);

            helloWorld = (TextView) ratePopupView.findViewById(R.id.helloWorld);

            dialogBuilder.setView(ratePopupView);
            dialog = dialogBuilder.create();
            dialog.show();

        }
    }

}
