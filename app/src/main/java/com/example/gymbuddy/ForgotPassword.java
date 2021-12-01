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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText email;
    private Button reset;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.forgotPasswordEmail);

        reset = (Button) findViewById(R.id.resetPasswordButton);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.bringToFront();
    }

    private void resetPassword() {

        String emailText = email.getText().toString().trim();

        if (emailText.isEmpty()){
            email.setError("Email is required!!");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            email.setError("Please provide a valid Email!");
            email.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(emailText)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            // redirect to login
                            startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ForgotPassword.this, "Reset email has been sent!", Toast.LENGTH_LONG).show();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ForgotPassword.this, "Reset failed, please check your email!", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}