package com.example.Interview.geek.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Interview.geek.MainActivity;
import com.example.Interview.geek.R;
import com.example.Interview.geek.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView emailField, passwordField, signup;
    private Button loginButton;
    private String uName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        signup = findViewById(R.id.registerAccount);

        loginButton.setOnClickListener(this);
        signup.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.loginButton) {
            //    Toast.makeText(Login.this, "login pressed", Toast.LENGTH_LONG).show();
            signInWithUsername();
        } else if (id == R.id.registerAccount) {
            Intent intent = new Intent(Login.this, SignupActivity.class);
            startActivity(intent);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // go to news feed with user profile information
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    private void signIn(String email) {
        String password = passwordField.getText().toString();

        if (email.length() == 0 || password.length() == 0) {
            Toast.makeText(Login.this, "Sign in Problem", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(Login.this, "Sign in Problem", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Login.this, "Sign in successful", Toast.LENGTH_LONG).show();
                    updateUI(mAuth.getCurrentUser());
                }
            }
        });
    }

    private void signInWithUsername() {

        uName = emailField.getText().toString();

        signIn(uName);
    }

}
