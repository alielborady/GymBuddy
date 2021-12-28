package com.example.gymbuddy;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.gymbuddy.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;
import java.util.Collection;

public class Chat extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityChatBinding binding;
    FirebaseListAdapter adapter;

    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);
        topAppBar.setTitle(getIntent().getStringExtra("secondUserName"));
        ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        String secondUserEmail = getIntent().getStringExtra("secondUserEmail");
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                if (input.getText().length() == 0) {
                    return;
                }

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            User user = postSnapshot.getValue(User.class);
                            if (user.getEmail().equals(currentUserEmail)) {
                                FirebaseDatabase.getInstance()
                                        .getReference("messages")
                                        .push()
                                        .setValue(
                                                new ChatMessage(
                                                        currentUserEmail,
                                                        secondUserEmail,
                                                        input.getText().toString(),
                                                        user.getFullName())
                                        );

                                // Clear the input
                                input.setText("");
                                listOfMessages.setSelection(adapter.getCount());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        Query query = FirebaseDatabase.getInstance().getReference("messages");
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setLayout(R.layout.message)
                .setQuery(query, ChatMessage.class)
                .build();
        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                if ((model.getFirstUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) || model.getSecondUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        && (model.getFirstUser().equals(secondUserEmail) || model.getSecondUser().equals(secondUserEmail))) {
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

                    if (model.getFirstUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        messageUser.setTextColor(Color.BLUE);
                        messageUser.setText("You");

                    } else {
                        messageUser.setTextColor(Color.GRAY);
                    }


                    listOfMessages.setSelection(adapter.getCount()-1);
                }
            }
        };
        listOfMessages.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}