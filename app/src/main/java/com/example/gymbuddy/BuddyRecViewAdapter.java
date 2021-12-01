package com.example.gymbuddy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BuddyRecViewAdapter extends RecyclerView.Adapter<BuddyRecViewAdapter.ViewHolder> {

    private ArrayList<User> users = new ArrayList<>();

    private static final String TAG = "MyActivity";


    public BuddyRecViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy_items, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String gymId = users.get(position).getGymId();
        holder.mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = holder.mAuth.getCurrentUser();

        holder.name.setText(users.get(position).getFullName());

        if (gymId.equalsIgnoreCase("Unavailable")){

            holder.gymName.setText("Unavailable");
            holder.strDate = "Unavailable";

        } else {

            holder.mDatabase = FirebaseDatabase.getInstance().getReference("gyms").child(gymId);

            holder.mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    holder.gymNameText = snapshot.getValue(Gym.class).getName();
                    holder.gymName.setText(holder.gymNameText);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

                }
            });

//            holder.strDate = "Unavailable";
//
//            Date date = users.get(position).getWorkoutTime();
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//            holder.strDate = dateFormat.format(date);
//
//            holder.workoutTime.setText(holder.strDate);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public FirebaseAuth mAuth;

        public DatabaseReference mDatabase;

        public TextView name, gymName, workoutTime;

        public Button buddyCard;

        public String gymNameText, strDate;

        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.userName);
            gymName = itemView.findViewById(R.id.gymName);
            workoutTime = itemView.findViewById(R.id.workoutTime);

            context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // go to chat view/class
                    context.startActivity(new Intent(context, HomeActivity.class));
                }
            });

        }
    }
}
