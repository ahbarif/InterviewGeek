package com.example.Interview.geek.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.Interview.geek.Blog.Blog;
import com.example.Interview.geek.Blog.BlogFeedActivity;
import com.example.Interview.geek.R;
import com.example.Interview.geek.Utility.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {

    private FirebaseDatabase mdatabase;
    private User currentUser;
    private String uid;
    private ImageView editButton, profilePIC;
    private TextView pName, pIns, pBio, pEmail, pPhone, pAddress, pDob;
    private TextView pCF, pHR, pTC, pUVA, pGit, pLinkedIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide(); // hide the title bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_tab);

        pCF = findViewById(R.id.profileCF);
        pHR = findViewById(R.id.profileHR);
        pTC = findViewById(R.id.profileTC);
        pUVA = findViewById(R.id.profileUVA);
        pGit = findViewById(R.id.profileGithub);
        pLinkedIN = findViewById(R.id.profileLinkedin);

        mdatabase = FirebaseDatabase.getInstance();

        editButton = findViewById(R.id.profileEditButton);
        pName = findViewById(R.id.profileName);
        pIns = findViewById(R.id.profileInstitution);
        pBio = findViewById(R.id.profileBio);
        pEmail = findViewById(R.id.profileEmail);
        pPhone = findViewById(R.id.profilePhone);
        pAddress = findViewById(R.id.profileAddress);
        pDob = findViewById(R.id.profileDOB);
        profilePIC = findViewById(R.id.profile_image);


        currentUser = new User();


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fetchData(new MyCallback() {
            @Override
            public void onCallback(User usr) {

                currentUser = usr;
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                        intent.putExtra("current", currentUser);
                        startActivity(intent);
                    }
                });

                populateFeed();

            }
        });

    }

    private void populateFeed() {
        if (currentUser.getName() != null) pName.setText(currentUser.getName());
        if (currentUser.getInstitution() != null) pIns.setText(currentUser.getInstitution());
        if (currentUser.getBio() != null) pBio.setText(currentUser.getBio());
        if (currentUser.getEmail() != null) pEmail.setText(currentUser.getEmail());
        if (currentUser.getPhone_number() != null) pPhone.setText(currentUser.getPhone_number());
        if (currentUser.getAddress() != null) pAddress.setText(currentUser.getAddress());
        if (currentUser.getDob() != null) pDob.setText(currentUser.getDob());

        if (currentUser.getCf_handle() != null) pCF.setText(currentUser.getCf_handle());
        if (currentUser.getHackerrank_handle() != null)
            pHR.setText(currentUser.getHackerrank_handle());
        if (currentUser.getTop_coder_handle() != null)
            pTC.setText(currentUser.getTop_coder_handle());
        if (currentUser.getUhunt_handle() != null) pUVA.setText(currentUser.getUhunt_handle());
        if (currentUser.getGithub() != null) pGit.setText(currentUser.getGithub());
        if (currentUser.getLinkedin() != null) pLinkedIN.setText(currentUser.getLinkedin());


        if (currentUser.getImgurl() != null && currentUser.getImgurl().equals("N/A") == false) {
            GlideApp.with(getApplicationContext())
                    .load(currentUser.getImgurl()).dontAnimate()
                    .override(100, 100)
                    .into(profilePIC);
        }
    }

    private void fetchData(final MyCallback myCallback) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        DatabaseReference ref = mdatabase.getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                User usr = snapshot.child("Users").child(uid).getValue(User.class);
                progressDialog.dismiss();
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

}