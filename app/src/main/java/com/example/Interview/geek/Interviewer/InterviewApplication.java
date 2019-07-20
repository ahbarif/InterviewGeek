package com.example.Interview.geek.Interviewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.Interview.geek.MainActivity;
import com.example.Interview.geek.R;
import com.example.Interview.geek.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InterviewApplication extends AppCompatActivity {

    private Spinner spinner1;
    private Button btnSubmit;
    private FirebaseDatabase mdatabase;
    private DatePicker datePicker;
    private TimePicker timePicker;

    private User loggedUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_application);

        mdatabase = FirebaseDatabase.getInstance();

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        btnSubmit = (Button) findViewById(R.id.buttonSubmit);

        datePicker = findViewById(R.id.myDatePicker);
        timePicker = findViewById(R.id.myTimePicker);

        loggedUser = new User();

        fetchData(new MyCallback() {
            @Override
            public void onCallback(User usr) {
                loggedUser = usr;
                Toast.makeText(getApplicationContext(), loggedUser.getName(), Toast.LENGTH_LONG).show();
            }
        });

        addListenerOnButton();
    }

    private void fetchData(final MyCallback myCallback) {

        DatabaseReference ref = mdatabase.getReference();
        final String interviewrID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                User usr = snapshot.child("Users").child(interviewrID).getValue(User.class);
                myCallback.onCallback(usr);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private interface MyCallback {
        void onCallback(User usr);
    }

    // add items into spinner dynamically


    // get the selected dropdown list value
    public void addListenerOnButton() {


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = String.valueOf(spinner1.getSelectedItem());
//                String date = datePicker.getda
                String date = String.valueOf(datePicker.getDayOfMonth()) + "-" + String.valueOf(datePicker.getMonth()) + "-" + String.valueOf(datePicker.getYear());
                int hour = timePicker.getCurrentHour().intValue();
                String ampm = "AM";

                if (hour >= 12) {
                    ampm = "PM";
                    hour -= 12;
                }
                if (hour == 0) {
                    ampm = "AM";
                    hour = 12;
                }

                String time = String.valueOf(hour) + ":" + String.valueOf(timePicker.getCurrentMinute()) + "" + ampm;
                upload(topic, date, time);
            }
        });
    }

    private void upload(String topic, String date, String time) {

        if(topic.equals("Select a topic")){
            Toast.makeText(getApplicationContext(), "Select a topic", Toast.LENGTH_LONG).show();
            return;
        }
        InterviewShoutout ist = new InterviewShoutout();
        ist.setTopic(topic);
        ist.setDate(date);
        ist.setTime(time);
        ist.setEmail(loggedUser.getEmail());
        ist.setCandidateID(FirebaseAuth.getInstance().getCurrentUser().getUid());

        DatabaseReference ref = mdatabase.getReference().child("Circulars");
        String key = ref.push().getKey();
        ist.setCircularID(key);
        ref.child(key).setValue(ist);
    }

}
