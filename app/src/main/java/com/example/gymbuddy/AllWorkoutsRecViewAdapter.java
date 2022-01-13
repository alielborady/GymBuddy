package com.example.gymbuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AllWorkoutsRecViewAdapter extends RecyclerView.Adapter<AllWorkoutsRecViewAdapter.ViewHolder>{

    public ArrayList<Workout> workouts = new ArrayList<>();
    private ArrayList<String> workoutsKeys = new ArrayList<>();

    private TextView helloWorld;
    private RatingBar ratingBar;
    private Button rateButton;

    DatabaseReference databaseReference;

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
        String workoutKey = workoutsKeys.get(position);

        holder.workoutName.setText(workout.getName());
        holder.category.setText(workout.getCategory());
        holder.reps.setText(workout.getReps());

        holder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pop up window here
                holder.createNewRateDialog(workoutKey,workout);
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

//        holder.rateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(holder.context, "Yes you are right", Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public void setWorkouts(ArrayList<Workout> workouts){
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    public void setKeys(ArrayList<String> keys){
        this.workoutsKeys = keys;
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

        public void createNewRateDialog(String workoutKey,Workout workout){

            dialogBuilder = new AlertDialog.Builder(itemView.getContext());
            final View ratePopupView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.ratepopup,null);

            helloWorld = (TextView) ratePopupView.findViewById(R.id.helloWorld);
            ratingBar = (RatingBar) ratePopupView.findViewById(R.id.rating);

            rateButton = (Button) ratePopupView.findViewById(R.id.rateNow);

            dialogBuilder.setView(ratePopupView);
            dialog = dialogBuilder.create();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            rateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    double newRating = ratingBar.getRating();

                    databaseReference = FirebaseDatabase.getInstance().getReference("workouts").child(workoutKey);

                    HashMap workoutMap = new HashMap();
                    workoutMap.put("rating", workout.getNewRating(newRating));
                    workoutMap.put("numOfRaters", workout.getNumOfRaters()+1);

                    databaseReference.updateChildren(workoutMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Toast.makeText(itemView.getContext(), "Thanks for rating!", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                                Intent intent = new Intent(itemView.getContext(),AllWorkouts.class);
                                itemView.getContext().startActivity(intent);
                            } else {
                                Toast.makeText(itemView.getContext(), "Please try again!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            });

        }
    }

}
