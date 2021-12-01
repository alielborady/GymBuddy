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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private TextView mainRegister,forgotPassword;
    private Button loginButton;

    private EditText loginEmail, loginPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mainRegister = (TextView) findViewById(R.id.mainRegister);
        mainRegister.setOnClickListener(this);

        forgotPassword = (TextView) findViewById(R.id.mainForgotPassword);
        forgotPassword.setOnClickListener(this);
        
        loginButton = (Button) findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(this);

        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mainRegister:
                startActivity(new Intent(this, UserRegister.class));
                break;

            case R.id.mainForgotPassword:
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
                break;
                
            case R.id.loginbutton:
                userLogin();
        }
    }

    private void userLogin() {

        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if(email.isEmpty()){
            loginEmail.setError("Email is required!!");
            loginEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmail.setError("Please provide a valid Email!");
            loginEmail.requestFocus();
        }

        if(password.isEmpty()){
            loginPassword.setError("Password is required!!");
            loginPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            loginPassword.setError("Password must be 6 or more characters!!");
            loginPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if(user.isEmailVerified()){
                                // redirect to home pae
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            }else {
                                user.sendEmailVerification();
                                Toast.makeText(MainActivity.this, "Please verify you email, a verification email has been sent to you.", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Failed to login. Please check your credentials then retry", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}