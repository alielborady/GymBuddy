package com.example.gymbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegister extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private Button registerButton;
    private EditText registerName, registerAge, registerEmail, registerPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        mAuth = FirebaseAuth.getInstance();

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        registerName = (EditText) findViewById(R.id.registerName);
        registerAge = (EditText) findViewById(R.id.registerAge);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPassword = (EditText) findViewById(R.id.registerPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerButton:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String name = registerName.getText().toString().trim();
        String age = registerAge.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();

        if(name.isEmpty()){
            registerName.setError("Name is required!!");
            registerName.requestFocus();
            return;
        }

        if(age.isEmpty()){
            registerAge.setError("Age is required!!");
            registerAge.requestFocus();
            return;
        }

        if(email.isEmpty()){
            registerEmail.setError("Email is required!!");
            registerEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registerEmail.setError("Please provide a valid Email!");
            registerEmail.requestFocus();
        }

        if(password.isEmpty()){
            registerPassword.setError("Password is required!!");
            registerPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            registerPassword.setError("Password Must be longer than 6 characters!!");
            registerPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(name,age,email);
                            user.setGymId("Unavailable");
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                user.sendEmailVerification();

                                                Toast.makeText(UserRegister.this, "User has been registered, a verification email has been sent.", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                //redirect to login page
                                                startActivity(new Intent(UserRegister.this, MainActivity.class));
                                            }else {
                                                Toast.makeText(UserRegister.this, "Failed", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(UserRegister.this, "Failed", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}