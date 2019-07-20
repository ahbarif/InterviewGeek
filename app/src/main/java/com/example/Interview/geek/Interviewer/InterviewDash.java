package com.example.Interview.geek.Interviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.Interview.geek.Contest.Reminder.ContestFeedAdapter;
import com.example.Interview.geek.R;

import java.util.ArrayList;

public class InterviewDash extends AppCompatActivity {

    CardView myslots, scheduler, available;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_dash);

        scheduler = findViewById(R.id.dashSchedule);
        myslots = findViewById(R.id.dashCirculars);
        available = findViewById(R.id.dashRequest);


        scheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterviewDash.this, InterviewApplication.class);
                startActivity(intent);


            }
        });

        myslots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterviewDash.this, MySlotActivity.class);
                startActivity(intent);
            }
        });

        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InterviewDash.this, AvailableInterviewActivity.class);
                startActivity(intent);
            }
        });


    }
}
