package com.example.gymbuddy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AllGymsRecViewAdapter extends RecyclerView.Adapter<AllGymsRecViewAdapter.ViewHolder>{

    public ArrayList<Gym> gyms = new ArrayList<>();

    private static final String TAG = "MyActivity";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gym_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Gym gym = gyms.get(position);

        holder.mAuth = FirebaseAuth.getInstance();
        String userId = holder.mAuth.getCurrentUser().getUid();

        holder.gymName.setText(gym.getName());
        holder.gymAddress.setText(gym.getAddress());

        holder.userDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        holder.updateGymDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        holder.gymDatabaseReference = FirebaseDatabase.getInstance().getReference("gyms");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.gymDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            if (ds.getValue(Gym.class).getName().equalsIgnoreCase(gym.getName())){

                                HashMap userMap = new HashMap();
                                userMap.put("gymId", ds.getKey());

                                holder.updateGymDatabaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(holder.context, "The gym is added as your main one", Toast.LENGTH_SHORT).show();
                                            holder.context.startActivity(new Intent(holder.context, FypActivity.class));
                                        } else {
                                            Toast.makeText(holder.context, "Please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return gyms.size();
    }

    public void setGyms(ArrayList<Gym> gyms){
        this.gyms = gyms;
        notifyDataSetChanged();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        private TextView gymName;
        private TextView gymAddress;

        private FirebaseAuth mAuth;
        private DatabaseReference userDatabaseReference;
        private DatabaseReference gymDatabaseReference;
        private DatabaseReference updateGymDatabaseReference;

        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gymName = itemView.findViewById(R.id.gymName);
            gymAddress = itemView.findViewById(R.id.gymAddress);

            context = itemView.getContext();
        }
    }

}
