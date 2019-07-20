package com.example.Interview.geek.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Interview.geek.Blog.Blog;
import com.example.Interview.geek.Blog.BlogFeedActivity;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private TextView field_name, field_username, field_email;
    private TextView field_password, field_repeat;
    private Button signupButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mdatabase;
    private ArrayList<User> registeredUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_signup);


        field_name = findViewById(R.id.registerName);
        field_username = findViewById(R.id.registerUsername);
        field_email = findViewById(R.id.registerEmail);
        field_password = findViewById(R.id.registerPassword);
        field_repeat = findViewById(R.id.registerRepeatPassword);
        signupButton = findViewById(R.id.signupButton);
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();

        fetchData(new MyCallback() {
            @Override
            public void onCallback(ArrayList<User> list) {
                String ret = "data = ";
                for (int i = 0; i < list.size(); i++) {
                    ret = ret + list.get(i).getUsername() + "\n";
                }

                Toast.makeText(SignupActivity.this, ret, Toast.LENGTH_LONG).show();
                registeredUser = list;
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processData();
            }
        });


        ////////////////// dataaaaaaaaaaaaaaaaaaaaaaaaaaa
    }

    private interface MyCallback {
        void onCallback(ArrayList<User> list);
    }

    private void fetchData(final MyCallback myCallback) {

        DatabaseReference ref = mdatabase.getReference();
        ref.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<User> list = new ArrayList<>();

                for (DataSnapshot blogSnapshot : dataSnapshot.getChildren()) {
                    User usr = blogSnapshot.getValue(User.class);
                    list.add(usr);
                }
                myCallback.onCallback(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void processData() {
        String name = field_name.getText().toString().trim();
        String username = field_username.getText().toString().trim();
        String email = field_email.getText().toString().trim();
        String password = field_password.getText().toString();
        String repeated = field_repeat.getText().toString();


        if (verify(name, username, email, password, repeated) == false) return;

        createAccount(name, username, email, password);
    }

    private boolean verify(String name, String username, String email, String password, String repeated) {

        if (isValidName(name) == false) return false;
        if (isValidUsername(username) == false) return false;

        for (User registered : registeredUser) {
            if (registered.getUsername().equals(username)) {
                field_username.setError("Username already exists");
                return false;
            } else if (registered.getEmail().equals(email)) {
                field_email.setError("Email already exists");
                return false;
            }
        }

        if (isValidEmail(email) == false) {
            field_email.setError("Email not valid");
            return false;
        }

        if (password.length() < 6) {
            field_password.setError("Passowrd length must be at least six");
            return false;
        }

        if (password.equals(repeated) == false) {
            field_repeat.setError("Password don't match");
            return false;
        }
        return true;
    }

    private boolean isValidName(String name) {

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == ' ') continue;
            field_name.setError("Name must contain letters only");
            return false;
        }

        if (name.length() == 0) {
            field_name.setError("Name cannot be empty");
            return false;
        }

        return true;
    }

    private boolean isValidUsername(String username) {

        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'))
                continue;
            field_username.setError("User name must contain letters and digit only");
            return false;
        }

        if (username.length() < 6) {
            field_username.setError("Username length must be at least 6");
            return false;
        }

        return true;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private String getHash(String input) {
        long base = 67, mod = 961748927;
        long Hash = 0L;
        long p = 1;

        for (int i = 0; i < input.length(); i++) {
            Hash = Hash + input.charAt(i) * p;
            Hash %= mod;
            p = p * base;
            p %= mod;
        }

        return String.valueOf(Hash);
    }


    private void createAccount(final String name, final String username, final String email, final String password) {
        final String hashedPassword = getHash(password);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignupActivity.this, "Success!", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            User newUser = new User();
                            newUser.setEmail(email);
                            newUser.setName(name);
                            newUser.setPassword(hashedPassword);
                            newUser.setUsername(username);

                            signIn(newUser, password);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignupActivity.this, "Error!", Toast.LENGTH_LONG).show();
                            updateUI(null, null);
                        }
                    }
                });
    }


    private void updateUI(FirebaseUser user, User newUser) {
        if (user != null) {
            DatabaseReference ref = mdatabase.getReference().child("Users");
            ref.child(user.getUid()).setValue(newUser);

            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    private void signIn(final User newUser, String password) {

        String email = newUser.getEmail();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Sign in Problem", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignupActivity.this, "Sign in successful", Toast.LENGTH_LONG).show();
                    updateUI(mAuth.getCurrentUser(), newUser);
                }
            }
        });
    }
}