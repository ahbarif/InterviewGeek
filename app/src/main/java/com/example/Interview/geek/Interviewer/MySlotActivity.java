package com.example.Interview.geek.Interviewer;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.Interview.geek.Blog.Blog;
import com.example.Interview.geek.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MySlotActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private FirebaseDatabase mdatabase;
    private FirebaseUser user;
    private ArrayList<InterviewShoutout> slotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_slot);

        recyclerView = findViewById(R.id.mySlotList);
        mdatabase = FirebaseDatabase.getInstance();
        slotList = new ArrayList<>();

        fetchData(new MyCallback() {
            @Override
            public void onCallback(ArrayList<InterviewShoutout> list) {
                String ret = "data = ";
                for (int i = 0; i < list.size(); i++) {
                    slotList.add(list.get(i));
                }


                Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_LONG).show();

                populateFeed();

            }
        });
    }

    private void populateFeed() {

        // eikhane adapter e array pathabo. simple
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final InterviewSlotAdapter adapter = new InterviewSlotAdapter(slotList, this.getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

    private void fetchData(final MyCallback myCallback) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final String myID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = mdatabase.getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref.child("Circulars").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<InterviewShoutout> list = new ArrayList<>();

                for (DataSnapshot blogSnapshot : dataSnapshot.getChildren()) {
                    InterviewShoutout ist = blogSnapshot.getValue(InterviewShoutout.class);
                    if(ist.getCandidateID().equals(myID))list.add(ist);
                }
                progressDialog.dismiss();
                myCallback.onCallback(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private interface MyCallback {
        void onCallback(ArrayList<InterviewShoutout> list);
    }
}
