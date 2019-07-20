package com.example.Interview.geek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.Interview.geek.Auth.Login;
import com.example.Interview.geek.Blog.BlogFeedActivity;
import com.example.Interview.geek.Blog.BlogWriting;
import com.example.Interview.geek.Contest.Reminder.UpcomingContestActivity;
import com.example.Interview.geek.Interviewer.InterviewApplication;
import com.example.Interview.geek.Interviewer.InterviewDash;
import com.example.Interview.geek.User.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    CardView contestList, newsFeed, createBlog, profile, schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
//
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_main);

        createBlog = findViewById(R.id.testBlogwriting);
        newsFeed = findViewById(R.id.testFeed);
        profile = findViewById(R.id.testProfile);
        schedule = findViewById(R.id.testSchedule);

        contestList = findViewById(R.id.upcomingContest);

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InterviewDash.class);
                startActivity(intent);
            }
        });

        contestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpcomingContestActivity.class);
                startActivity(intent);
            }
        });

        createBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BlogWriting.class);
                startActivity(intent);
            }
        });

        newsFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BlogFeedActivity.class);
                startActivity(intent);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


        final ImageButton iButton = findViewById(R.id.toolbar_button);
        iButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, iButton);
                popup.getMenuInflater()
                        .inflate(R.menu.navigation, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.navigation_Logout:
                                Toast.makeText(MainActivity.this, "Logout clicked", Toast.LENGTH_LONG).show();
                                endActivity();
                            default:
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    private void endActivity() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        FirebaseAuth.getInstance().signOut();
        this.finish();
    }

    private void startProfile() {
        Toast.makeText(MainActivity.this, "Profile clicked", Toast.LENGTH_LONG).show();
        Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent1);
    }
}
